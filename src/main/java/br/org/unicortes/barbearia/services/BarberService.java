package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.exceptions.BarberAlreadyExistsException;
import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import br.org.unicortes.barbearia.repositories.BarberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BarberService {
    @Autowired
    private BarberRepository barberRepository;

    public Barber createBarber(Barber barber) {
        if (barberRepository.existsByName(barber.getName())) {
            throw new BarberAlreadyExistsException(barber.getName());
        }
        return barberRepository.save(barber);
    }

    public Barber updateBarber(Long id, Barber barberAtualizado) {
        Barber barber = barberRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id));
        barber.setName(barberAtualizado.getName());
        barber.setEmail(barberAtualizado.getEmail());
        barber.setPhone(barberAtualizado.getPhone());
        barber.setCpf(barberAtualizado.getCpf());
        barber.setSalary(barberAtualizado.getSalary());
        return barberRepository.save(barber);
    }

    public void deleteBarber(Long id) {
        Barber barber = barberRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id));
        barberRepository.delete(barber);
    }

    public List<Barber> listAllBarbers() {
        return barberRepository.findAll();
    }
}
