package br.org.unicortes.barbearia.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.org.unicortes.barbearia.dtos.StockDTO;
import br.org.unicortes.barbearia.dtos.UsuarioDTO;
import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.models.Stock;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.services.UsuarioService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios().stream().map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = this.usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(convertToDTO(usuario));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = this.convertToEntity(usuarioDTO);
        Usuario usuarioAtualizado = this.usuarioService.updateUsuario(usuario);
        return ResponseEntity.ok(convertToDTO(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        this.usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setName(usuario.getName());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setRole(usuario.getRole());
        return usuarioDTO;
    }

    private Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setName(usuarioDTO.getName());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setRole(usuarioDTO.getRole());
        return usuario;
    }
}
