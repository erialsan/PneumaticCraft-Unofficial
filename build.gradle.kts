
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

dependencies {
    // Required dependencies
    implementation("maven.modrinth:codechickenlib-unofficial:1.3.0")
    implementation("curse.maven:codechickencore-222213:3293859")
    implementation("maven.modrinth:notenoughitems-unofficial:2.7.44-GTNH")
    implementation("curse.maven:forge-multi-part-229323:2242993")
    implementation("curse.maven:waila-73488:2230518")
    implementation("curse.maven:buildcraft-61811:6240755")

    // Optional/API dependencies (compileOnly)
    compileOnly("curse.maven:industrial-craft-242638:6833054")
    compileOnly("com.github.GTNewHorizons:OpenComputers:1.12.26-GTNH:api")
    compileOnly("curse.maven:ee3-65509:2305022")

    // In-Game Wiki Mod
    implementation("curse.maven:in-game-wiki-mod-223815:2247673")

    // Local jars fallback for anything not resolvable via Maven
    compileOnly(fileTree("libs") { include("*.jar") })
    implementation(fileTree("libs") { include("*.jar") })
}
