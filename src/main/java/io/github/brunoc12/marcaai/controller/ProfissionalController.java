package io.github.brunoc12.marcaai.controller;

import io.github.brunoc12.marcaai.model.Profissional;
import io.github.brunoc12.marcaai.service.ProfissionalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Profissional salvar(@RequestBody Profissional profissional) {
        return profissionalService.salvar(profissional);
    }

    @GetMapping("/{id}")
    public Profissional buscarPorId(@PathVariable Long id) {
        return profissionalService.buscarPorId(id);
    }

    @GetMapping
    public List<Profissional> buscarTodos() {
        return profissionalService.buscarTodos();
    }

    @PutMapping("/{id}")
    public Profissional atualizar(@PathVariable Long id, @RequestBody Profissional profissional) {
        return profissionalService.atualizar(id, profissional);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        profissionalService.excluir(id);
    }
}