package dto;

import com.google.gson.annotations.SerializedName;

public class SearchApiDto {
    @SerializedName("search_api")
    private ResultDto result;

    public SearchApiDto(ResultDto result) {
        this.result = result;
    }
    public ResultDto getResult() {
        return result;
    }

}
