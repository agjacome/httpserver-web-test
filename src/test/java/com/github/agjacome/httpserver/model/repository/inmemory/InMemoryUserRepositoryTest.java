package com.github.agjacome.httpserver.model.repository.inmemory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.model.repository.UserRepository;
import com.github.agjacome.httpserver.model.repository.exception.EntityAlreadyExistsException;
import com.github.agjacome.httpserver.model.repository.exception.EntityDoesNotExistException;
import com.github.agjacome.httpserver.util.Arrays;
import com.github.agjacome.httpserver.util.CaseInsensitiveString;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assume.assumeFalse;

import static com.github.agjacome.httpserver.model.User.Role.ADMIN;
import static com.github.agjacome.httpserver.model.User.Role.PAGE_1;
import static com.github.agjacome.httpserver.model.User.Role.PAGE_2;
import static com.github.agjacome.httpserver.model.User.Role.PAGE_3;
import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

@RunWith(Parameterized.class)
public class InMemoryUserRepositoryTest {

    private static User user(final String username, final Role ... roles) {
        return new User(uncased(username), "pass"::equals, Arrays.asSet(roles));
    }

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[]> createUserData() {
        return asList(new Object[][] {
            { "empty", new User[] {
                // empty
            }},
            { "single", new User[] {
                user("the_single_user", ADMIN)
            }},
            { "mutiple", new User[] {
                user("admin"         , ADMIN),
                user("multiple_one"  , PAGE_1),
                user("two_multiple"  , PAGE_3),
                user("the_multiple_3", PAGE_1, PAGE_3),
                user("non-matching")
            }}
        });
    }

    private UserRepository repository;

    private final CaseInsensitiveString matching;
    private final List<User>            users;

    public InMemoryUserRepositoryTest(final String matching, final User[] users) {
        this.matching = uncased(matching);
        this.users    = asList(users);
    }

    @Before
    public void createInMemoryUserRepository() {
        repository = InMemoryUserRepository.of(users);
    }

    @Test
    public void get_must_return_empty_if_no_user_matches_username() {
        assertThat(repository.get(uncased("unmatched-username"))).isEmpty();
    }

    @Test
    public void get_must_return_user_with_matching_username() {
        final Optional<User> expected = users.stream()
            .filter(u -> u.getId().equals(matching)).findFirst();

        assertThat(repository.get(matching)).isEqualTo(expected);
    }

    @Test
    public void search_must_return_builder_with_users_satisfying_predicate() {
        final Predicate<User> pred1 = u -> u.checkPassword("pass");
        assertThat(repository.search(pred1).build()).containsOnlyElementsOf(
            users.stream().filter(pred1).collect(toList())
        );

        final Predicate<User> pred2 = u -> u.getUsername().startsWith("t");
        assertThat(repository.search(pred2).build()).containsOnlyElementsOf(
            users.stream().filter(pred2).collect(toList())
        );
    }

    @Test
    public void search_by_username_must_return_builder_with_users_partially_matching_username() {
        final List<User> matchedUsers = users.stream().filter(
            u -> u.getUsername().contains(matching.getOriginal())
        ).collect(toList());

        assertThat(repository.searchByUsername(matching.getOriginal()).build())
            .containsOnlyElementsOf(matchedUsers);
    }

    @Test
    public void search_by_role_must_return_builder_with_users_matching_role() {
        final List<User> admins = users.stream()
            .filter(u -> u.hasRole(ADMIN)).collect(toList());
        assertThat(repository.searchByRole(ADMIN).build())
            .containsOnlyElementsOf(admins);

        final List<User> page1 = users.stream()
            .filter(u -> u.hasRole(PAGE_1)).collect(toList());
        assertThat(repository.searchByRole(PAGE_1).build())
            .containsOnlyElementsOf(page1);
    }

    @Test
    public void list_must_return_builder_with_all_users() {
        assertThat(repository.list().build()).containsOnlyElementsOf(users);
    }

    @Test
    public void count_must_return_number_of_users_satisfying_predicate() {
        final Predicate<User> pred1 = u -> u.checkPassword("pass");
        assertThat(repository.count(pred1)).isEqualTo(
            (int) users.stream().filter(pred1).count()
        );

        final Predicate<User> pred2 = u -> u.getUsername().startsWith("t");
        assertThat(repository.count(pred2)).isEqualTo(
            (int) users.stream().filter(pred2).count()
        );
    }

    @Test
    public void count_must_return_total_number_of_users() {
        assertThat(repository.count()).isEqualTo(users.size());
    }

    @Test
    public void create_must_throw_exception_if_user_already_exists() {
        assumeFalse(users.isEmpty());
        final User user = users.get(0);

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
            .isThrownBy(() -> repository.create(user));
    }

    @Test
    public void create_must_return_newly_inserted_user_and_update_repository() {
        final User user = user("non-existent-user", PAGE_1);
        assumeFalse(users.contains(user));

        assertThat(repository.create(user)).isEqualTo(user);
        assertThat(repository.get(user.getId())).contains(user);
    }

    @Test
    public void update_must_throw_exception_if_user_does_not_exist() {
        final User user = user("non-existent-user", PAGE_1);
        assumeFalse(users.contains(user));

        assertThatExceptionOfType(EntityDoesNotExistException.class)
            .isThrownBy(() -> repository.update(user));
    }

    @Test
    public void update_must_return_newly_updated_user_and_update_repository() {
        assumeFalse(users.isEmpty());
        final User user = users.get(0).withAddedRole(PAGE_2);

        assertThat(repository.update(user)).isEqualTo(user);
        assertThat(repository.get(user.getId())).hasValueSatisfying(u -> {
            assertThat(u).isEqualTo(user);
            assertThat(u.hasRole(PAGE_2)).isTrue();
        });
    }

    @Test
    public void save_must_create_user_if_user_does_not_exist() {
        final User user = user("non-existent-user", PAGE_1);
        assumeFalse(users.contains(user));

        assertThat(repository.save(user)).isEqualTo(user);
        assertThat(repository.get(user.getId())).contains(user);
    }

    @Test
    public void save_must_return_update_user_if_already_exists() {
        assumeFalse(users.isEmpty());
        final User user = users.get(0).withAddedRole(PAGE_2);

        assertThat(repository.save(user)).isEqualTo(user);
        assertThat(repository.get(user.getId())).hasValueSatisfying(u -> {
            assertThat(u).isEqualTo(user);
            assertThat(u.hasRole(PAGE_2)).isTrue();
        });
    }

    @Test
    public void delete_must_throw_exception_if_user_does_not_exist() {
        final User user = user("non-existent-user", PAGE_1);
        assumeFalse(users.contains(user));

        assertThatExceptionOfType(EntityDoesNotExistException.class)
            .isThrownBy(() -> repository.delete(user));
    }

    @Test
    public void delete_must_delete_user_from_repository() {
        assumeFalse(users.isEmpty());
        final User user = users.get(0);

        repository.delete(user);
        assertThat(repository.get(user.getId())).isEmpty();
    }

}
