import pandas as pd
import dataframe_image as dfi

columns = [chr(65 + i) for i in range(len(pd.read_excel('task.xlsm', sheet_name='Лист1', nrows=1).columns))]

data = pd.read_excel('task.xlsm', sheet_name='Лист1', skiprows=2, nrows=12, usecols='A:Y', names=columns)
data_filtered = data.drop(columns=['F']).fillna('')

styled = data_filtered.style.set_table_styles([
    {'selector': 'table', 'props': 'border: 2px solid blue; border-collapse: collapse;'},
    {'selector': 'th, td', 'props': 'border: none;'},
    {'selector': 'th:nth-child(-n+5), td:nth-child(-n+5)', 'props': 'border-left: 2px solid blue; border-right: 2px solid blue;'},
    {'selector': 'thead', 'props': 'border-bottom: 2px solid blue;'}
])

dfi.export(styled, 'result_table_v0.png')
