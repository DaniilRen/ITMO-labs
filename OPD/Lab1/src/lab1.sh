#!/bin/bash

mkdir lab0
cd lab0


# Task 1
echo -e "--> Starting task 1\n..."

mkdir -p burmy5/stoutland
mkdir -p burmy5/pikachu
touch burmy5/psyduck
mkdir -p burmy5/jigglypuff
mkdir -p burmy5/dustox
touch chikorita0
mkdir geodude1
mkdir -p geodude1/ledyba
mkdir -p geodude1/scraggy
mkdir -p geodude1/electabuzz
touch geodude1/nidorino
touch geodude1/magnemite
mkdir -p geodude1/mienshao
touch lucario9
mkdir -p magmar9/chinchou
touch magmar9/paras
touch magmar9/carvanha
mkdir -p magmar9/typhlosion
touch tyranitar3

echo -e 'Ходы Aqua Tail Body Slam Brine Counter Dive Double-Edge\nDynamicpunch Focus Punch Icy Wind Iron Tail Low Kick Magic\nCoat Mega Kick Mega Punch Mud-Slap Role Play Seismic Toss Signal Beam\nSleep Talk Snore Swift Wonder Room Worry Seed Zen\Headbutt' > burmy5/psyduck
echo -e 'satk=5 sdef=7 spd=5' > chikorita0
echo -e 'Развитые\nспособности Hustle' > geodude1/nidorino
echo -e 'Способности Tackle Supersonic\nThundershock Sonicboom Thunder Wave Magnet Bombs Spark Mirror Shot\nMetal Sound Electro Ball Flash Cannon Screech Discharge Lock-on Magnet\nRise Gyro Ball Zap Cannon'> geodude1/magnemite
echo -e 'satk=12 sdef=7 spd=9' > lucario9
echo -e 'Тип\nПокемона BUG GRASS' > magmar9/paras
echo -e 'satk=7 sdef=2\nspd=7' > magmar9/carvanha
echo -e 'weight=445.3 height=79.0 atk=13 def=11' > tyranitar3

echo -e "--> Task 1 completed!\n\n"


# Task 2
echo -e "--> Starting task 2\n..."

chmod 512 burmy5
chmod 361 burmy5/stoutland
chmod u=rwx burmy5/pikachu
chmod g=wx burmy5/pikachu
chmod o=r burmy5/pikachu
chmod ugo=r burmy5/psyduck
chmod 700 burmy5/jigglypuff
chmod 577 burmy5/dustox
chmod 042 chikorita0
chmod u=rwx geodude1
chmod g=rx geodude1
chmod o=w geodude1
chmod 511 geodude1/ledyba
chmod 570 geodude1/scraggy
chmod u=rwx geodude1/electabuzz
chmod g=rx geodude1/electabuzz
chmod o=w geodude1/electabuzz
chmod u=rw geodude1/nidorino
chmod g=w geodude1/nidorino
chmod o=w geodude1/nidorino
chmod u=rw geodude1/magnemite
chmod g= geodude1/magnemite
chmod o= geodude1/magnemite
chmod 315 geodude1/mienshao
chmod u= lucario9
chmod g=r lucario9
chmod o=rw lucario9
chmod u=wx magmar9
chmod g=rwx magmar9
chmod o=rx magmar9
chmod 771 magmar9/chinchou
chmod 006 magmar9/paras
chmod 624 magmar9/carvanha
chmod 551 magmar9/typhlosion
chmod u=rw tyranitar3
chmod g=w tyranitar3
chmod o= tyranitar3

echo -e "--> Task 2 completed!\n\n"


# Task 3
echo -e "--> Starting task 3\n..."

# 3.1
chmod u+w magmar9/typhlosion
cp tyranitar3 magmar9/typhlosion
chmod u-w magmar9/typhlosion

# 3.2
chmod u+w burmy5
ln -s burmy5 Copy_2
ln chikorita0 burmy5
chmod u-w burmy5

# 3.3
chmod u+w burmy5
ln chikorita0 burmy5/psyduckchikorita
chmod u-w burmy5

# 3.4
chmod u+r burmy5/stoutland
chmod u+r burmy5/psyduckchikorita
chmod u+r burmy5/chikorita0
chmod u+w geodude1/scraggy
cp -r burmy5 geodude1/scraggy
chmod u-w geodude1/scraggy
chmod u-r burmy5/stoutland
chmod u-r burmy5/psyduckchikorita
chmod u-r burmy5/chikorita0

# 3.5
cat magmar9/carvanha geodude1/nidorino > chikorita0_17

# 3.6
chmod u+w burmy5
chmod u+w burmy5/psyduckchikorita
# Так как ссылка с таким именем была создана на предшествующем шаге, удалим файл
rm burmy5/psyduckchikorita
ln -s chikorita0 burmy5/psyduckchikorita
chmod u-w burmy5
chmod u-w burmy5/psyduckchikorita

# 3.7
chmod u+r lucario9
cp lucario9 magmar9/paraslucario
chmod u-r lucario9

echo -e "--> Task 3 completed!\n\n"

echo -e "* Tree copied:\n"
ls -lR . 2>/dev/null


# Task 4
echo -e "--> Starting task 4\n..."

# 4.1
echo -e "\ntask 4.1 ->\n"
echo -e "* Output will be redirected to /tmp/task_4_1.log, Errors will be supressed\n"
ls -1dp ./*/*/*/p* 2>/dev/null | grep -v /$  | grep -v o$ | wc -l 1>/tmp/task_4_1.log

# 4.2
echo -e "\ntask 4.2 ->\n"
echo -e "* Errors will be redirected to stdout\n"
ls -Rp ./*/* 2>&1 | grep -v /$ | grep du | tail -4

# 4.3
echo -e "\ntask 4.3 ->\n"
cat ./*u | sort

# 4.4
echo -e "\ntask 4.4 ->\n"
echo -e "* Errors will be redirected to /tmp/task_4_4.log\n"
ls -lRSr . 2>/tmp/task_4_4.log | grep ca

# 4.5
echo -e "\ntask 4.5 ->\n"
echo -e "* Errors will be redirected to /tmp/task_4_5.log\n"
ls -trRp . | grep -v "/$" 2>/tmp/task_4_5.log | head -3

# 4.6
echo -e "\ntask 4.6 ->\n"
ls -1dp ./*/*/*/p* | grep -v /$ | wc -m | sort -nk1

echo -e "--> Task 4 completed!\n\n"


# Task 5
echo -e "--> Starting task 5\n..."

chmod -R u+rw burmy5
rm -f tyranitar3
rm -f magmar9/carvanha
rm -f Copy*
rm -f burmy5/psyduckchikori*
rm -rf burmy5
rmdir geodude1/mienshao


echo -e "--> Task 5 completed!"
