package id.ac.tazkia.registration.registrasimahasiswa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadError {
    private Integer baris;
    private String keterangan;
    private String data;
}
