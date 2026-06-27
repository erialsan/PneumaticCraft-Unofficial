# PneumaticCraft GTNH移植計画 (AppleMilkTea2方式)

## 方針
AppleMilkTea2-Unofficial と同様の方式で移植する:
1. **依存関係は `build.gradle.kts` に直接記述**
2. **`dependencies.gradle`** はコメントのみにして実質空にする
3. **`repositories.gradle`** も実質空にして、依存解決は well-known repos (CurseMaven, Modrinth) に任せる
4. **CurseForge Maven** (`curse.maven:name-projectId:fileId`) を最優先
5. **Modrinth Maven** (`maven.modrinth:project:version`) を次に使用
6. **GTNH Maven / アップストリーム Maven** をさらに次に使用
7. **どうしても解決できないものは `libs/*.jar`** で対応
8. **`Versions.java`** を GTNH の `Tags.VERSION` 方式に修正

## 修正ファイル一覧

### 1. `repositories.gradle`
- mobiusstrip.eu を削除（死んでいる）
- `repositories { }` を空にする（CurseMaven + Modrinth は well-known で自動解決）
- ただし IGW-Mod の maven.k-4u.nl は残す（代替手段がないため）
- OpenComputers の maven.cil.li も残す（API jar が Modrinth にない場合のフォールバック）

### 2. `dependencies.gradle`
- すべての実依存を削除し、コメントブロックのみにする（AppleMilkTea2 と同様）

### 3. `build.gradle.kts`
- `dependencies { }` ブロックを追加
- 以下の依存関係を設定:

| 依存 | 種別 | 解決方法 | Coordinate |
|------|------|---------|------------|
| CodeChickenLib | `implementation` | Modrinth Maven | `maven.modrinth:codechickenlib-unofficial:1.3.0` |
| CodeChickenCore | `implementation` | CurseForge | `curse.maven:codechickencore-222213:3293859` |
| NotEnoughItems | `implementation` | Modrinth | `maven.modrinth:notenoughitems-unofficial:2.7.44-GTNH` |
| ForgeMultipart | `implementation` | CurseForge | `curse.maven:forge-multi-part-229323:2242993` |
| WAILA | `implementation` | CurseForge | `curse.maven:waila-73488:2230518` |
| BuildCraft | `implementation` | CurseForge | `curse.maven:buildcraft-61811:6240755` |
| IC2 API | `compileOnly` | CurseForge | `curse.maven:industrial-craft-242638:6833054` |
| IGW-Mod | `implementation` | IGW Maven or `libs/` | `igwmod:IGW-Mod-1.7.10:1.1.3-18:userdev` |
| OpenComputers | `compileOnly` | OC Maven or `libs/` | `li.cil.oc:OpenComputers:MC1.7.10-1.4.5.28:api` |
| EE3 | `compileOnly` | CurseForge | `curse.maven:ee3-65509:2305022` |
| フォールバック | `compileOnly+implementation` | libs/ | `fileTree("libs") { include("*.jar") }` |

### 4. `gradle.properties`
- `customArchiveBaseName = PneumaticCraft` を追加
- `disableCheckstyle = true` を追加
- `gtnh.modules.gitVersion = false` を追加
- `modVersion = 1.12.x` を追加（オリジナル版に合わせる）

### 5. `Versions.java`
```java
package pneumaticCraft.lib;
import pneumaticCraft.Tags;
public class Versions{
    public static String fullVersionString(){
        return Tags.VERSION;
    }
}
```

### 6. `PneumaticCraft.java`
- `import pneumaticCraft.Tags;` を追加（すでに Versions.java 経由で使用するので不要かも）
- `@Mod` アノテーションに `version = Tags.VERSION` を追加（任意、なくても PreInit で設定される）

## フォールバック計画
各依存が Maven で解決できない場合:
- `libs/` ディレクトリを作成し、該当する jar をダウンロード
- `implementation(fileTree("libs") { include("*.jar") })` ですべて読み込み
- 個別の Maven coordinate をコメントアウト or 削除

## ビルド確認
```bash
./gradlew compileJava
```

エラーが出た場合:
1. どの依存が解決できないかを確認
2. 該当 jar を `libs/` に配置
3. 再ビルド
