begin
  mydate := to_date('2015-01-01'); -- Noncompliant {{Specify the date format in this TO_DATE.}}
--          ^^^^^^^^^^^^^^^^^^^^^
  
  mydate := to_date('2015-01-01', 'rrrr-mm-dd');
  mydate := my_to_date('2015-01-01');
  mydate := pkg.to_date('2015-01-01');
end;