package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    public static final String BEERS_URL = "/api/v1/beers";
    public static final String BEER_POST_URL = "/api/v1/beer";
    public static final String GET_BEER_BY_ID_URL = "/api/v1/beer/{id}";
    private final RestTemplateBuilder restTemplateBuilder;
    private final RestClient.Builder builder;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer page, Integer size) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BEERS_URL);

        if (beerName != null) {
            builder.queryParam("beerName", beerName);
        }
        if (beerStyle != null) {
            builder.queryParam("beerStyle", beerStyle);
        }
        if (showInventory != null) {
            builder.queryParam("showInventory", showInventory);
        }
        if (page != null) {
            builder.queryParam("pageNumber", page);
        }
        if (size != null) {
            builder.queryParam("pageSize", size);
        }


        ResponseEntity<BeerDTOPageImpl> response = restTemplate.getForEntity(builder.toUriString(), BeerDTOPageImpl.class);
        List<BeerDTO> beers =  response.getBody().getContent();

        beers.forEach(beerDTO -> System.out.println(beerDTO.getBeerName()));

        return response.getBody();
    }

    @Override
    public Page<BeerDTO> listBeers() {
        return this.listBeers(null, null, null, null, null);
    }

    @Override
    public BeerDTO getBeerById(UUID id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(GET_BEER_BY_ID_URL, BeerDTO.class, id);
    }

    @Override
    public void deleteBeer(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(GET_BEER_BY_ID_URL, beerId);
    }

    @Override
    public BeerDTO createBeer(BeerDTO beerDTO) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        URI uri = restTemplate.postForLocation(BEER_POST_URL, beerDTO);

        assert uri != null;
        return restTemplate.getForObject(uri.getPath(), BeerDTO.class);
    }
}
