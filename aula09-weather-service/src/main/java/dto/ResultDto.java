package dto;

public class ResultDto {
    LocationDto[] result;
    ResultDto( LocationDto[] result) {
        this.result = result;
    }

    public LocationDto[] getLocations() {
        return result;
    }
}
