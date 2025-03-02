package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer page, Integer size);
    Page<BeerDTO> listBeers();

    BeerDTO getBeerById(UUID id);

    void deleteBeer(UUID beerId);

    BeerDTO createBeer(BeerDTO beerDTO);

}
