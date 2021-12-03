package br.com.caelum.eats.restaurante;

import br.com.caelum.eats.administrativo.TipoDeCozinha;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
class RestauranteController {

    private RestauranteRepository restauranteRepo;
    private CardapioRepository cardapioRepo;
    private DistanciaRestClient distanciaRestClient;

    @GetMapping("/restaurantes/{id}")
    RestauranteDto detalha(@PathVariable("id") Long id) {
        Restaurante restaurante = restauranteRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        return new RestauranteDto(restaurante);
    }

    @GetMapping("/parceiros/restaurantes/do-usuario/{username}")
    public RestauranteDto detalhaParceiro(@PathVariable("username") String username) {
        Restaurante restaurante = restauranteRepo.findByUsername(username);
        return new RestauranteDto(restaurante);
    }

    @GetMapping("/restaurantes")
    List<RestauranteDto> detalhePorIds(@RequestParam("ids") List<Long> ids) {
        return restauranteRepo.findAllById(ids).stream().map(RestauranteDto::new).collect(Collectors.toList());
    }

    @GetMapping("/parceiros/restaurantes/{id}")
    RestauranteDto detalhaParceiro(@PathVariable("id") Long id) {
        Restaurante restaurante = restauranteRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        return new RestauranteDto(restaurante);
    }

    @PostMapping("/parceiros/restaurantes")
    Restaurante adiciona(@RequestBody Restaurante restaurante) {
        restaurante.setAprovado(false);
        Restaurante restauranteSalvo = restauranteRepo.save(restaurante);
        Cardapio cardapio = new Cardapio();
        cardapio.setRestaurante(restauranteSalvo);
        cardapioRepo.save(cardapio);
        return restauranteSalvo;
    }

    @Transactional
    @PutMapping("/parceiros/restaurantes/{id}")
    public RestauranteDto atualiza(@RequestBody RestauranteDto restaurante) {
        Restaurante doBD = restauranteRepo.getOne(restaurante.getId());
        TipoDeCozinha tipoDeCozinhaOriginal = doBD.getTipoDeCozinha();
        String cepOriginal = doBD.getCep();
        restaurante.populaRestaurante(doBD);
        Restaurante salvo = restauranteRepo.save(doBD);
        if (!tipoDeCozinhaOriginal.getId().equals(restaurante.getTipoDeCozinha().getId())
                ||
                !cepOriginal.equals(restaurante.getCep())) {
            distanciaRestClient.restauranteAtualizado(salvo);
        }
        return new RestauranteDto(salvo);
    }


    @GetMapping("/admin/restaurantes/em-aprovacao")
    List<RestauranteDto> emAprovacao() {
        return restauranteRepo.findAllByAprovado(false).stream().map(RestauranteDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @PatchMapping("/admin/restaurantes/{id}")
    public void aprova(@PathVariable("id") Long id) {
        restauranteRepo.aprovaPorId(id);
        Restaurante restaurante = restauranteRepo.getOne(id);
        distanciaRestClient.novoRestauranteAprovado(restaurante);
    }
}
