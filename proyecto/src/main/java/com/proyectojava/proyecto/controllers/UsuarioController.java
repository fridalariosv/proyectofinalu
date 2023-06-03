package com.proyectojava.proyecto.controllers;

import com.proyectojava.proyecto.dao.UsuarioDao;
import com.proyectojava.proyecto.models.Usuario;
import com.proyectojava.proyecto.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UsuarioController {

    @Autowired

    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;
    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.GET)
    public Usuario getUsuario(@PathVariable Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Frida");
        usuario.setApellido("Larios");
        usuario.setEmail("fridalariosv@gmail.com");
        usuario.setTelefono("37373778");
        return usuario;
    }
    /*** Mandamos a llamar a los usuarios*/
    @RequestMapping(value = "api/usuarios", method = RequestMethod.GET)
    public List<Usuario> getUsuarios(@RequestHeader(value="Authorization") String token) {
        if (!validarToken(token)) { return null; }

        return usuarioDao.getUsuarios();
    }

    private boolean validarToken(String token) {
        String usuarioId = jwtUtil.getKey(token);
        return usuarioId != null;
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registrarUsuario(@RequestBody Usuario usuario) {

        /* Proceso de HASH */
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
        usuario.setPassword(hash);

        usuarioDao.registrar(usuario);
    }

    @RequestMapping(value = "usuario123")
    public Usuario editar() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Frida");
        usuario.setApellido("Larios");
        usuario.setEmail("fridalariosv@gmail.com");
        usuario.setTelefono("37373778");
        return usuario;
    }

    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void eliminar(@RequestHeader(value="Authorization") String token, @PathVariable Long id) {
        if (!validarToken(token)) { return; }
        usuarioDao.eliminar(id);
    }
}
