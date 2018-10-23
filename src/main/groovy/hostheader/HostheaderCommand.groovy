package hostheader

import groovy.util.logging.Slf4j
import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import picocli.CommandLine.Command
import picocli.CommandLine.Option

import javax.inject.Inject

@Slf4j
@Command(name = 'hostheader', description = '...',
        mixinStandardHelpOptions = true)
class HostheaderCommand implements Runnable {
    @Client( 'http://localhost:8081/' ) @Inject RxHttpClient httpClient

    @Option(names = ['-v', '--verbose'], description = '...')
    boolean verbose

    static void main(String[] args) throws Exception {
        PicocliRunner.run(HostheaderCommand.class, args)
    }

    void run() {
        log.info( 'Making REST call...')
        httpClient.exchange( HttpRequest.POST( '/', 'testing' ) ).blockingFirst()
        log.info( '...done' )
    }
}
