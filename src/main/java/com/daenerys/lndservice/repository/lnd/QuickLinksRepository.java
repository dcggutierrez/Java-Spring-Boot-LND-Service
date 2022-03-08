package com.daenerys.lndservice.repository.lnd;

import com.daenerys.lndservice.model.lnd.QuickLinksEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuickLinksRepository extends JpaRepository<QuickLinksEntity, String> {
}
