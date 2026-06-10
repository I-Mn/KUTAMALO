import re
with open('view/MainView.fxml', 'r', encoding='utf-8') as f: text = f.read()
ids = set(re.findall(r'fx:id="(.*?)"', text))
actions = set(re.findall(r'onAction="#(.*?)"', text))
key_actions = set(re.findall(r'onKeyReleased="#(.*?)"', text))
mouse_actions = set(re.findall(r'onMouseClicked="#(.*?)"', text))

with open('controller/MainController.java', 'r', encoding='utf-8') as f: java = f.read()

print('Missing IDs in Java:')
for id in ids:
    if id not in java:
        print('  - ' + id)

print('\nMissing Actions in Java:')
for action in actions.union(key_actions).union(mouse_actions):
    if ('void ' + action + '(') not in java:
        print('  - ' + action)
