package ru.heumn.Procat.storage.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.heumn.Procat.storage.enums.Role;

import java.time.Instant;
import java.util.Set;


@Data
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;

    @NotNull(message = "Поле логина обязательно")
    @NotEmpty(message = "Поле логина не должно быть пустое")
    @Size(min = 5, max = 50, message = "Логин не должен быть меньше 5 и больше 50")
    String login;

    @NotNull(message = "Поле имени обязательно")
    @NotEmpty(message = "Поле имени не должно быть пустое")
    @Size(min = 2, max = 20, message = "Имя не должено быть меньше 2 и больше 20")
    String name;

    @NotNull(message = "Поле фамилии обязательно")
    @NotEmpty(message = "Поле фамилии не должно быть пустое")
    @Size(min = 2, max = 25, message = "Фамилия не должена быть меньше 2 и больше 25")
    String lastName;

    @Size(min = 2, max = 25, message = "Отчетсво не должена быть меньше 2 и больше 25")
    String patronymic;

    @Size(min = 6, max = 60, message = "пароль не должена быть меньше 6 и больше 60")
    @NotNull(message = "Поле пароль обязательно")
    @NotEmpty(message = "Поле пароль не должно быть пустое")
    String password;

    Instant dateCreate;

    Boolean active;

    Instant lastLogin;

    @Enumerated(EnumType.STRING)
    Set<Role> roles;
}
