package br.org.unicortes.barbearia.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractResponse<T> {
    private boolean success;
    private String message;
    private String errorCode;
    private T data;

    public static <T>AbstractResponse<T> success(T data) {
        return new AbstractResponse<>(true, "Operação realizada com sucesso", null, data);
    }

    public static <T>AbstractResponse<T> success(T data, String message) {
        return new AbstractResponse<>(true, message, null, data);
    }

    public static <T>AbstractResponse<T> error(String message, String errorCode) {
        return new AbstractResponse<>(false, message, errorCode, null);
    }
}
