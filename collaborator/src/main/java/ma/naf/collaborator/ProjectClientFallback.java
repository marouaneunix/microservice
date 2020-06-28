package ma.naf.collaborator;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class ProjectClientFallback implements ProjectClient {
    private final Throwable cause;

    public ProjectClientFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public List<String> getProjectsName() {
        log.error(cause.getMessage());
        return Collections.emptyList();
    }
}
