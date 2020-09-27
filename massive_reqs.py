import requests
import pandas as pd
from time import sleep


URL_PKM = 'https://pokeapi.co/api/v2/pokemon/'
URL_SPC = 'https://pokeapi.co/api/v2/pokemon-species/'
MAX = 893
i = 1
status = 200

pkmns = []

def percent(be: int, of: int):
    return round((be * 100) / of)

while i <= MAX:
    req = requests.get(URL_PKM + str(i))
    req_spc = requests.get(URL_SPC + str(i))
    payload = req.json()
    payload_spc = req_spc.json()

    name = payload['species']['name']
    
    types = payload['types']

    type_1 = types[0]['type']['name']
    type_2 = types[1]['type']['name'] if len(types) > 1 else ''

    hp = 0
    attack = 0
    defense = 0
    spatt = 0
    spdef = 0
    spd = 0

    for status in payload['stats']:
        if status['stat']['name'] == 'hp':
            hp = status['base_stat']    
        elif status['stat']['name'] == 'attack':
            attack = status['base_stat']
        elif status['stat']['name'] == 'defense':
            defense = status['base_stat']
        elif status['stat']['name'] == 'special-attack':
            spatt = status['base_stat']
        elif status['stat']['name'] == 'special-defense':
            spdef = status['base_stat']
        elif status['stat']['name'] == 'speed':
            spd = status['base_stat']
    i += 1

    leg = payload_spc['is_legendary']

    total = hp + attack + defense + spatt + spdef + spd

    gen = payload_spc['generation']['url']
    gen = gen.split('/')[-2]

    sleep(1)

    pkmns.append({
        "Name": name,
        "Type 1": type_1,
        "Type 2": type_2,
        "Total": total,
        "HP": hp,
        "Attack": attack,
        "Defense": defense,
        "Sp. Atk": spatt,
        "Sp. Def": spdef,
        "Speed": spd,
        "Generation": gen,
        "Legendary": leg
    })

    print(f'{percent(i, MAX)}%', end='\r', flush=True)

df = pd.DataFrame(pkmns)

df.to_csv('pkmn.csv', index=False)