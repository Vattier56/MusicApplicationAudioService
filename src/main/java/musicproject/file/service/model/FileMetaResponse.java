package musicproject.file.service.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class FileMetaResponse {

    public enum FILE_FORMAT {
        MP3("Mp3"),
        WMA("wma"),
        AIFF("aiff"),
        WAV("wav");

        private String value;

        FILE_FORMAT(String fileFormat) { this.value = fileFormat; }

        public String getValue() { return  this.value; }
    }

    private String title;
    private String author;
    private String album;
    private String genre;
    private String duration;
    private final String source;

}
