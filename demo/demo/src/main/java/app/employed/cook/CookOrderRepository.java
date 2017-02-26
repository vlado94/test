package app.employed.cook;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CookOrderRepository extends JpaRepository<CookOrder, CookOrderId>{

}
