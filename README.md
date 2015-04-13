# meu-onibus-android
### Meu Ônibus - Projeto M2M / Druid
#### Core:
* Mapas 
* Localização do usuário
* Trajetos no mapa
* Favoritar pontos de ônibus
* Notícias
* Tutorial

#### Configurações no Projeto:
* NOMES DO MENU  - res/values/strings.xml
* URL DE SERVIÇOS - src/br.com.m2m.meuonibus.models.ws/MeuOnibusWS.java
* Gerar assinatura do aplicativo: https://developer.android.com/tools/publishing/app-signing.html
* Depois de criar a API KEY no google console substituir a que já está sendo usada no AndroidManifest.xml.<br>
ex.: \<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="API_KEY"/\>

#### Configurações Google Console configuração de API:
* link: https://console.developers.google.com/project
* referência: https://developers.google.com/maps/documentation/android/start?hl=pt-br

###### Google Console:
* Criar Projeto 
* Criar API KEY
* Usar app's SHA-1 fingerprint e nome do pacote do projeto na API KEY.<br>
ex.: CE:0A:32:E9:30:A1:BC:AE:E4:80:67:49:0C:1F:FF:F0:BE:E9:BF:3D;br.com.m2m.meuonibus
* Habilitar Google Maps Android API v2 no serviço do Google Console
* Habilitar Google Places API for Android no serviço do Google Console



