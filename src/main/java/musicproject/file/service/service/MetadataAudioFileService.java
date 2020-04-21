package musicproject.file.service.service;

import musicproject.file.service.exception.FileProcessingException;
import musicproject.file.service.model.FileMetaResponse;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class MetadataAudioFileService {
    /*add more file formats*/
    public FileMetaResponse getSongInfo(File file ) {

        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag audioTag = audioFile.getTag();

            return FileMetaResponse.builder()
                    .title(audioTag.getFirst(FieldKey.TITLE))
                    .author(audioTag.getFirst(FieldKey.ARTIST))
                    .album(audioTag.getFirst(FieldKey.ALBUM))
                    .genre(audioTag.getFirst(FieldKey.GENRE))
                    .duration(getDurationWithMp3Spi(file))
                    .source(FileMetaResponse.FILE_FORMAT.MP3.getValue())
                    .build();

        } catch (CannotReadException e) {
            throw  new FileProcessingException("File extension not supported");
        } catch (IOException | TagException | InvalidAudioFrameException | ReadOnlyFileException | NoSuchMethodError e) {
            throw  new FileProcessingException("Cannot read metadata");
        }
    }

    private String getDurationWithMp3Spi(final File file) {
        AudioFileFormat fileFormat;

        try {
            fileFormat = AudioSystem.getAudioFileFormat(file);

        } catch (UnsupportedAudioFileException | IOException e) {
            return "";
        }

        if (fileFormat instanceof TAudioFileFormat) {
            Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
            Long microseconds = (Long) properties.get("duration");

            int mili = (int) (microseconds / 1000);
            int sec = (mili / 1000) % 60;
            int min = (mili / 1000) / 60;

            if(sec < 10) return min + ":0" + sec + " MIN";
            else return min + ":" + sec + " MIN";

        } else {
            return "";
        }
    }
}
