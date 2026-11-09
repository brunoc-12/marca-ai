package io.github.brunoc12.marcaai.controller;




import io.github.brunoc12.marcaai.model.Servico;
import io.github.brunoc12.marcaai.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servico salvar (@RequestBody Servico servico){
        return servicoService.salvar(servico);

    }

    @GetMapping("/{id}")
    public Servico buscarPorId(@PathVariable Long id) {
        return servicoService.buscarPorId(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Servico> buscarTodos(){
        return servicoService.buscarTodos();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id){
        servicoService.excluir(id);

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Servico atualizar (@PathVariable Long id, @RequestBody Servico servico){
        return servicoService.atualizar(id, servico);
    }




}
