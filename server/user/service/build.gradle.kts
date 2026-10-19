plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:user:infra"))
    implementation(project(":shared-contracts"))
    implementation(libs.spring.boot.starter.mail) // OtpService envoie les codes OTP par email
}
