with open('input.txt') as src:
    rows = [' & '.join(r.split()) + r' \\' for r in src.readlines()]
    with open('output.txt', 'w') as out:
        print(rows)
        for r in rows:
            out.write(r + "\n")