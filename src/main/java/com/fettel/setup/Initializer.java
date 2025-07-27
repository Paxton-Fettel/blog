package com.fettel.setup;

import com.fettel.model.Category;
import com.fettel.model.Role;
import com.fettel.repository.CategoryRepository;
import com.fettel.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;

    public Initializer(RoleRepository roleRepository, CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        if(categoryRepository.count() == 0) {
            categoryRepository.save(new Category("Technology"));
            categoryRepository.save(new Category("Business"));
            categoryRepository.save(new Category("Health"));
            categoryRepository.save(new Category("Food"));
            categoryRepository.save(new Category("Gym"));
            categoryRepository.save(new Category("Game"));
        }
    }
}
