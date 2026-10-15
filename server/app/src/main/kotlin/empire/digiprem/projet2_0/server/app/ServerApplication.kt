package empire.digiprem.projet2_0.server.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync

private const val BASE_PKG = "empire.digiprem.projet2_0.server"

@SpringBootApplication(scanBasePackages = [BASE_PKG])
@EnableJpaRepositories(basePackages = [BASE_PKG])
@EntityScan(basePackages = [BASE_PKG])
@EnableAsync
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}
