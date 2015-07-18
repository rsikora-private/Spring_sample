package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.demo.core.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
