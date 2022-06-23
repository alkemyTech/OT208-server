
insert into organizations (id, name , image, email , welcome_text , about_us_text , phone , timestamps , soft_delete ) values
('7d254950-e45a-11ec-8fea-0242ac120002','SomosMas', 'https://cohorte-mayo-2820e45d.s3.amazonaws.com/1654056131209.jpg' , 'somosmasong@mail.com',
'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tincidunt a dolor ac scelerisque. Sed posuere, velit nec venenatis porta, purus est consequat arcu, sagittis lacinia nulla metus non lorem. Nulla tincidunt leo in nisl lacinia bibendum. Nulla non sollicitudin velit, nec hendrerit nisi. Integer ac odio ac ligula semper bibendum. Maecenas eget congue urna, tincidunt lacinia nisl. Sed dui ex, viverra eu tortor congue, condimentum consectetur nisl. Aenean at aliquam orci. ',
'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tincidunt a dolor ac scelerisque. ',
'01123456789', '2022-06-04T22:31:36+00:00' , 0);

insert into slides ( id, image_url, orders, text, organizations) values
('0b754b8b-bbb3-4359-8ec7-966fc90f55e2','https://cohorte-mayo-2820e45d.s3.amazonaws.com/633b4caa7f924a8592bcedfd3e1501ab.jpg', 1, 'Lorem ipsum dolor sit amet', '7d254950-e45a-11ec-8fea-0242ac120002');

insert into slides ( id, image_url, orders, text, organizations) values
('dec931ce-8dbb-43f4-8761-824f340dd394','https://cohorte-mayo-2820e45d.s3.amazonaws.com/3bd8e30155b147929e23818cdc732ff2.jpeg', 2, 'Una prueba de Slide', '7d254950-e45a-11ec-8fea-0242ac120002');

insert into slides ( id, image_url, orders, text, organizations) values
('602685aa-cf03-4f2e-9350-811208cd92b2','https://cohorte-mayo-2820e45d.s3.amazonaws.com/a50968ffede44579a0d6cbe9a6fe1e35.jpeg', 4, 'Otra Prueba de Slide', '7d254950-e45a-11ec-8fea-0242ac120002');