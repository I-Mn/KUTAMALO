import re

# Update view/MainView.fxml
with open('view/MainView.fxml', 'r') as f:
    fxml = f.read()

analytics_svg = 'M6.5 17.5v-3m5 3v-9m5 9v-4 M21.5 5.5a3 3 0 1 1-6 0a3 3 0 0 1 6 0Z M21.496 11s.004.34.004 1c0 4.478 0 6.718-1.391 8.109S16.479 21.5 12 21.5c-4.478 0-6.718 0-8.109-1.391S2.5 16.479 2.5 12c0-4.478 0-6.717 1.391-8.109C5.282 2.5 7.521 2.5 12 2.5h1'
profile_svg = 'M11.5 2.5h1c4.243 0 6.364 0 7.682 1.318S21.5 7.258 21.5 11.5v1c0 4.243 0 6.364-1.318 7.682S16.742 21.5 12.5 21.5h-1c-4.243 0-6.364 0-7.682-1.318S2.5 16.742 2.5 12.5v-1c0-4.243 0-6.364 1.318-7.682S7.258 2.5 11.5 2.5 M12 7.395a3.5 3.5 0 1 1 0 7a3.5 3.5 0 0 1 0-7 M18.982 20.895a7 7 0 0 0-13.965 0'
settings_svg = 'm21.318 7.141l-.494-.856c-.373-.648-.56-.972-.878-1.101c-.317-.13-.676-.027-1.395.176l-1.22.344c-.459.106-.94.046-1.358-.17l-.337-.194a2 2 0 0 1-.788-.967l-.334-.998c-.22-.66-.33-.99-.591-1.178c-.261-.19-.609-.19-1.303-.19h-1.115c-.694 0-1.041 0-1.303.19c-.261.188-.37.518-.59 1.178l-.334.998a2 2 0 0 1-.789.967l-.337.195c-.418.215-.9.275-1.358.17l-1.22-.345c-.719-.203-1.078-.305-1.395-.176c-.318.129-.505.453-.878 1.1l-.493.857c-.35.608-.525.911-.491 1.234c.034.324.268.584.736 1.105l1.031 1.153c.252.319.431.875.431 1.375s-.179 1.056-.43 1.375l-1.032 1.152c-.468.521-.702.782-.736 1.105s.14.627.49 1.234l.494.857c.373.647.56.971.878 1.1s.676.028 1.395-.176l1.22-.344a2 2 0 0 1 1.359.17l.336.194c.36.23.636.57.788.968l.334.997c.22.66.33.99.591 1.18c.262.188.609.188 1.303.188h1.115c.694 0 1.042 0 1.303-.189s.371-.519.59-1.179l.335-.997c.152-.399.428-.738.788-.968l.336-.194c.42-.215.9-.276 1.36-.17l1.22.344c.718.204 1.077.306 1.394.177c.318-.13.505-.454.878-1.101l.493-.857c.35-.607.525-.91.491-1.234s-.268-.584-.736-1.105l-1.031-1.152c-.252-.32-.431-.875-.431-1.375s.179-1.056.43-1.375l1.032-1.153c.468-.52.702-.781.736-1.105s-.14-.626-.49-1.234Z M15.52 12a3.5 3.5 0 1 1-7 0a3.5 3.5 0 0 1 7 0Z'
logout_svg = 'M4.393 4C4 4.617 4 5.413 4 7.004v9.994c0 1.591 0 2.387.393 3.002q.105.165.235.312c.483.546 1.249.765 2.78 1.202c1.533.438 2.3.657 2.856.329a1.5 1.5 0 0 0 .267-.202C11 21.196 11 20.4 11 18.803V5.197c0-1.596 0-2.393-.469-2.837a1.5 1.5 0 0 0-.267-.202c-.555-.328-1.323-.11-2.857.329c-1.53.437-2.296.656-2.78 1.202a2.5 2.5 0 0 0-.234.312M11 4h2.017c1.902 0 2.853 0 3.443.586c.33.326.476.764.54 1.414m-6 14h2.017c1.902 0 2.853 0 3.443-.586c.33-.326.476-.764.54-1.414m4-6h-7m5.5-2.5S22 11.34 22 12s-2.5 2.5-2.5 2.5'

# Replace SVG paths and style for Analytics
fxml = re.sub(r'<HBox fx:id="navAnalytics".*?>\s*<SVGPath content=".*?" styleClass="nav-icon" />',
              f'<HBox fx:id="navAnalytics" onMouseClicked="#navToAnalytics" styleClass="nav-item" alignment="CENTER_LEFT" spacing="15">\n                    <SVGPath content="{analytics_svg}" \n         styleClass="nav-icon" \n         style="-fx-fill: transparent; -fx-stroke: #cccccc; -fx-stroke-width: 1.5; -fx-stroke-line-cap: round; -fx-stroke-line-join: round;" />', fxml, flags=re.DOTALL)

# Replace Profile
fxml = re.sub(r'<HBox fx:id="navProfile".*?>\s*<SVGPath content=".*?" styleClass="nav-icon" />',
              f'<HBox fx:id="navProfile" onMouseClicked="#navToProfile" styleClass="nav-item" alignment="CENTER_LEFT" spacing="15">\n                    <SVGPath content="{profile_svg}" \n         styleClass="nav-icon" \n         style="-fx-fill: transparent; -fx-stroke: #cccccc; -fx-stroke-width: 1.5; -fx-stroke-line-cap: round; -fx-stroke-line-join: round;" />', fxml, flags=re.DOTALL)

# Replace Settings
fxml = re.sub(r'<HBox fx:id="navSettings".*?>\s*<SVGPath content=".*?" styleClass="nav-icon" />',
              f'<HBox fx:id="navSettings" onMouseClicked="#navToSettings" styleClass="nav-item" alignment="CENTER_LEFT" spacing="15">\n                    <SVGPath content="{settings_svg}" \n         styleClass="nav-icon" \n         style="-fx-fill: transparent; -fx-stroke: #cccccc; -fx-stroke-width: 1.5; -fx-stroke-line-cap: round; -fx-stroke-line-join: round;" />', fxml, flags=re.DOTALL)

# Replace Logout
fxml = re.sub(r'<HBox fx:id="navLogout".*?>\s*<SVGPath content=".*?" styleClass="nav-icon" />',
              f'<HBox fx:id="navLogout" onMouseClicked="#logout" styleClass="nav-item" alignment="CENTER_LEFT" spacing="15">\n                    <SVGPath content="{logout_svg}" \n         styleClass="nav-icon" \n         style="-fx-fill: transparent; -fx-stroke: #FF416C; -fx-stroke-width: 1.5; -fx-stroke-line-cap: round; -fx-stroke-line-join: round;" />', fxml, flags=re.DOTALL)

with open('view/MainView.fxml', 'w') as f:
    f.write(fxml)


# Update controller/MainController.java
with open('controller/MainController.java', 'r') as f:
    java = f.read()

food_svg = 'M17 13.23s-.91-.46-1.818-.46c-1.364 0-3.182 1.845-3.182 4.615C12 20.154 14.49 22 17 22s5-1.846 5-4.615s-1.818-4.616-3.182-4.616c-.909 0-1.818.462-1.818.462m0 0c0-1.384.91-3.23 2.727-3.23M10.655 5c.896 0 1.623-.672 1.623-1.5S11.55 2 10.655 2h-5.41c-.896 0-1.622.672-1.622 1.5S4.349 5 5.246 5m5.923-.077c.956 1.766 1.74 3.36 2.22 5.077q.059.21.111.423M10.428 22h-4.1C2.747 22 2 21.31 2 18v-4.223c0-3.4 1.098-5.891 2.705-8.862'
shop_svg = 'M10.5 20.25a.75.75 0 1 1-1.5 0a.75.75 0 0 1 1.5 0m8.5 0a.75.75 0 1 1-1.5 0a.75.75 0 0 1 1.5 0M2 3h.207c1.324 0 1.987 0 2.419.402s.479 1.063.573 2.384l.251 3.519c.142 1.989.213 2.983.515 3.791a6 6 0 0 0 3.931 3.661c.828.243 1.83.243 3.836.243c2.105 0 3.158 0 4.01-.258a6 6 0 0 0 4-4C22 11.89 22 10.843 22 8.75c0-.698 0-1.047-.086-1.33a2 2 0 0 0-1.333-1.334C20.297 6 19.948 6 19.25 6H5.5M16 10v3m-5-3v3'
trans_svg = 'M22 15.422V18.5c0 .466 0 .699-.076.883a1 1 0 0 1-.541.54c-.184.077-.417.077-.883.077s-.699 0-.883-.076a1 1 0 0 1-.54-.541C19 19.199 19 18.966 19 18.5s0-.699-.076-.883a1 1 0 0 0-.541-.54C18.199 17 17.966 17 17.5 17h-11c-.466 0-.699 0-.883.076a1 1 0 0 0-.54.541C5 17.801 5 18.034 5 18.5s0 .699-.076.883a1 1 0 0 1-.541.54C4.199 20 3.966 20 3.5 20s-.699 0-.883-.076a1 1 0 0 1-.54-.541C2 19.199 2 18.966 2 18.5v-3.078c0-1.202 0-1.803.172-2.37s.505-1.067 1.172-2.067L4 10l.962-2.308c.745-1.79 1.118-2.684 1.874-3.188S8.56 4 10.5 4h3c1.939 0 2.908 0 3.664.504s1.129 1.399 1.874 3.188L20 10l.656.985c.667 1 1 1.5 1.172 2.067S22 14.22 22 15.422 M6.125 14H6m.25 0a.25.25 0 1 1-.5 0a.25.25 0 0 1 .5 0m11.875 0H18m.25 0a.25.25 0 1 1-.5 0a.25.25 0 0 1 .5 0M2 8.5L4 10l2 .5h12l2-.5l2-1.5'
subs_svg = 'M12 14a3 3 0 0 1-2.836-2.018C8.984 11.46 8.552 11 8 11c-2.357 0-3.536 0-4.268.732S3 13.643 3 16s0 3.535.732 4.268S5.643 21 8 21h8c2.357 0 3.535 0 4.268-.732C21 19.535 21 18.357 21 16s0-3.536-.732-4.268C19.535 11 18.357 11 16 11c-.552 0-.984.46-1.164.982A3 3 0 0 1 12 14 M21 17v-5c0-2.357 0-3.536-.732-4.268C19.535 7 18.357 7 16 7H8c-2.357 0-3.536 0-4.268.732S3 9.643 3 12v5 M21 13V8c0-2.357 0-3.536-.732-4.268C19.535 3 18.357 3 16 3H8c-2.357 0-3.536 0-4.268.732S3 5.643 3 8v5'
house_svg = 'm1.5 10.002l5.5-6m0 0l4.311 4.703c.586.639.879.958 1.264 1.128c.384.169.818.169 1.685.169h8.24l-4.311-4.703c-.586-.639-.879-.958-1.264-1.128c-.384-.17-.818-.17-1.685-.17zM11 8.5V20H7c-1.886 0-2.828 0-3.414-.586S3 17.885 3 16V8.5 M11 20h6c1.886 0 2.828 0 3.414-.586S21 17.885 21 16v-6M4 7V4m3.125 7.25H7m.25 0a.25.25 0 1 1-.5 0a.25.25 0 0 1 .5 0M7 20v-4m8-2h2'
other_svg = 'M20.943 16.835a15.76 15.76 0 0 0-4.476-8.616c-.517-.503-.775-.754-1.346-.986C14.55 7 14.059 7 13.078 7h-2.156c-.981 0-1.472 0-2.043.233c-.57.232-.83.483-1.346.986a15.76 15.76 0 0 0-4.476 8.616C2.57 19.773 5.28 22 8.308 22h7.384c3.029 0 5.74-2.227 5.25-5.165 M7.257 4.443c-.207-.3-.506-.708.112-.8c.635-.096 1.294.338 1.94.33c.583-.009.88-.268 1.2-.638C10.845 2.946 11.365 2 12 2s1.155.946 1.491 1.335c.32.37.617.63 1.2.637c.646.01 1.305-.425 1.94-.33c.618.093.319.5.112.8l-.932 1.359c-.4.58-.599.87-1.017 1.035S13.837 7 12.758 7h-1.516c-1.08 0-1.619 0-2.036-.164S8.589 6.38 8.189 5.8z M13.627 12.919c-.216-.799-1.317-1.519-2.638-.98s-1.53 2.272.467 2.457c.904.083 1.492-.097 2.031.412c.54.508.64 1.923-.739 2.304c-1.377.381-2.742-.214-2.89-1.06m1.984-5.06v.761m0 5.476v.764'

java = re.sub(r'kat\.contains\("makan"\).*?return ".*?";', f'kat.contains("makan") || kat.contains("food") || kat.contains("drinks")) {{\n            return "{food_svg}";', java, flags=re.DOTALL)
java = re.sub(r'kat\.contains\("belanja"\).*?return ".*?";', f'kat.contains("belanja") || kat.contains("shop")) {{\n            return "{shop_svg}";', java, flags=re.DOTALL)
java = re.sub(r'kat\.contains\("transport"\).*?return ".*?";', f'kat.contains("transport") || kat.contains("mobil") || kat.contains("car")) {{\n            return "{trans_svg}";', java, flags=re.DOTALL)
java = re.sub(r'kat\.contains\("subs"\).*?return ".*?";', f'kat.contains("subs")) {{\n            return "{subs_svg}";', java, flags=re.DOTALL)
java = re.sub(r'kat\.contains\("house"\).*?return ".*?";', f'kat.contains("house") || kat.contains("housing")) {{\n            return "{house_svg}";', java, flags=re.DOTALL)
java = re.sub(r'else \{\s*return ".*?";', f'else {{\n            return "{other_svg}";', java, flags=re.DOTALL)

# Since category icons are now stroked, we need to update MainController.java where they are rendered.
# In initialize() for filter combobox: iconPath.setStyle("-fx-fill: transparent; -fx-stroke: white; -fx-stroke-width: 1.5; -fx-stroke-line-cap: round; -fx-stroke-line-join: round;"); instead of iconPath.setFill(Color.web("#888888"));
java = re.sub(r'iconPath\.setFill\(Color\.web\("#888888"\)\);', 'iconPath.setStyle("-fx-fill: transparent; -fx-stroke: #888888; -fx-stroke-width: 1.5; -fx-stroke-line-cap: round; -fx-stroke-line-join: round;");', java)

# In renderListTransaksi() for list items: iconPath.getStyleClass().add("item-icon-svg");
# We need to add the inline style for stroke since item-icon-svg might use fill
java = java.replace('iconPath.getStyleClass().add("item-icon-svg");', 'iconPath.getStyleClass().add("item-icon-svg");\n            iconPath.setStyle("-fx-fill: transparent; -fx-stroke: white; -fx-stroke-width: 1.5; -fx-stroke-line-cap: round; -fx-stroke-line-join: round;");')

with open('controller/MainController.java', 'w') as f:
    f.write(java)
