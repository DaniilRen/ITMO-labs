#!/bin/bash
cd lab0
# shopt -s globstar

# done: 1, 2, 3, 4, 5,

# # 4.1
# echo -e "\ntask 4.1 ->\n"
# wc -l $(ls -Rp ./*/* 2>/dev/null | grep -v /$ | grep o$) 1>/tmp/task_4_1.log

# # 4.2
# echo -e "\ntask 4.2 ->\n"
# ls -Rp ./*/* 2>&1 | grep -v /$ | grep du | tail -4

# # 4.3
# echo -e "\ntask 4.3 ->\n"
# x=$(ls -Rp ./*/* | grep -v /$ | grep u$)
# [ ! -z "${x}" ] && cat ${x} | sort

# # 4.4
# echo -e "\ntask 4.4 ->\n"
# ls -lRSr . 2>/tmp/task_4_4.log | grep ca

# # 4.5
# echo -e "\ntask 4.5 ->\n"
# (ls -ltr $(ls -Rp . | grep -v "/$") | head -3) 2>/tmp/task_4_5.log

# # 4.6
# echo -e "\ntask 4.6 ->\n"
# wc -m $(ls -Rp ./*/*/*/* | grep -v /$ | grep -v :$ | grep "/p") | sort -nk1
# grep *p */*p */*/*p | wc -m | sort -nk1,1
# ls -1dp ./*/*/*/p* | grep -v /$  | grep -v /$ | wc -m | sort -nk1






# OLD varinants of task 4 with (lovely) globstar
# Task 4
# echo -e "--> Starting task 4\n..."

# # 4.1
# echo -e "\ntask 4.1 ->\n"
# echo -e "* Output redirected to /tmp/task_4_1.log, Errors supressed\n"
# { wc -l $(ls -dp **/*o | grep -v "/$"); } 1>/tmp/task_4_1.log 2>/dev/null

# # 4.2
# echo -e "\ntask 4.2 ->\n"
# echo -e "* Errors redirected to stdout\n"
# { ls -ltr $(grep -rl "du" .) | tail -4; } 2>&1

# # # 4.3
# echo -e "\ntask 4.3 ->\n"
# [ ! -z "$(ls -dp **/*u | grep -v "/$")" ] && cat $(ls -dp **/*u | grep -v "/$") | sort
# ls -dp **/*u | grep -v "/$"
ls -p | grep -v "/$" | grep ""
cat -n ./* | grep -v "*u$" | sort

# # 4.4
# echo -e "\ntask 4.4 ->\n"
# echo -e "* Errors redirected to /tmp/task_4_4.log\n"
# { [ ! -z "$(grep -rl "a" .)" ] && ls -lSr $(grep -rl "a"); } 2>/tmp/task_4_4.log

# # 4.5
# echo -e "\ntask 4.5 ->\n"
# echo -e "* Errors redirected to /tmp/task_4_5.log\n"
# { ls -ltr $(ls -dp **/* | grep -v "/$") | head -3;} 2>/tmp/task_4_5.log

# # 4.6
# echo -e "\ntask 4.6 ->\n"
# wc -m $(ls -dp **/* | grep -v "/$" | grep "/p") | sort -nk1

# echo -e "--> Task 4 completed!\n\n"
