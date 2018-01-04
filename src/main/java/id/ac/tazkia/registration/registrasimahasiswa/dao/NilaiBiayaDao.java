package id.ac.tazkia.registration.registrasimahasiswa.dao;

import id.ac.tazkia.registration.registrasimahasiswa.entity.JenisBiaya;
import id.ac.tazkia.registration.registrasimahasiswa.entity.NilaiBiaya;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NilaiBiayaDao extends PagingAndSortingRepository <NilaiBiaya, String> {
    Page<NilaiBiaya> findByJenisBiaya(JenisBiaya jd, Pageable page);

    Page<JenisBiaya> findByJenisBiayaContainingIgnoreCaseOrderByJenisBiaya(String nama, Pageable page);
}
