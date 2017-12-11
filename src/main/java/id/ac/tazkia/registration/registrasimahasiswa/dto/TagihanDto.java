package id.ac.tazkia.registration.registrasimahasiswa.dto;

import id.ac.tazkia.registration.registrasimahasiswa.entity.JenisBiaya;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class TagihanDto {
    @NotNull
    @NotEmpty
    private JenisBiaya jenisBiaya;
    @NotNull
    @NotEmpty
    private String  nomorTagihan;
    private String keterangan;

    @NotNull @Column(columnDefinition = "DATE")
    private LocalDate tanggalTagihan;

    private String  nilai;

    private Boolean lunas;
}
