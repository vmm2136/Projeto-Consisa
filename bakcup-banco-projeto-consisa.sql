PGDMP  6                    }            Projeto_Consisa    17.5    17.5     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    16388    Projeto_Consisa    DATABASE     �   CREATE DATABASE "Projeto_Consisa" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
 !   DROP DATABASE "Projeto_Consisa";
                     postgres    false            �            1259    16389    tarefas    TABLE     �   CREATE TABLE public.tarefas (
    id uuid NOT NULL,
    data_fim date,
    data_inicio date,
    nome_tarefa character varying(255) NOT NULL,
    status_tarefa character varying(255) NOT NULL,
    tarefa_pai_id uuid,
    usuario_responsavel uuid
);
    DROP TABLE public.tarefas;
       public         heap r       postgres    false            �            1259    16396    usuarios    TABLE     a   CREATE TABLE public.usuarios (
    id uuid NOT NULL,
    nome character varying(255) NOT NULL
);
    DROP TABLE public.usuarios;
       public         heap r       postgres    false            �          0    16389    tarefas 
   TABLE DATA           |   COPY public.tarefas (id, data_fim, data_inicio, nome_tarefa, status_tarefa, tarefa_pai_id, usuario_responsavel) FROM stdin;
    public               postgres    false    217   �       �          0    16396    usuarios 
   TABLE DATA           ,   COPY public.usuarios (id, nome) FROM stdin;
    public               postgres    false    218   �       %           2606    16395    tarefas tarefas_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.tarefas
    ADD CONSTRAINT tarefas_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.tarefas DROP CONSTRAINT tarefas_pkey;
       public                 postgres    false    217            '           2606    16400    usuarios usuarios_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT usuarios_pkey;
       public                 postgres    false    218            (           2606    16401 #   tarefas fk3avxho2nfvjws1rir5gmkqv2t    FK CONSTRAINT     �   ALTER TABLE ONLY public.tarefas
    ADD CONSTRAINT fk3avxho2nfvjws1rir5gmkqv2t FOREIGN KEY (tarefa_pai_id) REFERENCES public.tarefas(id);
 M   ALTER TABLE ONLY public.tarefas DROP CONSTRAINT fk3avxho2nfvjws1rir5gmkqv2t;
       public               postgres    false    217    217    4645            )           2606    16406 #   tarefas fkp2uc37355k64di02sime6anls    FK CONSTRAINT     �   ALTER TABLE ONLY public.tarefas
    ADD CONSTRAINT fkp2uc37355k64di02sime6anls FOREIGN KEY (usuario_responsavel) REFERENCES public.usuarios(id);
 M   ALTER TABLE ONLY public.tarefas DROP CONSTRAINT fkp2uc37355k64di02sime6anls;
       public               postgres    false    218    4647    217            �      x������ � �      �      x������ � �     