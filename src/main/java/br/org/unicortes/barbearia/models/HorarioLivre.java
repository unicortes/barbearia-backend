package br.org.unicortes.barbearia.models;

import br.org.unicortes.barbearia.dtos.HorarioLivreDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_horarios_livres")
public class HorarioLivre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Barbeiro é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @NotNull(message = "Serviço é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @NotNull(message = "Data/hora inicial é obrigatória")
    @FutureOrPresent(message = "Data/hora deve ser atual ou futura")
    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @NotNull(message = "Data/hora final é obrigatória")
    @Future(message = "Data/hora deve ser futura")
    @Column(name = "fim", nullable = false)
    private LocalDateTime fim;

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private boolean disponivel = true;

    @Version
    private Long version;
}
