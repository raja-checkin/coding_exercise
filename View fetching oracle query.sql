--date column is in MM/DD/YYYY format, so converted the sysdate.
select v.* from  Users u inner join Views v 
on u.user_id=v.userId and u.country='US'
where v.views > 2 and v.date  between TO_DATE(SYSDATE-10, 'MM/DD/YYYY') and TO_DATE(SYSDATE, 'MM/DD/YYYY')
order by v.date desc;