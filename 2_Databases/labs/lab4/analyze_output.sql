-- 1                                   

 Hash Join  (cost=341.18..4606.50 rows=6738 width=21) (actual time=3.939..8.122 rows=5920 loops=1)
   Hash Cond: ("Н_ВЕДОМОСТИ"."ЧЛВК_ИД" = "Н_ЛЮДИ"."ИД")
   ->  Bitmap Heap Scan on "Н_ВЕДОМОСТИ"  (cost=130.10..4371.38 rows=9152 width=12) (actual time=0.492..2.538 rows=9313 loops=1)
         Recheck Cond: (("ЧЛВК_ИД" < 163249) AND ("ЧЛВК_ИД" > 153285))
         Heap Blocks: exact=670
         ->  Bitmap Index Scan on "ВЕД_ЧЛВК_FK_IFK"  (cost=0.00..127.81 rows=9152 width=0) (actual time=0.410..0.411 rows=9313 loops=1)
               Index Cond: (("ЧЛВК_ИД" < 163249) AND ("ЧЛВК_ИД" > 153285))
   ->  Hash  (cost=163.97..163.97 rows=3768 width=17) (actual time=3.426..3.427 rows=3777 loops=1)
         Buckets: 4096  Batches: 1  Memory Usage: 221kB
         ->  Seq Scan on "Н_ЛЮДИ"  (cost=0.00..163.97 rows=3768 width=17) (actual time=0.014..2.781 rows=3777 loops=1)
               Filter: (("ФАМИЛИЯ")::text > 'Ёлкин'::text)
               Rows Removed by Filter: 1341
 Planning Time: 0.461 ms
 Execution Time: 8.445 ms
(14 rows)


-- 2                                                                 

 Nested Loop  (cost=0.86..23.70 rows=130 width=21) (actual time=0.098..0.100 rows=0 loops=1)
   ->  Index Only Scan using "ВЕД_ЧЛВК_FK_IFK" on "Н_ВЕДОМОСТИ"  (cost=0.29..9.43 rows=65 width=4) (actual time=0.031..0.042 rows=92 loops=1)
         Index Cond: ("ЧЛВК_ИД" = 142390)
         Heap Fetches: 0
   ->  Materialize  (cost=0.56..12.65 rows=2 width=21) (actual time=0.000..0.000 rows=0 loops=92)
         ->  Nested Loop  (cost=0.56..12.64 rows=2 width=21) (actual time=0.035..0.035 rows=0 loops=1)
               ->  Index Scan using "ЧЛВК_PK" on "Н_ЛЮДИ"  (cost=0.28..8.30 rows=1 width=17) (actual time=0.016..0.016 rows=1 loops=1)
                     Index Cond: ("ИД" = 142390)
                     Filter: (("ФАМИЛИЯ")::text > 'Иванов'::text)
               ->  Index Only Scan using "SYS_C003500_IFK" on "Н_СЕССИЯ"  (cost=0.28..4.31 rows=2 width=4) (actual time=0.017..0.017 rows=0 loops=1)
                     Index Cond: ("ЧЛВК_ИД" = 142390)
                     Heap Fetches: 0
 Planning Time: 0.331 ms
 Execution Time: 0.142 ms
(14 rows)
