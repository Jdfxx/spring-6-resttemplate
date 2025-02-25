package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void testListBeersNoName() {
        beerClient.listBeers(null, null, null, null, null);
    }

    @Test
    void testListBeersWithName() {
        beerClient.listBeers("Ale",null, null, null, null);
    }

    @Test
    void testListBeersWithBeerStyle() {
        beerClient.listBeers(null, BeerStyle.PORTER, null, null, null);
    }

    @Test
    void testListBeersWithShowInventory() {
        beerClient.listBeers(null, BeerStyle.PORTER, true, null, null);
    }

    @Test
    void testListBeersWithpage2() {
        beerClient.listBeers(null, null, true, 2, null);
    }

    @Test
    void testListBeersWithSize50() {
        beerClient.listBeers(null, null, true, null, 50);
    }


    @Test
    void getBeerById() {
        Page<BeerDTO> beers = beerClient.listBeers();
        BeerDTO beerDTO = beers.getContent().getFirst();

        BeerDTO beerDTOById = beerClient.getBeerById(beerDTO.getId());

        assertThat(beerDTOById).isNotNull();
        assertThat(beerDTO.getBeerName()).isEqualTo(beerDTOById.getBeerName());
    }

    @Test
    void postBeerById() {
        Page<BeerDTO> beers = beerClient.listBeers();
        BeerDTO beerDTO = beers.getContent().getFirst();
        beerDTO.setId(null);
        beerDTO.setBeerName("SOME TEST BEER");
        BeerDTO beerDTOById = beerClient.createBeer(beerDTO);

        assertThat(beerDTOById).isNotNull();
        assertThat(beerDTO.getBeerName()).isEqualTo(beerDTOById.getBeerName());
    }

    @Test
    void testDeleteBeer() {
        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs 2")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO beerDto = beerClient.createBeer(newDto);

        beerClient.deleteBeer(beerDto.getId());

        assertThrows(HttpClientErrorException.class, () -> {
            //should error
            beerClient.getBeerById(beerDto.getId());
        });
    }
}