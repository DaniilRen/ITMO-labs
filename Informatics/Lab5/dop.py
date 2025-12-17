import pandas as pd
import dataframe_image as dfi

columns = [chr(65 + i) for i in range(25)]

data = pd.read_excel(
    'task.xlsm',
    sheet_name='Лист1',
    usecols='A:Y',
    nrows=15,
    keep_default_na=False,
    names=columns,
    header=None
)

data = data.drop(columns=['F'])

numeric_cols = data.columns
data[numeric_cols] = data[numeric_cols].apply(pd.to_numeric, errors="ignore")

def set_blue_borders(x):
    styles = pd.DataFrame('', index=x.index, columns=x.columns)

    if 3 in x.index:
        styles.loc[3, :] = 'border-top: 2px solid blue !important'
    
    if 14 in x.index:
        styles.loc[14, :] = 'border-bottom: 2px solid blue !important'

    side_cols_left_right = ['A', 'B', 'C', 'Y']
    for col in side_cols_left_right:
        if col in x.columns:
            mask = ~x.index.isin([0, 1, 2])
            for idx in x.index[mask]:
                current_style = styles.loc[idx, col]
                if current_style:
                    styles.loc[idx, col] = f"{current_style}; border-left: 2px solid blue !important; border-right: 2px solid blue !important"
                else:
                    styles.loc[idx, col] = 'border-left: 2px solid blue !important; border-right: 2px solid blue !important'

    if 'E' in x.columns:
        mask = ~x.index.isin([0, 1, 2])
        for idx in x.index[mask]:
            current_style = styles.loc[idx, 'E']
            if current_style:
                styles.loc[idx, 'E'] = f"{current_style}; border-right: 2px solid blue !important"
            else:
                styles.loc[idx, 'E'] = 'border-right: 2px solid blue !important'

    return styles

data = (
    data.style
        .set_table_styles([
            {"selector": "tr, td", "props": [("border", "1px solid gray")]}
        ])
        .apply(set_blue_borders, axis=None)
        .format(na_rep='', precision=0)
)

dfi.export(data, 'result_table.png')
