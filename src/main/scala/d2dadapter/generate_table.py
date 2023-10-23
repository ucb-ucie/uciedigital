
table_name = "src\main\scala\d2dadapter\CRC16_8005_table.txt"


with open(table_name, "r") as table_file:
    numbers = table_file.read().split()

result = ['VecInit(']
line = 0

for i in range(len(numbers)):
    if i > 0 and i % 8 == 0:
        result.append('      ')
        line += 1
    result[line] += f'{numbers[i]}.U, '
result[line] = result[line][:-2]
result[line] += ')'


result_name = table_name.split('.txt')[0] + "_result.txt"
with open(result_name, "w") as result_file:
    for line in result:
        result_file.write(line + '\n')





