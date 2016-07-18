CREATE OR REPLACE FUNCTION sp_nom_calculo_nomina_horas_extras(
    _tipo smallint,
    _id_empleado character,
    _fecha_desde timestamp without time zone,
    _fecha_hasta timestamp without time zone,
    _hepf bit,
    _maxhe smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_TOTAL_HE numeric(6,2); _TOTAL_HF numeric(6,2); _TOTAL_HT numeric(6,2); _TOTAL_HD numeric(6,2); _DiasHorasExtras smallint;

	_daynum smallint; _semini smallint; _semfin smallint; _dayini smallint; _dayfin smallint; _contsem smallint; 
	_fechasemini timestamp; _fechasemfin timestamp; _fechasemaux timestamp; _movhea smallint; 
	_LUN_Tot numeric(4,2); _MAR_Tot numeric(4,2); _MIE_Tot numeric(4,2); _JUE_Tot numeric(4,2); _VIE_Tot numeric(4,2); _SAB_Tot numeric(4,2); _DOM_Tot numeric(4,2); 
	_LUN_HE numeric(4,2); _MAR_HE numeric(4,2); _MIE_HE numeric(4,2); _JUE_HE numeric(4,2); _VIE_HE numeric(4,2); _SAB_HE numeric(4,2); _DOM_HE numeric(4,2); 
	_LUN_HT numeric(4,2); _MAR_HT numeric(4,2); _MIE_HT numeric(4,2); _JUE_HT numeric(4,2); _VIE_HT numeric(4,2); _SAB_HT numeric(4,2); _DOM_HT numeric(4,2);
	_LUN_Fecha timestamp; _MAR_Fecha timestamp; _MIE_Fecha timestamp; _JUE_Fecha timestamp; _VIE_Fecha timestamp; _SAB_Fecha timestamp; _DOM_Fecha timestamp;
	_LUN_M3 smallint; _MAR_M3 smallint; _MIE_M3 smallint; _JUE_M3 smallint; _VIE_M3 smallint; _SAB_M3 smallint;
BEGIN
	_movhea := -14; --(select ID_Movimiento from TBL_MOVIMIENTOS where ID_Movimiento = -14) -- si es null debera rechazar la nomina

	_TOTAL_HE := 0.00;
	_TOTAL_HF := 0.00;
	_TOTAL_HD := 0.00;
	_TOTAL_HT := 0.00;
	_DiasHorasExtras := 0;

	IF _Tipo = 2 -- REVISA LOS DE LA NOMINA DE CONFIANZA
	THEN
		IF _MaxHE > 0 -- Si se especifica la variable de Maximo de horas extras acumuladas, significa que va a hacer el proceso simple del calculo de horas extras 
		THEN
			_TOTAL_HE := case when (	select sum(Num_de_Horas) 
																	from TBL_NOM_PERMISOS 
																	where ID_Empleado = _ID_Empleado and 
																				ID_Movimiento = _movhea and 
																				ID_FechaMovimiento between  _Fecha_Desde and _Fecha_Hasta ) is null
													then 0
													else	(	select sum(Num_de_Horas) 
																	from TBL_NOM_PERMISOS 
																	where ID_Empleado = _ID_Empleado and 
																				ID_Movimiento = _movhea and 
																				ID_FechaMovimiento between  _Fecha_Desde and _Fecha_Hasta )
								
													end;
			
		ELSE
			--set DATEFIRST 1
			_semini := date_part('week',_Fecha_Desde);
			_semfin := date_part('week',_Fecha_Hasta);
			
			if _semini > _semfin -- ser? mayor la semana inicial cuando es del a?o pasado y la final del a?o siguiente Ejem 29-12-2004 a 04-01-2005
			then
				_semini := ( _semfin - 1 );
			end if;
			
			_dayini := date_part('dow',_Fecha_Desde); if _dayini = 0 then _dayini := 7; end if;
			_dayfin := date_part('dow',_Fecha_Hasta); if _dayfin = 0 then _dayfin := 7; end if;

			_contsem := _semini;
		
			--primero llena toda la estructura de las horas del periodo de nomina
			while(_contsem <= _semfin) -- controla el numero de semanas en la nomina de la semana 3 a la 4 por ejemplo
			loop
				if _contsem = _semini
				then
					_daynum := _dayini;
					_fechasemini := ( _Fecha_Desde - (cast((_daynum -1) as text) || ' days')::interval); --dateadd(day, -(_daynum -1), _Fecha_Desde)
					_fechasemfin := ( _Fecha_Desde + (cast((7 - _daynum) as text) || ' days')::interval); --dateadd(day, (7 - _daynum), _Fecha_Desde)
				elsif _contsem = _semfin
				then
					_daynum := _dayfin;
					_fechasemini := ( _Fecha_Hasta - (cast((_daynum -1) as text) || ' days')::interval); --dateadd(day, -(_daynum -1), _Fecha_Hasta)
					_fechasemfin := ( _Fecha_Hasta + (cast((7 - _daynum) as text) || ' days')::interval); --dateadd(day, (7 - _daynum), _Fecha_Hasta)
				else
					_daynum := _dayini;
					_fechasemaux := ( _Fecha_Desde + (cast(((_contsem - _semini)*7) as text) || ' days')::interval); --dateadd(day, ((_contsem - _semini)*7), _Fecha_Desde);
					_fechasemini := ( _fechasemaux - (cast((_daynum -1) as text) || ' days')::interval); --dateadd(day, -(_daynum -1), fechasemaux );
					_fechasemfin := ( _fechasemaux + (cast((7 - _daynum) as text) || ' days')::interval); --dateadd(day, (7 - _daynum), fechasemaux);
				end if;
		
				_JUE_M3 := 0;
				_VIE_M3 := 0;
				_SAB_M3 := 0;
			
				_LUN_Fecha := _fechasemini;
				_LUN_Tot := ( 	select sum(Num_de_Horas) 
													from TBL_NOM_PERMISOS 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _LUN_Fecha );
				if _LUN_Tot is not null
				then
					_JUE_M3 := _JUE_M3 + 1;
				end if;
			
				_MAR_Fecha := _fechasemini + '1 day'::interval; --dateadd(day,1,_fechasemini)
				_MAR_Tot := ( 	select sum(Num_de_Horas) 
													from TBL_NOM_PERMISOS 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _MAR_Fecha );
				if _MAR_Tot is not null
				then
					_JUE_M3 := _JUE_M3 + 1;
				end if;
			
				_MIE_Fecha := _fechasemini + '2 days'::interval; --dateadd(day,2,_fechasemini)
				_MIE_Tot := ( 	select sum(Num_de_Horas) 
													from TBL_NOM_PERMISOS 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _MIE_Fecha );
				if _MIE_Tot is not null
				then
					_JUE_M3 = _JUE_M3 + 1;
				end if;
				
				_JUE_Fecha := _fechasemini + '3 days'::interval; --dateadd(day,3,_fechasemini) 
				_JUE_Tot := ( 	select sum(Num_de_Horas) 
													from TBL_NOM_PERMISOS 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _JUE_Fecha );
				if _JUE_Tot is not null
				then
					_VIE_M3 := _JUE_M3 + 1;
				else
					_VIE_M3 := _JUE_M3;
				end if;
			
				_VIE_Fecha := _fechasemini + '4 days'::interval; --dateadd(day,4,_fechasemini) 
				_VIE_Tot = ( 	select sum(Num_de_Horas) 
													from TBL_NOM_PERMISOS 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _VIE_Fecha );
				if _VIE_Tot is not null
				then
					_SAB_M3 := _VIE_M3 + 1;
				else
					_SAB_M3 := _VIE_M3;
				end if;
				
				_SAB_Fecha := _fechasemini + '5 days'::interval; --dateadd(day,5,_fechasemini) 
				_SAB_Tot := ( 	select sum(Num_de_Horas) 
													from TBL_NOM_PERMISOS 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _SAB_Fecha );
			
				_DOM_Fecha := _fechasemfin; 
				_DOM_Tot := ( 	select sum(Num_de_Horas) 
													from TBL_NOM_PERMISOS 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _DOM_Fecha );
				
				_LUN_HE := case when _LUN_Tot is null then 0 else case when _LUN_Tot > 3 then 3 else _LUN_Tot end end;
				_LUN_HT := case when _LUN_Tot is null then 0 else case when _LUN_Tot > 3 then _LUN_Tot - 3 else 0 end end;
				if _LUN_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if; 
				_MAR_HE := case when _MAR_Tot is null then 0 else case when _MAR_Tot > 3 then 3 else _MAR_Tot end end;
				_MAR_HT := case when _MAR_Tot is null then 0 else case when _MAR_Tot > 3 then _MAR_Tot - 3 else 0 end end;
				if _MAR_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if;
				_MIE_HE := case when _MIE_Tot is null then 0 else case when _MIE_Tot > 3 then 3 else _MIE_Tot end end;
				_MIE_HT := case when _MIE_Tot is null then 0 else case when _MIE_Tot > 3 then _MIE_Tot - 3 else 0 end end;
				if _MIE_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if;
				_JUE_HE := case when _JUE_Tot is null then 0 else 
												case 	when _JUE_M3 >= 3 then 0  
															when _JUE_Tot > 3 then 3 else _JUE_Tot end end;
				_JUE_HT := case when _JUE_Tot is null then 0 else 
												case 	when _JUE_M3 >= 3 then _JUE_Tot   
															when _JUE_Tot > 3 then _JUE_Tot - 3 else 0 end end;
				if _JUE_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if;
				_VIE_HE := case when _VIE_Tot is null then 0 else 
												case 	when _VIE_M3 >= 3 then 0  
															when _VIE_Tot > 3 then 3 else _VIE_Tot end end;
				_VIE_HT := case when _VIE_Tot is null then 0 else 
												case 	when _VIE_M3 >= 3 then _VIE_Tot   
															when _VIE_Tot > 3 then _VIE_Tot - 3 else 0 end end;
				if _VIE_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if;
				_SAB_HE :=  case when _SAB_Tot is null then 0 else 
												case 	when _SAB_M3 >= 3 then 0  
															when _SAB_Tot > 3 then 3 else _SAB_Tot end end;
				_SAB_HT := case when _SAB_Tot is null then 0 else 
												case 	when _SAB_M3 >= 3 then _SAB_Tot   
															when _SAB_Tot > 3 then _SAB_Tot - 3 else 0 end end;
				if _SAB_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if;
				-- asigna las horas domingo
				_DOM_HE := case when _DOM_Tot is null then 0 else _DOM_Tot end;
				_DOM_HT := 0;
				if _DOM_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if;
				
				if(_LUN_Fecha >= _Fecha_Desde and _LUN_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _LUN_HE;
					_TOTAL_HT := _TOTAL_HT + _LUN_HT;
				end if;
				if(_MAR_Fecha >= _Fecha_Desde and _MAR_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _MAR_HE;
					_TOTAL_HT := _TOTAL_HT + _MAR_HT;
				end if;
				if(_MIE_Fecha >= _Fecha_Desde and _MIE_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _MIE_HE;
					_TOTAL_HT := _TOTAL_HT + _MIE_HT;
				end if;
				if(_JUE_Fecha >= _Fecha_Desde and _JUE_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _JUE_HE;
					_TOTAL_HT := _TOTAL_HT + _JUE_HT;
				end if;
				if(_VIE_Fecha >= _Fecha_Desde and _VIE_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _VIE_HE;
					_TOTAL_HT := _TOTAL_HT + _VIE_HT;
				end if;
				if(_SAB_Fecha >= _Fecha_Desde and _SAB_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _SAB_HE;
					_TOTAL_HT := _TOTAL_HT + _SAB_HT;
				end if;
				if(_DOM_Fecha >= _Fecha_Desde and _DOM_Fecha <= _Fecha_Hasta)
				then
					_TOTAL_HD := _TOTAL_HD + _DOM_HE;
				end if;
			
			
				--select _LUN_Fecha as LUN_HE, _MAR_Fecha as MAR_HE, _MIE_Fecha as MIE_HE, _JUE_Fecha as JUE_HE, 
					--				_VIE_Fecha as VIE_HE, _SAB_Fecha as SAB_HE, _DOM_Fecha as DOM_HE
			
				--select _LUN_HE as LUN_HE, _LUN_HF as LUN_F, _MAR_HE as MAR_HE, _MAR_HF as MAR_F, _MIE_HE as MIE_HE, _MIE_HF as MIE_F,
					--			_JUE_HE as JUE_HE, _JUE_HF as JUE_F, _VIE_HE as VIE_HE, _VIE_HF as VIE_F, _SAB_HE as SAB_HE, _SAB_HF as SAB_F,
						--			_DOM_HE as DOM_HE, _DOM_HF as DOM_F
			
				--select _daynum as DIA, _contsem as Semana, _fechasemini as INI, _fechasemfin as FIN
			
				_contsem := _contsem + 1;
			
			end loop;
		END IF;
		-- fin calculo de nomina de confianza
	ELSE
		IF _MaxHE > 0 -- Si se especifica la variable de Maximo de horas extras acumuladas, significa que va a hacer el proceso simple del calculo de horas extras 
		THEN
			_TOTAL_HE := case when ( 	select sum(HNA) 
																		from TBL_NOM_DIARIO_DET 
																		where ID_Empleado = _ID_Empleado and 
																					ID_Movimiento = _movhea and 
																					ID_FechaMovimiento between  _Fecha_Desde and _Fecha_Hasta ) is null
													then 0
													else	( 	select sum(HNA) 
																		from TBL_NOM_DIARIO_DET 
																		where ID_Empleado = _ID_Empleado and 
																					ID_Movimiento = _movhea and 
																					ID_FechaMovimiento between  _Fecha_Desde and _Fecha_Hasta )
								
						end;
		ELSE
			--SET DATEFIRST 1
			--set DATEFIRST 1
			_semini := date_part('week',_Fecha_Desde);
			_semfin := date_part('week',_Fecha_Hasta);

			if _semini > _semfin -- ser? mayor la semana inicial cuando es del a?o pasado y la final del a?o siguiente Ejem 29-12-2004 a 04-01-2005
			then
				_semini := ( _semfin - 1 );
			end if;
			
			_dayini := date_part('dow',_Fecha_Desde); if _dayini = 0 then _dayini := 7; end if;
			_dayfin := date_part('dow',_Fecha_Hasta); if _dayfin = 0 then _dayfin := 7; end if;

			raise notice 'semini % semfin %           dayini  % dayfin %  ',_semini,_semfin, _dayini, _dayfin; 
			
			_contsem := _semini;
			
			--primero llena toda la estructura de las horas del periodo de nomina
			while(_contsem <= _semfin) -- controla el numero de semanas en la nomina de la semana 3 a la 4 por ejemplo
			loop
				if _contsem = _semini
				then
					_daynum := _dayini;
					_fechasemini := ( _Fecha_Desde - (cast((_daynum -1) as text) || ' days')::interval); --dateadd(day, -(_daynum -1), _Fecha_Desde)
					_fechasemfin := ( _Fecha_Desde + (cast((7 - _daynum) as text) || ' days')::interval); --dateadd(day, (7 - _daynum), _Fecha_Desde)
				elsif _contsem = _semfin
				then
					_daynum := _dayfin;
					_fechasemini := ( _Fecha_Hasta - (cast((_daynum -1) as text) || ' days')::interval); --dateadd(day, -(_daynum -1), _Fecha_Hasta)
					_fechasemfin := ( _Fecha_Hasta + (cast((7 - _daynum) as text) || ' days')::interval); --dateadd(day, (7 - _daynum), _Fecha_Hasta)
				else
					_daynum := _dayini;
					_fechasemaux := ( _Fecha_Desde + (cast(((_contsem - _semini)*7) as text) || ' days')::interval); --dateadd(day, ((_contsem - _semini)*7), _Fecha_Desde);
					_fechasemini := ( _fechasemaux - (cast((_daynum -1) as text) || ' days')::interval); --dateadd(day, -(_daynum -1), fechasemaux );
					_fechasemfin := ( _fechasemaux + (cast((7 - _daynum) as text) || ' days')::interval); --dateadd(day, (7 - _daynum), fechasemaux);
				end if;
				
				--select _daynum as DAYNUM, _contsem as CONTSEM, _fechasemini as FECHASEMINI, _fechasemfin as FECHASEMFIN
				--select _semini as SEMINI, _semfin as SEMFIN, _dayini as DIAINI, _dayfin as DIAFIN, _daynum as DAYNUM, 
					--_fechasemini as FECHASEMINI, _fechasemfin as FECHASEMFIN 
		
				_SAB_M3 := 0;
				_LUN_M3 := 0;
				_MAR_M3 := 0;
			
				_MIE_Fecha := _fechasemini;
				_MIE_Tot := ( 	select sum(HNA) 
													from TBL_NOM_DIARIO_DET 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _MIE_Fecha );
				if _MIE_Tot > 0
				then
					_SAB_M3 := _SAB_M3 + 1;
				end if;
			
				_JUE_Fecha := _fechasemini + '1 day'::interval; --dateadd(day,1,_fechasemini)
				_JUE_Tot := ( 	select sum(HNA) 
													from TBL_NOM_DIARIO_DET 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _JUE_Fecha );
				if _JUE_Tot > 0
				then
					_SAB_M3 := _SAB_M3 + 1;
				end if;
			
				_VIE_Fecha := _fechasemini + '2 days'::interval; --dateadd(day,2,_fechasemini)
				_VIE_Tot := ( 	select sum(HNA) 
													from TBL_NOM_DIARIO_DET 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _VIE_Fecha );
				if _VIE_Tot > 0
				then
					_SAB_M3 := _SAB_M3 + 1;
				end if;
			
				_SAB_Fecha := _fechasemini + '3 days'::interval; --dateadd(day,3,_fechasemini) 
				_SAB_Tot := ( 	select sum(HNA) 
								from TBL_NOM_DIARIO_DET 
								where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _SAB_Fecha );
				if _SAB_Tot > 0
				then
					_LUN_M3 := _SAB_M3 + 1;
				else
					_LUN_M3 := _SAB_M3;
				end if;
				
				_DOM_Fecha := _fechasemini + '4 days'::interval; --dateadd(day,4,_fechasemini) 
				_DOM_Tot := ( 	select sum(HNA) 
													from TBL_NOM_DIARIO_DET 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _DOM_Fecha );
		
		
				_LUN_Fecha := _fechasemini + '5 days'::interval; --dateadd(day,5,_fechasemini) 
				_LUN_Tot := ( 	select sum(HNA) 
													from TBL_NOM_DIARIO_DET 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _LUN_Fecha );
				if _LUN_Tot > 0
				then
					_MAR_M3 := _LUN_M3 + 1;
				else
					_MAR_M3 := _LUN_M3;
				end if;
				
				_MAR_Fecha := _fechasemini + '6 days'::interval; --dateadd(day,6,_fechasemini) 
				_MAR_Tot := ( 	select sum(HNA) 
													from TBL_NOM_DIARIO_DET 
													where ID_Empleado = _ID_Empleado and 
																	ID_Movimiento = _movhea and 
																					ID_FechaMovimiento = _MAR_Fecha );
			
		
				
				_MIE_HE := case when _MIE_Tot is null then 0 else case when _MIE_Tot > 3 then 3 else _MIE_Tot end end;
				_MIE_HT := case when _MIE_Tot is null then 0 else case when _MIE_Tot > 3 then _MIE_Tot - 3 else 0 end end;
				if _MIE_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if; 
				_JUE_HE := case when _JUE_Tot is null then 0 else case when _JUE_Tot > 3 then 3 else _JUE_Tot end end;
				_JUE_HT := case when _JUE_Tot is null then 0 else case when _JUE_Tot > 3 then _JUE_Tot - 3 else 0 end end;
				if _JUE_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if; 
				_VIE_HE := case when _VIE_Tot is null then 0 else case when _VIE_Tot > 3 then 3 else _VIE_Tot end end;
				_VIE_HT := case when _VIE_Tot is null then 0 else case when _VIE_Tot > 3 then _VIE_Tot - 3 else 0 end end;
				if _VIE_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if; 
				_SAB_HE := case when _SAB_Tot is null then 0 else 
												case 	when _SAB_M3 >= 3 then 0  
															when _SAB_Tot > 3 then 3 else _SAB_Tot end end;
				_SAB_HT := case when _SAB_Tot is null then 0 else 
												case 	when _SAB_M3 >= 3 then _SAB_Tot   
															when _SAB_Tot > 3 then _SAB_Tot - 3 else 0 end end;
				if _SAB_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if; 
				-- asigna las horas domingo
				_DOM_HE := case when _DOM_Tot is null then 0 else _DOM_Tot end;
				_DOM_HT := 0;
				if _DOM_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if; 
				
				_LUN_HE := case when _LUN_Tot is null then 0 else 
												case 	when _LUN_M3 >= 3 then 0  
															when _LUN_Tot > 3 then 3 else _LUN_Tot end end;
				_LUN_HT := case when _LUN_Tot is null then 0 else 
												case 	when _LUN_M3 >= 3 then _LUN_Tot   
															when _LUN_Tot > 3 then _LUN_Tot - 3 else 0 end end;
				if _LUN_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if; 
				_MAR_HE := case when _MAR_Tot is null then 0 else 
												case 	when _MAR_M3 >= 3 then 0  
															when _MAR_Tot > 3 then 3 else _MAR_Tot end end;
				_MAR_HT := case when _MAR_Tot is null then 0 else 
												case 	when _MAR_M3 >= 3 then _MAR_Tot   
															when _MAR_Tot > 3 then _MAR_Tot - 3 else 0 end end;
				if _MAR_Tot is not null then _DiasHorasExtras := _DiasHorasExtras + 1; end if; 
				
				if(_LUN_Fecha >= _Fecha_Desde and _LUN_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _LUN_HE;
					_TOTAL_HT := _TOTAL_HT + _LUN_HT;
				end if;
				if(_MAR_Fecha >= _Fecha_Desde and _MAR_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _MAR_HE;
					_TOTAL_HT := _TOTAL_HT + _MAR_HT;
				end if;
				if(_MIE_Fecha >= _Fecha_Desde and _MIE_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _MIE_HE;
					_TOTAL_HT := _TOTAL_HT + _MIE_HT;
				end if;
				if(_JUE_Fecha >= _Fecha_Desde and _JUE_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _JUE_HE;
					_TOTAL_HT := _TOTAL_HT + _JUE_HT;
				end if;
				if(_VIE_Fecha >= _Fecha_Desde and _VIE_Fecha <= _Fecha_Hasta)
				then	
					_TOTAL_HE := _TOTAL_HE + _VIE_HE;
					_TOTAL_HT := _TOTAL_HT + _VIE_HT;
				end if;
				if(_SAB_Fecha >= _Fecha_Desde and _SAB_Fecha <= _Fecha_Hasta)
				then
					_TOTAL_HE := _TOTAL_HE + _SAB_HE;
					_TOTAL_HT := _TOTAL_HT + _SAB_HT;
				end if;
				if(_DOM_Fecha >= _Fecha_Desde and _DOM_Fecha <= _Fecha_Hasta)
				then
					_TOTAL_HD := _TOTAL_HD + _DOM_HE;
				end if;
			
			
				/*
				select _MIE_Fecha as MIE_FECHA, _JUE_Fecha as JUE_FECHA, 
									_VIE_Fecha as VIE_FECHA, _SAB_Fecha as SAB_FECHA, _DOM_Fecha as DOM_FECHA, _LUN_Fecha as LUN_FECHA, _MAR_Fecha as MAR_FECHA
			
				select _MIE_HE as MIE_HE, _MIE_HF as MIE_F,
								_JUE_HE as JUE_HE, _JUE_HF as JUE_F, _VIE_HE as VIE_HE, _VIE_HF as VIE_F, _SAB_HE as SAB_HE, _SAB_HF as SAB_F,
									_DOM_HE as DOM_HE, _DOM_HF as DOM_F, _LUN_HE as LUN_HE, _LUN_HF as LUN_F, _MAR_HE as MAR_HE, _MAR_HF as MAR_F
				--*/
		
				_contsem := _contsem + 1;
			
			end loop;
		END IF;
	END IF;

	IF _MaxHE > 0
	THEN
		IF(_TOTAL_HE > _MaxHE)
		THEN
			_TOTAL_HF := _TOTAL_HE - _MaxHE;
			_TOTAL_HE := _MaxHE;
		END  IF;

		IF _TOTAL_HE > 0
		THEN
			_DiasHorasExtras := case 	when _TOTAL_HE > 6 then 3
										when _TOTAL_HE <= 6 and _TOTAL_HE > 3 then 2
										else 1 end;
		END IF;
	END IF;

	-- si se especifcó  todas las horas extras por fuera, Aplica todas las horas por fuera
	IF _HEPF = '1'
	THEN
		_TOTAL_HF := _TOTAL_HF + _TOTAL_HE + _TOTAL_HD + _TOTAL_HT;
		_TOTAL_HE := 0;
		_TOTAL_HD := 0;
		_TOTAL_HT := 0;
		_DiasHorasExtras := 0;
	END IF;

	RETURN QUERY
	SELECT _TOTAL_HE, _TOTAL_HF, _TOTAL_HD, _TOTAL_HT, _DiasHorasExtras;
	
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_calculo_nomina_horas_extras(smallint, character, timestamp without time zone, timestamp without time zone, bit, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cajas_cierrez(
    _entidad smallint,
    _transferir bit,
    _obs character varying,
    _beneficiario character varying,
    _rfc character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(254); _errpart int; _resultpart varchar(254); _ID_Cierre integer; _Desde int; _Hasta int; _tipoBAN smallint; _claveBAN smallint; _tipoDEST smallint; _claveDEST smallint; _Numero integer;
	_Concepto varchar(40); _Descripcion varchar(15); _Facturas numeric(19,4); _Devoluciones numeric(19,4);
	_OtrosDEP numeric(19,4); _OtrosRET numeric(19,4); _Traspaso numeric(19,4); _TotalVales numeric(19,4); _TotalFinales numeric(19,4); 
	_TotalFacturas numeric(19,4); _TotalOtros numeric(19,4); _TotalPagos numeric(19,4); _TotalTraspasos numeric(19,4); _TotalEfectivo numeric(19,4);
	_Fondo numeric(19,4); _TotalPorComp numeric(19,4); _NetoEfectivo numeric(19,4); _idTrasp integer; _Fecha timestamp;  _mes smallint; _ano smallint; 
	_DesdeFact varchar(255); _HastaFact varchar(255); _DesdeDev varchar(255); _HastaDev varchar(255); 
	_REC RECORD; _Revertidas boolean;
BEGIN

	CREATE LOCAL TEMPORARY TABLE _TMP_VENTAS_CIERRE (
		Partida serial NOT NULL ,
		Clave varchar(20) NOT NULL ,
		Descripcion varchar(255) NOT NULL ,
		Total numeric(19,4) NULL 
	);
	CREATE LOCAL TEMPORARY TABLE _TMP_VENTAS_CIERRES_FACTURAS (
		ID_Entidad integer NOT NULL,
		Numero integer NOT NULL,
		Total numeric(19,4) NOT NULL
	);
  
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'El cierre se genero correctamente';
	_Descripcion := (select Cuenta from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _entidad);
	_Desde := (select UltimoNumTrasp from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _entidad) + 1;
	_Hasta := (select max(Num) from TBL_BANCOS_MOVIMIENTOS where Tipo = 1 and Clave = _entidad);
	_Fecha := NOW()::date;
	_tipoBAN := 1;
	_claveBAN := _Entidad;
	_tipoDEST := (select TipoTRASP from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _entidad);
	_claveDEST := (select ClaveTRASP from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _entidad);
	_Fondo := (select FondoTRASP from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _entidad);
	_Numero := (select max(Numero) from tbl_ventas_cierres_cab where TipoDesde = 1 and ClaveDesde = _entidad) + 1;
	_DesdeFact := '';
	_HastaFact := '';
	_DesdeDev := '';
	_HastaDev := '';
	_Revertidas := false;
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);

	IF _transferir = '1'
	THEN
		IF( (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = '1') > 0
			OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1 )
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha pertenece a un periodo cerrado o inexistente'
		END IF;
	END IF;
	
	IF _Numero is null 
	THEN
		_Numero := 1; 
	END IF;				
	
	IF _tipoDEST = 0 OR _claveDEST = 0 
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: No se puede realizar el cierre porque la caja de destino no se define aun'
		
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ETQ', _result, null;
	END IF;

	IF _tipoBAN = _tipoDEST AND _claveBAN = _claveDEST 
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: No se puede realizar el cierre porque la caja de origen y de destino es la misma';
		
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ETQ', _result, null;
	END IF;	
		
	IF (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = _tipoDEST and Clave = _claveDEST) <> 1 OR
		(select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = _tipoBAN and Clave = _claveBAN) <> 1 
	THEN
		_err := 3;
		_result := (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: No se puede realizar el cierre porque la caja de destino esta en moneda extranjera';
		
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ETQ', _result, null;
	END IF;
			
	IF _Desde is null or _Hasta is null
	THEN
		_err = 3;
		_result = (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El numero inicial o final son Nulos. Es probable que no haya habido movimientos aun';

		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ETQ', _result, null;
	ELSE
		IF _Desde > _Hasta
		THEN
			_err = 3;
			_result = (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El numero inicial es mayor al numero final. Esto puede deberse a que no hay movimientos desde el ultimo cierre';
			
			INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
			SELECT 'ETQ', _result, null;
	
		END IF;
	END IF;


	IF _err = 0
	THEN
		_Concepto := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ') 
				|| ' ' || _Descripcion || ' ' || cast(_Desde as varchar) || ' - ' || cast(_Hasta as varchar);
		
		--Genera los totales de caja.
		_Facturas := ( 	select ROUND(sum(Deposito),2) 
				from TBL_BANCOS_MOVIMIENTOS 
				where Tipo = _tipoBAN and Clave = _claveBAN and Estatus <> 'C' and 
					Num between _Desde and _Hasta and Ref like 'VFAC|%' );
		_Devoluciones := ( select ROUND(sum(Retiro),2) 
				from TBL_BANCOS_MOVIMIENTOS 
				where Tipo = _tipoBAN and Clave = _claveBAN and Estatus <> 'C' and 
					Num between _Desde and _Hasta and Ref like 'VDEV|%' );
		_OtrosDEP := ( select ROUND(sum(Deposito),2) 
				from TBL_BANCOS_MOVIMIENTOS 
				where Tipo = _tipoBAN and Clave = _claveBAN and Estatus <> 'C' and 
					Num between _Desde and _Hasta and Ref not like 'VFAC|%' );
		_OtrosRET := ( select ROUND(sum(Retiro),2) 
				from TBL_BANCOS_MOVIMIENTOS 
				where Tipo = _tipoBAN and Clave = _claveBAN and Estatus <> 'C' and 
					Num between _Desde and _Hasta and Ref not like 'VDEV|%' );

		IF _Facturas is null 
		THEN
			_Facturas := 0; 
		ELSE
			_DesdeFact := ( select e.descripcion || ' ' || c.Numero::varchar  from tbl_ventas_facturas_cab c join tbl_ventas_entidades e on c.id_entidad = e.id_entidadventa where c.id_vc = 
				split_part(  (select Ref from TBL_BANCOS_MOVIMIENTOS where Tipo = _tipoBAN and Clave = _claveBAN and Estatus <> 'C' and Num between _Desde and _Hasta and Ref like 'VFAC|%' order by Num asc limit 1) , '|', 2)::integer );
			_HastaFact := ( select e.descripcion || ' ' || c.Numero::varchar  from tbl_ventas_facturas_cab c join tbl_ventas_entidades e on c.id_entidad = e.id_entidadventa where c.id_vc = 
				split_part(  (select Ref from TBL_BANCOS_MOVIMIENTOS where Tipo = _tipoBAN and Clave = _claveBAN and Estatus <> 'C' and Num between _Desde and _Hasta and Ref like 'VFAC|%' order by Num desc limit 1 ) , '|', 2)::integer );
		END IF;
		IF _Devoluciones is null
		THEN
			_Devoluciones := 0;
		ELSE
			_DesdeDev := ( select e.descripcion || ' ' || c.Numero::varchar  from tbl_ventas_devoluciones_cab c join tbl_ventas_entidades e on c.id_entidad = e.id_entidadventa where c.id_vc = 
				split_part(  (select Ref from TBL_BANCOS_MOVIMIENTOS where Tipo = _tipoBAN and Clave = _claveBAN and Estatus <> 'C' and Num between _Desde and _Hasta and Ref like 'VDEV|%' order by Num asc limit 1) , '|', 2)::integer );
			_HastaDev := ( select e.descripcion || ' ' || c.Numero::varchar  from tbl_ventas_devoluciones_cab c join tbl_ventas_entidades e on c.id_entidad = e.id_entidadventa where c.id_vc = 
				split_part(  (select Ref from TBL_BANCOS_MOVIMIENTOS where Tipo = _tipoBAN and Clave = _claveBAN and Estatus <> 'C' and Num between _Desde and _Hasta and Ref like 'VDEV|%' order by Num desc limit 1 ) , '|', 2)::integer );
		END IF;
		IF _OtrosDEP is null
		THEN
			_OtrosDEP := 0;
		END IF;
		IF _OtrosRET is null
		THEN
			_OtrosRET := 0;
		END IF;
	
		_Traspaso := (_Facturas + _OtrosDEP) - (_Devoluciones + _OtrosRET); 
	
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ETQ', _Concepto, null;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
	
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ACU', (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ') || ' ' || _DesdeFact || ' - ' || _HastaFact, _Facturas;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ACU', (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ') || ' ' || _DesdeDev || ' - ' || _HastaDev, -_Devoluciones;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ACU', (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), _OtrosDEP;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ACU', (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), -_OtrosRET;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'TIT', (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ2'), _Traspaso;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;

		--Ahora se involucra en vales
		_TotalFinales := ( select sum(Final) from VIEW_VALES where ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'F'); 
		_TotalFacturas := ( select sum(Factura) from VIEW_VALES where ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'A'); 
		_TotalOtros := ( select sum(Compra) from VIEW_VALES where ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'C'); 
		_TotalPagos := ( select sum(Pago) from VIEW_VALES where ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'G'); 
		_TotalTraspasos := ( select sum(Traspaso) from VIEW_VALES where ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'T'); 
		_TotalPorComp := ( select sum(Provisional) from VIEW_VALES where ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'P'); 
				
		IF _TotalFinales is null
		THEN
			_TotalFinales := 0;
		END IF;
		IF _TotalFacturas is null
		THEN
			_TotalFacturas := 0;
		END IF;
		IF _TotalOtros is null
		THEN
			_TotalOtros := 0;
		END IF;
		IF _TotalPagos is null
		THEN
			_TotalPagos := 0;
		END IF;
		IF _TotalTraspasos is null
		THEN
			_TotalTraspasos := 0;
		END IF;
		IF _TotalPorcomp is null
		THEN
			_TotalPorComp := 0;
		END IF;	
		
		_TotalVales := (_TotalFinales + _TotalFacturas + _TotalOtros + _TotalPagos + _TotalTraspasos);
		_TotalEfectivo := (_Traspaso - _TotalVales);
		_NetoEfectivo = _Fondo + _TotalEfectivo - _TotalPorComp;
	
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'TIT', (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ2'), -_TotalVales;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
		
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'VAL', (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ2'), _TotalFinales;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT ID_Gasto, cast((Fecha::date) as varchar) || ' ' || substring(Concepto,1,30), Final
		FROM VIEW_VALES WHERE ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'F';
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
		
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'VAL', (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ2'), _TotalFacturas;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT ID_Gasto, cast((Fecha::date) as varchar) || ' ' || substring(Concepto,1,30), Factura
		FROM VIEW_VALES WHERE ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'A';
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
			
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'VAL', (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ2'), _TotalTraspasos;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT ID_Gasto, cast((Fecha::date) as varchar) || ' ' || substring(Concepto,1,30), Traspaso
		FROM VIEW_VALES WHERE ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'T';
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;


		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'VAL', (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ3'), _TotalPagos;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT ID_Gasto, cast((Fecha::date) as varchar) || ' ' || substring(Concepto,1,30), Pago
		FROM VIEW_VALES WHERE ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'G';
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
		
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'VAL', (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ3'), _TotalOtros;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT ID_Gasto, cast((Fecha::date) as varchar) || ' ' || substring(Concepto,1,30), Compra
		FROM VIEW_VALES WHERE ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'C';
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
	
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'TIT', (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ3'), _TotalEfectivo;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
		
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'TIT', (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ3'), _Fondo;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
	
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'VAL', (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ3'), -_TotalPorcomp;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT ID_Gasto, cast((Fecha::date) as varchar) || ' ' || substring(Concepto,1,30), Provisional
		FROM VIEW_VALES WHERE ID_Clave = _claveBAN and Numero = 0 and ID_Tipo = 'P';
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;
	
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'TIT', (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ4'), _NetoEfectivo;
		INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
		SELECT 'ESP','', null;

		--///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		--/////////////////////	INSERTA LAS FACTURAS EN LA TABLA TEMPORAL Y EL DESCGLOSE DE FACTURAS DEL CIERRE
		IF _Facturas != 0.0
		THEN
			INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
			SELECT 'ETQ', (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'BANCAJ_CIERRES' and m.sub::text = 'BD' and m.elm::text = 'ETQ4'), null;
			
			FOR _REC IN ( 	select Fecha, Deposito, Ref from TBL_BANCOS_MOVIMIENTOS where Tipo = _tipoBAN and Clave = _claveBAN and Estatus <> 'C' and Num between _Desde and _Hasta and Ref like 'VFAC|%' order by Num asc	 ) 
			LOOP
				IF (select c.Status  from tbl_ventas_facturas_cab c where c.id_vc = split_part(_REC.Ref, '|', 2)::integer) = 'R' -- Significa que existen facturas de este cierre revertidas
				THEN
					_Revertidas = true;
				END IF;
				
				INSERT INTO _TMP_VENTAS_CIERRES_FACTURAS (ID_Entidad, Numero, Total)
				VALUES(	(select c.ID_Entidad  from tbl_ventas_facturas_cab c where c.id_vc = split_part(_REC.Ref, '|', 2)::integer),
								(select c.Numero  from tbl_ventas_facturas_cab c where c.id_vc = split_part(_REC.Ref, '|', 2)::integer), _REC.Deposito	);

				INSERT INTO _TMP_VENTAS_CIERRE (Clave, Descripcion, Total)
				VALUES(       ( select e.descripcion from tbl_ventas_facturas_cab c join tbl_ventas_entidades e on c.id_entidad = e.id_entidadventa where c.id_vc =  split_part(_REC.Ref, '|', 2)::integer), 
								cast((_REC.Fecha::date) as varchar) || ' - ' || (select c.Numero  from tbl_ventas_facturas_cab c where c.id_vc = split_part(_REC.Ref, '|', 2)::integer)::varchar 
									|| ' - ' || (select c.Status  from tbl_ventas_facturas_cab c where c.id_vc = split_part(_REC.Ref, '|', 2)::integer), _REC.Deposito	);
			END LOOP;
		END IF;
				
		--Genera la transferencia
		IF _Transferir = '1'
		THEN
			IF _Revertidas = false
			THEN
				INSERT INTO TBL_VENTAS_CIERRES_CAB
				VALUES(default, _tipoBAN, _claveBAN, _Numero, _Desde, _Hasta, _tipoDEST, _claveDEST, _Fecha, 'G',
					_Facturas, _Devoluciones, _OtrosDep, _OtrosRet, _Traspaso, _TotalVales, _TotalFinales, _TotalFacturas,
					_TotalOtros, _TotalPagos, _TotalTraspasos, _TotalEfectivo, _Fondo, _TotalPorComp, _NetoEfectivo, _Obs)
				RETURNING currval(pg_get_serial_sequence('TBL_VENTAS_CIERRES_CAB', 'id_cierre')) INTO _ID_Cierre;

				INSERT INTO TBL_VENTAS_CIERRES_STMP
				SELECT _ID_Cierre, Partida, Clave, Descripcion, Total
				FROM _TMP_VENTAS_CIERRE
				WHERE (Total is not null and Total <> 0 and 
					(Clave <> 'TIT' and Clave <> 'ETQ' and Clave <> 'ESP' and Clave <> 'OPT')) or (Clave = 'TIT' or Clave = 'ETQ' or Clave = 'ESP' or Clave = 'OPT');

				INSERT INTO TBL_VENTAS_CIERRES_FACTURAS
				SELECT _ID_Cierre, ID_Entidad, Numero, Total
				FROM _TMP_VENTAS_CIERRES_FACTURAS;
				
				-- GENERA LA TRANSFERENCIA DE CAJAS
				--RAISE NOTICE '% / % / % / % / %',_claveBAN,_Fecha,_Concepto,_Traspaso,_claveDEST; 
				SELECT * INTO _errpart, _resultpart, _idTRASP
				FROM sp_bancos_trans_agregar('1',_claveBAN,_Fecha,(_Concepto || ' ' || _DesdeFact || ' - ' || _HastaFact),_Beneficiario,_Traspaso,'RET','G','1',1.0,'','','1',_claveDEST,'000',_RFC,'01','','','') AS ( err integer, res varchar, clave integer);
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					-- Actualiza el ultimo numero de movimiento (El Traspaso) en la entidad
					UPDATE TBL_BANCOS_CUENTAS
					SET UltimoNumTrasp = _Hasta + 1
					WHERE Tipo = _tipoBAN and Clave = _claveBAN;

					-- Procede a hacer el traspaso de vales
					--RAISE NOTICE 'OKY: ID Trasp: %', _idTRASP; 

					SELECT * INTO _errpart, _resultpart, _idTRASP
					FROM sp_cajas_vales_traspasar(_claveBAN, _claveDEST, '1', '1', '1', '1', '1') AS ( err integer, res varchar, clave smallint);
					
					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
					END IF;
				END IF;
			ELSE --Existen facturas revertidas, no genera el cierre
				_err := 3;
				_result := 'ERROR: Existen facturas revertidas en este cierre, no se puede generar el cierre hasta que se cancelen todas estas facturas';
			END IF;
		END IF;
		
	END IF;

	IF _Transferir = '0'
	THEN
		RETURN QUERY
		SELECT 1, Partida, Clave, Descripcion, 
			CASE WHEN Total is null THEN 0 ELSE Total END
		FROM _TMP_VENTAS_CIERRE 
		WHERE (Total is not null and Total <> 0 and (Clave <> 'TIT' and Clave <> 'ETQ' and Clave <> 'ESP' and Clave <> 'OPT')) 
			or (Clave = 'TIT' or Clave = 'ETQ' or Clave = 'ESP' or Clave = 'OPT');
	ELSE
		RETURN QUERY SELECT _err, _result, _entidad;
	END IF;

	DROP TABLE _TMP_VENTAS_CIERRE;
	DROP TABLE _TMP_VENTAS_CIERRES_FACTURAS;
	
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cajas_cierrez(smallint, bit, character varying, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
INSERT INTO tbl_usuarios_permisos_catalogo
VALUES ('CRM');

--@FIN_BLOQUE
delete from TBL_VARIABLES
where ID_Variable = 'CC_COSTVEN';

--@FIN_BLOQUE
ALTER TABLE tbl_compras_entidades
ADD  tipocobro smallint;

UPDATE tbl_compras_entidades
SET tipocobro = 2;

ALTER TABLE tbl_compras_entidades
ALTER COLUMN  tipocobro set not null;

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_compras_entidades AS 
 SELECT e.id_entidadcompra, e.id_tipoentidad, e.serie, e.doc, e.descripcion, e.formato, e.id_bodega, b.descripcion AS nombrebodega, e.fija, e.fijacost, e.devolucion, e.orden, e.fmt_devolucion, e.fmt_orden, e.iva, e.infoplantoc, e.infogasrec, e.id_clasificacion, e.status, e.recepcion, e.fmt_recepcion, e.tipocobro
   FROM tbl_compras_entidades e
   JOIN tbl_invserv_bodegas b ON e.id_bodega = b.id_bodega;

ALTER TABLE view_compras_entidades
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_compras_entidades_agregar(smallint, smallint, character varying, integer, character varying, character varying, smallint, bit, bit, integer, integer, character varying, character varying, numeric, smallint, smallint, character varying, character, integer, character varying);

CREATE OR REPLACE FUNCTION sp_compras_entidades_agregar(
    _id_entidadcompra smallint,
    _id_tipoentidad smallint,
    _serie character varying,
    _doc integer,
    _descripcion character varying,
    _formato character varying,
    _id_bodega smallint,
    _fija bit,
    _fijacost bit,
    _devolucion integer,
    _orden integer,
    _fmt_devolucion character varying,
    _fmt_orden character varying,
    _iva numeric,
    _infoplantoc smallint,
    _infogasrec smallint,
    _id_clasificacion character varying,
    _status character,
    _recepcion integer,
    _fmt_recepcion character varying,
    _tipocobro smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _Numero smallint;
BEGIN
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');--'La entidad de compra/gasto se registró correctamente';

	IF(select count(*) from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) > 0 
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: La clave de la entidad ya existe en otro registro';
	END IF; 

	IF(select count(*) from TBL_COMPRAS_ENTIDADES where ID_TipoEntidad = _ID_TipoEntidad and Serie = _Serie) > 0 
	THEN
		_err := 3;
		_result := (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: Ya existe la serie para este tipo de documento';
	END IF;

	--Verifica si la bodega es compatible con la entidad que se va a agregar 
	IF _ID_TipoEntidad = 0
	THEN
		IF(select count(*) from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega and ID_InvServ = 'P') < 1
		THEN
			_err := 3;
			_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR3');
		END IF;
	ELSE
		IF(select count(*) from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega and ID_InvServ = 'G') < 1
		THEN
			_err := 3;
			_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR3');
		END IF;
	END IF;
	
	IF _err = 0
	THEN
		INSERT INTO TBL_COMPRAS_ENTIDADES
		VALUES(_ID_EntidadCompra, _ID_TipoEntidad, _Serie, _Doc, _Descripcion, _Formato, _ID_Bodega, _Fija, _FijaCost, _Devolucion, _Orden, _Fmt_Devolucion, _Fmt_Orden, _IVA, _InfoPlantOC, _InfoGasRec, _ID_Clasificacion, _Status, _Recepcion, _Fmt_Recepcion, _TipoCobro);
		
		-- INSERTA LOS ENLACES
		INSERT INTO TBL_BANCOS_VS_COMPRAS
		SELECT Tipo, Clave, _ID_EntidadCompra
		FROM _TMP_BANCOS_VS_COMPRAS;

	END IF;

	RETURN QUERY SELECT _err, _result, _ID_EntidadCompra::varchar;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_entidades_agregar(smallint, smallint, character varying, integer, character varying, character varying, smallint, bit, bit, integer, integer, character varying, character varying, numeric, smallint, smallint, character varying, character, integer, character varying, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_compras_entidades_cambiar(smallint, smallint, character varying, integer, character varying, character varying, smallint, bit, bit, integer, integer, character varying, character varying, numeric, smallint, smallint, character varying, character, integer, character varying);

CREATE OR REPLACE FUNCTION sp_compras_entidades_cambiar(
    _id_entidadcompra smallint,
    _id_tipoentidad smallint,
    _serie character varying,
    _doc integer,
    _descripcion character varying,
    _formato character varying,
    _id_bodega smallint,
    _fija bit,
    _fijacost bit,
    _devolucion integer,
    _orden integer,
    _fmt_devolucion character varying,
    _fmt_orden character varying,
    _iva numeric,
    _infoplantoc smallint,
    _infogasrec smallint,
    _id_clasificacion character varying,
    _status character,
    _recepcion integer,
    _fmt_recepcion character varying,
    _tipocobro smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); 
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');--'La entidad de compra/gasto se registró correctamente';

	IF(select count(*) from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: La clave de la entidad No existe, no se puede cambiar';
	END IF; 

	IF(select count(*) from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra <> _ID_EntidadCompra and ID_TipoEntidad = _ID_TipoEntidad and Serie = _Serie) > 0 
	THEN
		_err := 3;
		_result := (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: Ya existe la serie para este tipo de documento, no se puede cambiar';
	END IF;

	IF (select ID_TipoEntidad from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) = 0
	THEN
		IF(select count(*) from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega and ID_InvServ = 'P') < 1
		THEN
			_err := 3;
			_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR3');
		END IF;
	ELSE
		IF(select count(*) from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega and ID_InvServ = 'G') < 1
		THEN
			_err := 3;
			_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_ENTIDADES' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR3');
		END IF;
	END IF;

	IF _err = 0
	THEN
		DELETE FROM TBL_BANCOS_VS_COMPRAS
		WHERE ID_EntidadCompra = _ID_EntidadCompra;
		
		UPDATE TBL_COMPRAS_ENTIDADES
		SET Serie = _Serie, Doc = _Doc, Descripcion = _Descripcion,  Formato = _Formato, ID_Bodega = _ID_Bodega, Fija = _Fija, FijaCost = _FijaCost, 
				Devolucion = _Devolucion, Orden = _Orden, Fmt_Devolucion = _Fmt_Devolucion, Fmt_Orden = _Fmt_Orden, IVA = _IVA, 
				InfoPlantOC = _InfoPlantOC, InfoGasRec = _InfoGasRec,
				ID_Clasificacion = _ID_Clasificacion, Status = _Status,
				Recepcion = _Recepcion, Fmt_Recepcion = _Fmt_Recepcion, TipoCobro = _TipoCobro
		WHERE ID_EntidadCompra = _ID_EntidadCompra;
		
		-- INSERTA LOS ENLACES
		INSERT INTO TBL_BANCOS_VS_COMPRAS
		SELECT Tipo, Clave, _ID_EntidadCompra
		FROM _TMP_BANCOS_VS_COMPRAS;

	END IF;

	RETURN QUERY SELECT _err, _result, _ID_EntidadCompra::varchar;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_entidades_cambiar(smallint, smallint, character varying, integer, character varying, character varying, smallint, bit, bit, integer, integer, character varying, character varying, numeric, smallint, smallint, character varying, character, integer, character varying, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
INSERT INTO tbl_variables(id_variable, descripcion, ventero, vdecimal, vfecha, valfanumerico, desistema, modulo)
VALUES('CC_VENNP','CC|-|-|-|-', null, null, null, '', '1','VEN');

--@FIN_BLOQUE
INSERT INTO tbl_variables(id_variable, descripcion, ventero, vdecimal, vfecha, valfanumerico, desistema, modulo)
VALUES('CC_COMPNP','CC|-|-|-|-', null, null, null, '', '1','COMP');

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ventas_facturas_agregar(
    _id_entidadventa smallint,
    _numero integer,
    _id_cliente integer,
    _fecha timestamp without time zone,
    _referencia character varying,
    _moneda smallint,
    _tc numeric,
    _condicion smallint,
    _obs character varying,
    _importe numeric,
    _descuento numeric,
    _subtotal numeric,
    _iva numeric,
    _total numeric,
    _fsipg_efectivo numeric,
    _fsipg_bancos numeric,
    _fsipg_cambio numeric,
    _id_bodega smallint,
    _id_enlace integer,
    _id_vendedor smallint,
    _tipoenlace character,
    _uuid character,
    _ieps numeric,
    _ivaret numeric,
    _isrret numeric)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_Factura int; _mes smallint; _ano smallint;	_errpart int;  _resultpart varchar(255);  _ID_Movimiento int;  _concepto varchar(80); _conceptocost varchar(80); _Ref varchar(25);  _claseref varchar(25); 
	_FijaCost bit; _costven numeric(19,4); _CC_COSTVEN char(19); _AuditarAlm bit;
	_ID_CXC int; _numpol int; _clase varchar(1024); _bancajmov int;
	_nombreCli varchar(120); _Cantidad numeric(19,4); _ServComp bit; _CC_CLI char(19); 
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_VENNP char(19); _CC_DCAF char(19); 
	_CC_DCEC char(19); _diff numeric(19,4); _TotDebe numeric(19,4); _TotHaber numeric(19,4); _contPart smallint; 
	_totPart smallint; _CC_VEN char(19); _CC_VENCONT char(19); _DesgloseCLIENT bit;
	 _contban smallint; _totalban smallint; _ID_FormaPago smallint; _ID_BanCaj smallint; _tipoDeposito character(3); _RefPago varchar(50); _IdMon smallint; _RemisionAsociada bit; 
	_id_clasificacion varchar(10); _numPagos smallint; _RefExt int; _ID_CFD int; _TFD smallint;  
	_Beneficiario varchar(80); _RFCOrigen varchar(15); _moneda_ce character(3); /*_CuentaOrigen varchar(80); _BancoOrigen character(3); _CuentaDestino varchar(50); _BancoDestino character(3);*/
	_DocFinal varchar(50); _ID_SatBanco character(3); _RFC varchar(15);	
	--Iteracion
	_REC_TMP_PAGOS RECORD; _REC_TMP_VENTAS_FACTURAS_DET RECORD; _TotalPesosMult numeric(19,4); _ParcialPesosMult numeric(19,4); _BanCaj smallint; _CC_BAN char(19);
BEGIN	
	_err := 0;
	_result := 'La factura se ha registrado satisfactoriamente';
	--_ServComp := '1';
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPP');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPPPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSV');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSVPN');
	_CC_IVARet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVARETV');
	_CC_ISRRet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_ISRRETV');
	_CC_Desc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSV');
	_moneda_ce := (select id_satmoneda from TBL_CONT_MONEDAS where Clave = _moneda);
	_Cantidad := round((_Total * _TC), 2);
	_ImporteTotalPesos := ROUND((_Importe * _TC), 2);
	_IVAPesos :=  ROUND((_IVA * _TC), 2);
	_IEPSPesos :=  ROUND((_IEPS * _TC), 2);
	_IVARetPesos :=  ROUND((_IVARet * _TC), 2);
	_ISRRetPesos :=  ROUND((_ISRRet * _TC), 2);
	_DescPesos := ROUND((_Descuento * _TC), 2);
	_TotalPesos := ROUND((_Total * _TC), 2);	
	_concepto := 'Venta ' || (select Descripcion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_conceptocost := 'Costo ' || (select Descripcion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);

	_Beneficiario := case when _ID_Cliente = 0 then 'Al Portador' else ( select VAlfanumerico from TBL_VARIABLES where ID_Variable = 'EMPRESA') end;
	_RFCOrigen := case when _ID_Cliente = 0 then 'XAXX010101000' else ( select RFC from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) end;
	--_CuentaOrigen := case when _ID_Cliente = 0 then '' else ( select MetodoDePago from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) end;
	--_BancoOrigen := case when _ID_Cliente = 0 then '000' else ( select ID_SatBanco from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) end;

	_CC_CLI := (select ID_CC from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente);
	_DesgloseCLIENT := (select DesgloseCLIENT from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_Fija := (select Fija from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_FijaCost := (select FijaCost from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_AuditarAlm := ( select AuditarAlm from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega );
	_RemisionAsociada := ( case when _ID_Enlace is not null and _TipoEnlace = 'VREM' then '1' else '0' end );
	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_id_clasificacion := (select ID_Clasificacion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_numPagos = (select count(*) from _TMP_PAGOS);

	IF _ID_Enlace is not null
	THEN
		_claseref := _TipoEnlace || '|' || cast(_ID_Enlace as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';
	END IF;
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;

	IF (select count(*) from TBL_VENTAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadVenta and Numero = _Numero) > 0
	THEN
		_err := 3;
		_result := 'ERROR: La clave de la factura ya existe, No se puede duplicar';
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF _Condicion = 0
	THEN
		FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
		LOOP
			_ID_FormaPago := _REC_TMP_PAGOS.ID_FormaPago;
			_ID_BanCaj := _REC_TMP_PAGOS.ID_BanCaj;
			_FijaBAN := (case when _ID_FormaPago = 1 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			_IdMon := (case when _ID_FormaPago = 1 then (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			
			IF _Fija <> _FijaBAN
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la venta porque al pagarse de contado requiere que los bancos o cajas manejen los mismos trazos contables que la venta';
				EXIT;
			END IF;

			IF _Moneda <> _IdMon 
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la venta porque al pagarse de contado requiere que los bancos o cajas manejen la misma moneda que la venta';
				EXIT;
			END IF;

			/*IF _ID_Cliente = 0 and _Fija = '0' and _ID_FormaPago > 1
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar una venta directa de mostrador pagada de contado con transferencia, porque no existe soporte para contabilidad electrónica, selecciona un cliente o una entidad Fija';
				EXIT;
			END IF;*/
		END LOOP;
	ELSIF _Condicion = 1 -- Es a crédito, por lo tanto revisa si el total de esta factura mas el total de la deuda del cliente rebasa el límite de crédito, de ser asi, rechazará la venta
	THEN
		IF(select Dias from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) > 0 -- este cliente tiene crédito
		THEN
			IF (select LimiteCredito from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) > 0 AND 
				((_ImporteTotalPesos + getcliprosldtotmn('CL', _ID_Cliente)) > (select LimiteCredito from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente))
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la factura a crédito porque la suma de las deudas de este cliente, más esta factura, sobrepasa su límite de crédito';
			END IF;
		ELSE -- no tiene crédito
			_err := 3;
			_result := 'ERROR: No se puede agregar la factura porque al cobrarse a crédito requiere que se le brinde al cliente por lo menos un día de crédito';
		END IF;	
	END IF;
	
	IF _IVA > 0.0 AND ( _CC_IVA = '' OR _CC_IVAPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva por pagar efectivamente cobrado o la de iva por pagar pendiente de cobrar, no existe o no se ha enlazado';
	END IF;
	IF _IEPS > 0.0 AND ( _CC_IEPS = '' OR _CC_IEPSPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente cobrado o la de IEPS pendiente de cobrar no existe o no se ha enlazado';
	END IF;
	IF _IVARet > 0.0 AND ( _CC_IVARet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de IVA Retenido no existe o no se ha enlazado';
	END IF;
	IF _ISRRet > 0.0 AND ( _CC_ISRRet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de ISR Retenido no existe o no se ha enlazado';
	END IF;
	IF _Descuento > 0.0 AND _CC_Desc = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de descuento sobre ventas no existe o no se ha enlazado';
	END IF;
	
	-- procede a realizar verificaci?n sobre los trazos
	IF _FijaCost = '0'
	THEN
		_CC_COSTVEN := (select cc from TBL_INVSERV_COSTOS_CONCEPTOS where ID_Concepto = 50);
		IF _CC_COSTVEN is null
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para el costo de ventas no existe o no se ha enlazado';	
		END IF;
	END IF;

	IF _Condicion = 0
	THEN
		_CC_VENCONT := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VENCONT');
		IF _CC_VENCONT = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para ventas de contado no existe o no se ha enlazado';	
		END IF;
	ELSE
		_CC_VEN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VEN');
		IF _CC_VEN = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para ventas a crédito no existe o no se ha enlazado';	
		END IF;
	END IF;

	IF _Condicion = 3
	THEN
		_CC_VENNP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VENNP');
		IF _CC_VENNP = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para registro de partida doble cuando no se establece ningun metodo de pago, no existe o no se ha enlazado';
		END IF;
	END IF;

	--VERIFICA SI ES CFD
	IF (select CFD from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta) <> '00'
	THEN
		IF _uuid <> '' -- Es emisor directo de CFDI, por lo tanto no puede enlazar un CFDI Externo
		THEN
			_err := 3;
			_result := 'ERROR: No se puede enlazar ningun CFDI porque esta entidad de venta genera sus propios CFDIs';
		ELSE
			IF (select count(*) from TBL_CFD_REPORTE_MENSUAL where Mes = _mes and Ano = _ano and Cerrado = '0') > 0
			THEN
				IF (select count(*) from TBL_VENTAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadVenta and Numero = (_Numero -1)) is not null
				THEN
					IF (select TFD from TBL_VENTAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadVenta and Numero = (_Numero -1)) is null
							or (select TFD from TBL_VENTAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadVenta and Numero = (_Numero -1)) <> 3
					THEN 
						_err := 3;
						_result := 'ERROR: No se puede agregar la factura porque la factura anterior no est&aacute; sellada. Primero debes sellar la factura anterior para poder agregar esta factura';
					END IF;
				END IF;
			ELSE
				_err := 3;
				_result := 'ERROR: No se puede agregar la factura porque el mes de comprobantes fiscales digitaya';
			END IF;
		END IF;
	ELSE -- Si no es Emisor directo de CFDI comprueba si es carga con CFDI externo
		IF _uuid <> ''
		THEN
			IF (select count(*) from TBL_CFDVEN where UUID = _uuid) > 0
			THEN
				_ID_CFD := (select ID_CFD from TBL_CFDVEN where UUID = _uuid) ;
				_TFD := 3;
			ELSE
				_err := 3;
				_result := 'ERROR: No se puede enlazar el CFDI al registro, porque no existe el UUID proporcionado';
			END IF;
		END IF;
	END IF;

	IF _err = 0
	THEN
		INSERT INTO TBL_VENTAS_FACTURAS_CAB (  id_entidad, numero, id_clipro, fecha, referencia, status, moneda, tc, fechaenvio, condicion, obs, 
			importe, descuento, subtotal, iva, total, ref, id_pol, id_polcost, id_bodega, mimporte, mdescuento, msubtotal, miva, mtotal, efectivo, bancos, cambio, id_vendedor, id_cfd, tfd, 
			ieps, ivaret, isrret )
		VALUES(_ID_EntidadVenta, _Numero, _ID_Cliente, _Fecha, _Referencia, (case when _RemisionAsociada = '0' then (case when _AuditarAlm = '1' then 'G' else 'E' end) else 'E' end), _Moneda, _TC, _Fecha, _Condicion, _Obs,
				_Importe, _Descuento, _SubTotal, _IVA, _Total, _claseref, null, null, _ID_Bodega, _Importe, _Descuento, _SubTotal, _IVA,  _Total, _FSIPG_Efectivo, _FSIPG_Bancos, _FSIPG_Cambio, _ID_Vendedor, _ID_CFD, _TFD,
				_ieps, _ivaret, _isrret)
		 RETURNING currval(pg_get_serial_sequence('TBL_VENTAS_FACTURAS_CAB', 'id_vc')) INTO _id_factura;
		 
		-- actualiza el numero de FACTURA
		UPDATE TBL_VENTAS_ENTIDADES
		SET Doc = _Numero + 1
		WHERE ID_EntidadVenta = _ID_EntidadVenta;
		
		-- inserta el detalle
		INSERT INTO TBL_VENTAS_FACTURAS_DET
		SELECT _ID_Factura, Partida, Cantidad, ID_Prod, Precio, Descuento, IVA, Obs, Importe, ImporteDesc, ImporteIVA, TotalPart, IEPS, ImporteIEPS, IVARet, ImporteIVARet, ISRRet, ImporteISRRet
		FROM _TMP_VENTAS_FACTURAS_DET;

		_Ref := 'VFAC|' || cast(_ID_Factura as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';

		-- Actualiza el status de la cotizacion, pedido o remision ligando el ID de la FACTURA
		IF _ID_Enlace is not null
		THEN
			IF _TipoEnlace = 'VPED'
			THEN
				UPDATE TBL_VENTAS_PEDIDOS_CAB
				SET Status = 'F', ID_Factura = _ID_Factura, TipoEnlace = 'VFAC'
				WHERE ID_VC = _ID_Enlace;
			ELSIF _TipoEnlace = 'VCOT'
			THEN
				UPDATE TBL_VENTAS_COTIZACIONES_CAB
				SET Status = 'F', ID_Factura = _ID_Factura, TipoEnlace = 'VFAC'
				WHERE ID_VC = _ID_Enlace;
			ELSE
				UPDATE TBL_VENTAS_REMISIONES_CAB
				SET ID_Factura = _ID_Factura
				WHERE ID_VC = _ID_Enlace;
			END IF;
		END IF;

		IF _uuid <> ''
		THEN
			UPDATE TBL_CFDVEN
			SET FSI_Tipo = 'FAC', FSI_ID = _ID_Factura
			WHERE ID_CFD = _ID_CFD;
			
			UPDATE TBL_CFD
			SET FSI_Tipo = 'FAC', FSI_ID = _ID_Factura
			WHERE ID_CFD = _ID_CFD;
		END IF;
		
		--Si no es de mostrador, actualiza los ultimos precios de clientes
		IF _ID_Cliente > 0
		THEN
			FOR _REC_TMP_VENTAS_FACTURAS_DET IN ( select * from _TMP_VENTAS_FACTURAS_DET order by Partida asc ) 
			LOOP
				-- Procede a agregar el ultimo costo del producto o servicio correspondiente al Cliente ( si existe lo actualiza de lo contrario lo inserta )
				if(select count(*) from TBL_VENTAS_VS_INVENTARIO where ID_Tipo = 'CL' and ID_Cliente = _ID_Cliente and ID_Prod = _REC_TMP_VENTAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda) = 0
				then
					insert into TBL_VENTAS_VS_INVENTARIO
					values('CL', _ID_Cliente,  _REC_TMP_VENTAS_FACTURAS_DET.ID_Prod, _Moneda, _Fecha,  _REC_TMP_VENTAS_FACTURAS_DET.Precio,  _REC_TMP_VENTAS_FACTURAS_DET.Descuento);
				else
					update TBL_VENTAS_VS_INVENTARIO
					set Fecha = _Fecha, Precio =  _REC_TMP_VENTAS_FACTURAS_DET.Precio, Descuento =  _REC_TMP_VENTAS_FACTURAS_DET.Descuento
					where ID_Tipo = 'CL' and ID_Cliente = _ID_Cliente and ID_Prod =   _REC_TMP_VENTAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda;
				end if;
			END LOOP; 
		END IF; 

		--Crea tabla temporal indispensable... para contable.
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_TMP (
			Part smallint NOT NULL ,
			Cuenta char(19) NOT NULL ,
			Concepto varchar(80) NOT NULL ,
			Parcial numeric(19,4) NOT NULL ,
			Moneda smallint NOT NULL ,
			TC numeric(19,4) NOT NULL ,
			Debe numeric(19,4) NOT NULL ,
			Haber numeric(19,4) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP( 
			id_part smallint NOT NULL,
			num character varying(20) NOT NULL,
			banco character(3) NOT NULL,
			ctaori character varying(50) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			monto numeric(19,4) NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			banemisext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		); 
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP ( 
			id_part smallint NOT NULL,
			ctaori character varying(50) NOT NULL,
			bancoori character(3) NOT NULL,
			monto numeric(19,4) NOT NULL,
			ctadest character varying(50) NOT NULL,
			bancodest character(3) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			bancooriext character varying(150) NOT NULL,
			bancodestext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP (
			id_part smallint NOT NULL,
			metpagopol character(2) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			monto numeric(19,4) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP ( 
			id_part smallint NOT NULL, 
			uuid_cfdi character(36) NOT NULL, 
			monto numeric(19,4) NOT NULL, 
			rfc character varying(13) NOT NULL,
			id_tipo character varying(10) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL,
			cfd_cbb_serie character varying(10) NOT NULL,
			cfd_cbb_numfol integer NOT NULL,
			numfactext character varying(36) NOT NULL,
			taxid character varying(30) NOT NULL
		); 
		-- Agrega a la tabla de temporal para: polizas final o detalles de bancos cajas final
		_contPart := 1;
		IF _Condicion = 0 
		THEN
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_VENCONT, 'Importe de la venta', _Importe, _Moneda, _TC, 0.0, _ImporteTotalPesos);
		ELSE
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_VEN, 'Importe de la venta', _Importe, _Moneda, _TC, 0.0, _ImporteTotalPesos);
		END IF;

		IF _uuid <> ''
		THEN
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP
			VALUES(_contPart, _uuid, _Total, _RFCOrigen, 'CompNal', _moneda_ce, _TC, '', 0, '', '');
		END IF;

		-- procede a registrar los otros conceptos de la poliza ( EL IVA , Deuda con Cliente ETC)
		IF _IVA > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a iva efectivamente cobrado
			THEN
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVA, 'Impuesto de la venta', _IVA, _Moneda, _TC, 0.0, _IVAPesos);	
			ELSE -- de credito o sin pago... envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVAPN, 'Impuesto de la venta', _IVA, _Moneda, _TC, 0.0, _IVAPesos);	
			END IF;
		END IF;

		IF _IEPS > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a ieps efectivamente cobrado
			THEN
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, 0.0, _IEPSPesos);	
			ELSE -- de credito o sin pago... envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPSPN, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, 0.0, _IEPSPesos);	
			END IF;
		END IF;

		IF _IVARet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IVARet, 'Retención de IVA', _IVARet, _Moneda, _TC, _IVARetPesos, 0.0);	
		END IF;

		IF _ISRRet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_ISRRet, 'Retención de ISR', _ISRRet, _Moneda, _TC, _ISRRetPesos, 0.0);	
		END IF;

		IF _Descuento > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_Desc, 'Descuento explicito', _Descuento, _Moneda, _TC, _DescPesos, 0.0);
		END IF;
		
		-- procede a registrar los movimiento en la caja, banco, Cuenta por cobrar, y la poliza en caso necesario
		IF _ID_Cliente > 0 and _Condicion = 1 -- si es a credito y no de mostrador
		THEN
			SELECT * INTO _errpart, _resultpart, _id_cxc 
			FROM sp_client_cxc_alta(_ID_EntidadVenta, _Fecha, 'CL', _ID_Cliente, _Concepto, _Moneda, _TC, _Total, _Cantidad, '0', 'VFAC', _ID_Factura, _Ref) as ( err integer, res varchar, clave integer ); --este movimiento no registrara poliza porque el concepto 0 de cxc es de sistema 
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			ELSE
				_clase = 'VCXC|' || cast(_ID_CXC as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';

				UPDATE TBL_VENTAS_FACTURAS_CAB
				SET ID_Pol = _ID_CXC
				WHERE ID_VC = _ID_Factura;
				
				-- Procede a registrar la poliza si y solo si es una entidad dinamica
				-- Primero registra la deuda total del cliente para la partida doble en tmp
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_CLI, 'Deuda total del Cliente', _Total, _Moneda, _TC, _TotalPesos, 0.0);
					
				IF _Fija = '0'
				THEN
					-- Primero registra y crea la tabla temporal de detalle de la poliza
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
						Part smallint NOT NULL ,
						Cuenta char(19) NOT NULL ,
						Concepto varchar(80) NOT NULL ,
						Parcial numeric(19,4) NOT NULL ,
						Moneda smallint NOT NULL ,
						TC numeric(19,4) NOT NULL ,
						Debe numeric(19,4) NOT NULL ,
						Haber numeric(19,4) NOT NULL
					);
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
						id_part smallint NOT NULL, 
						uuid_cfdi character(36) NOT NULL, 
						monto numeric(19,4) NOT NULL, 
						rfc character varying(13) NOT NULL,
						id_tipo character varying(10) NOT NULL,
						moneda character(3) NOT NULL,
						tipcamb numeric(19,5) NOT NULL,
						cfd_cbb_serie character varying(10) NOT NULL,
						cfd_cbb_numfol integer NOT NULL,
						numfactext character varying(36) NOT NULL,
						taxid character varying(30) NOT NULL
 					); 
					-- Fin de la tabla temporal

					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
					_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					IF _TotDebe > _TotHaber
					THEN
						_diff := _TotDebe - _TotHaber;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
					ELSIF _TotDebe < _TotHaber
					THEN
						_diff := _TotHaber - _TotDebe;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
					END IF;
					
					-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
					-- Agrega ahora la poliza principal
					--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
					SELECT * INTO _errpart, _resultpart, _numpol 
					FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
					ELSE
						UPDATE  TBL_CLIENT_CXC
						SET ID_Pol = _numpol
						WHERE ID_CP = _ID_CXC;
					END IF;

					DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
					DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
				END IF;
			END IF; 
		ELSIF _Condicion = 0 -- es de contado
		THEN
			-- Termina la poliza dividiendo los pagos en sus cuentas de banco o caja
			_TotalPesosMult = 0.0;
			_ParcialPesosMult = 0.0;
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_ParcialPesosMult := round((_REC_TMP_PAGOS.Total * _TC), 2);
				_TotalPesosMult := _TotalPesosMult + _ParcialPesosMult;
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				_CC_BAN := (select CC from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_CuentaDestino := (select Descripcion from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_BancoDestino := (select ID_SatBanco from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				_contPart := _contPart + 1;
								
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_BAN, _REC_TMP_PAGOS.RefPago, _REC_TMP_PAGOS.Total, _Moneda, _TC, _ParcialPesosMult, 0.0);

				--Ingresa contablilidad electronica del pago
				IF _BanCaj = 0 -- Es banco
				THEN
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSIF _REC_TMP_PAGOS.ID_SatMetodosPago = '03' --Es una transferencia
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP
						SELECT _contPart, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.Total, Descripcion, ID_SatBanco, _Fecha,  _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.BancoExt, BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				ELSE -- BanCaj = 1 "Caja"
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque (Deposito o Retiro es lo mismo, toma los datos directos capturados en el dialogo)
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES ( 
					id_part smallint NOT NULL,
					num character varying(20) NOT NULL,
					banco character(3) NOT NULL,
					ctaori character varying(50) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					monto numeric(19,4) NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					banemisext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				); 
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS ( 
					id_part smallint NOT NULL,
					ctaori character varying(50) NOT NULL,
					bancoori character(3) NOT NULL,
					monto numeric(19,4) NOT NULL,
					ctadest character varying(50) NOT NULL,
					bancodest character(3) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					bancooriext character varying(150) NOT NULL,
					bancodestext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO (
					id_part smallint NOT NULL,
					metpagopol character(2) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					monto numeric(19,4) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL 
				); 
				-- Fin de la tabla temporal

				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;

				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
				-- Agrega ahora la poliza principal
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('IG', _Fecha, _Concepto,'0', '', _TotalPesos, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				END IF;
				
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES; 
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
			END IF;

			--Ahora ejecuta los movimientos de caja y bancos
			_clase := '';
			IF _err = 0
			THEN
				FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
				LOOP
					_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos

					_Beneficiario := ( select VAlfanumerico from TBL_VARIABLES where ID_Variable = 'EMPRESA');
					_RFC := _RFCOrigen;
					
					SELECT * INTO _errpart, _resultpart, _bancajmov 
					FROM  sp_bancos_movs_agregar( _BanCaj, _REC_TMP_PAGOS.ID_BanCaj, _Fecha, _Concepto, _Beneficiario, _REC_TMP_PAGOS.Total, 0.00, _REC_TMP_PAGOS.TipoMov, 
						'G', _Moneda, _TC, _Ref, _REC_TMP_PAGOS.RefPago, _id_clasificacion, _numpol, _REC_TMP_PAGOS.ID_SatBanco, _RFC, _REC_TMP_PAGOS.ID_SatMetodosPago, 
																		_REC_TMP_PAGOS.BancoExt, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.Cheque) as ( err integer, res varchar, clave integer);
					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
						EXIT;
					END IF;

					INSERT INTO TBL_VENTAS_FACTURAS_PAGOS
					VALUES(_ID_Factura, _bancajmov);
					
					IF _numPagos > 1
					THEN
						IF _BanCaj = 0
						THEN
							_clase := _clase || 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
						ELSE
							_clase := _clase || 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
						END IF;
					ELSE
						IF _BanCaj = 0
						THEN
							_clase := 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
						ELSE
							_clase := 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
						END IF;
					END IF;
				END LOOP;
			END IF;
			
			IF _Fija = '0' -- Aqui registra la referencia de las polizas de ingreso a sus movimientos de caja o bancos
			THEN
				IF _numPagos > 1
				THEN
					INSERT INTO TBL_REFERENCIAS_EXT
					VALUES(default,_clase)
					RETURNING currval(pg_get_serial_sequence(' TBL_REFERENCIAS_EXT', 'id_ref')) INTO _RefExt;
					UPDATE TBL_CONT_POLIZAS
					SET Ref = 'REXT|' || cast(_RefExt as varchar) || '|||'
					WHERE ID = _numpol;
		 		ELSE
					UPDATE TBL_CONT_POLIZAS
					SET Ref = _clase
					WHERE ID = _numpol;
				END IF;
			END IF; 
		ELSIF _Condicion = 3 -- Es de ningun pago
		THEN
			_clase = 'VFAC|' || cast(_id_factura as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';

			-- Procede a registrar la poliza si y solo si es una entidad dinamica
			-- Primero registra la deuda total en CC_VENNP
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_VENNP, 'Documento por cobrar', _Total, _Moneda, _TC, _TotalPesos, 0.0);
					
			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL
 				); 
				-- Fin de la tabla temporal

				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;
					
				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;

				-- Agrega ahora la poliza principal
				--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					UPDATE TBL_VENTAS_FACTURAS_CAB
					SET ID_Pol = _numpol
					WHERE ID_VC = _ID_Factura;
				END IF;
				
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
				
			END IF;
			
		END IF;
		
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP; 
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;

		--Procede a agregar el movimiento al almacén
		IF _err = 0 AND _FijaCost = '0' AND _RemisionAsociada = '0' and (select count(*) from _TMP_VENTAS_FACTURAS_DET where Tipo = 'P') > 0
		THEN
			CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (
				ID_Bodega smallint NOT NULL ,
				ID_Prod varchar(20) NOT NULL ,
				Partida smallint NOT NULL ,
				Cantidad numeric(9, 3) NOT NULL ,
				Costo numeric(19,4) NULL 
			); 

			insert into _TMP_INVSERV_ALMACEN_MOVIM_DET
			select _ID_Bodega, ID_Prod, Partida, Cantidad, Precio --Precio no es el costo, sin embargo lo ignorará en la tarjeta de almacen y se calculará el costo porque el concepto de venta 50, indica recalcular costo
			from _TMP_VENTAS_FACTURAS_DET
			where Tipo = 'P'
			order by Partida ASC;
		
			SELECT * INTO _errpart, _resultpart, _ID_Movimiento 
			FROM sp_invserv_alm_movs_agregar(_Fecha, _ID_Bodega, (case when _AuditarAlm = '1' then 'P' else 'U' end), '50', _ConceptoCost, '',/*1 ENT 2 SAL*/ '2', _Ref, 'VFAC', _ID_Factura) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				UPDATE TBL_VENTAS_FACTURAS_CAB
				SET ID_PolCost = _ID_Movimiento
				WHERE ID_VC = _ID_Factura;
			END IF;
	
			DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET; 
		END IF;
		-- Fin del movimiento al almacen
		
	END IF; 
	
	RETURN QUERY SELECT _err, _result, _id_factura;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_ventas_facturas_agregar(smallint, integer, integer, timestamp without time zone, character varying, smallint, numeric, smallint, character varying, numeric, numeric, numeric, numeric, numeric, numeric, numeric, numeric, smallint, integer, smallint, character, character, numeric, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION view_compras_entidades_ids(
    _id_usuario character varying,
    _entidad character varying)
  RETURNS SETOF record AS
$BODY$  
BEGIN
	IF _ID_Usuario = 'cef-su' OR (select count(*) from tbl_usuarios_submodulo_roles where id_usuario = _id_usuario and id_rol = 'cef-comp') > 0
	THEN
		IF _Entidad = 'CEF-X' -- Regresa todo el set
		THEN
			RETURN QUERY
			SELECT _id_usuario as id_usuario, e.ID_EntidadCompra AS id_entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega
			ORDER BY e.ID_EntidadCompra ASC;
			
		ELSIF _Entidad = 'CEF-1'
		THEN
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, e.ID_EntidadCompra AS id_entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega
			ORDER BY e.ID_EntidadCompra ASC
			LIMIT 1;
		ELSE
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, e.ID_EntidadCompra AS id_entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega
			WHERE e.ID_EntidadCompra = _Entidad::smallint;
		END IF;
	ELSE
		IF _Entidad = 'CEF-X' -- Regresa todo el set
		THEN
			RETURN QUERY 
			SELECT DISTINCT _ID_Usuario as ID_Usuario, e.ID_EntidadCompra AS ID_Entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega INNER JOIN
				TBL_USUARIOS_SUBMODULO_COMPRAS u ON e.ID_EntidadCompra = u.ID_EntidadCompra
			WHERE (u.id_usuario = _ID_Usuario OR u.id_usuario IN
				(select sr.ID_Rol from tbl_usuarios_submodulo_roles sr where sr.id_usuario = _ID_Usuario))
			ORDER BY e.ID_EntidadCompra ASC;
			
		ELSIF _Entidad = 'CEF-1'
		THEN
			RETURN QUERY 
			SELECT DISTINCT _ID_Usuario as ID_Usuario, e.ID_EntidadCompra AS ID_Entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega INNER JOIN
				TBL_USUARIOS_SUBMODULO_COMPRAS u ON e.ID_EntidadCompra = u.ID_EntidadCompra
			WHERE (u.id_usuario = _ID_Usuario OR u.id_usuario IN
				(select sr.ID_Rol from tbl_usuarios_submodulo_roles sr where sr.id_usuario = _ID_Usuario))
			ORDER BY e.ID_EntidadCompra ASC
			LIMIT 1;

		ELSE
			RETURN QUERY
			SELECT DISTINCT _ID_Usuario as ID_Usuario, e.ID_EntidadCompra AS ID_Entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega INNER JOIN
				TBL_USUARIOS_SUBMODULO_COMPRAS u ON e.ID_EntidadCompra = u.ID_EntidadCompra
			WHERE (u.id_usuario = _ID_Usuario OR u.id_usuario IN
				(select sr.ID_Rol from tbl_usuarios_submodulo_roles sr where sr.id_usuario = _ID_Usuario)) AND e.ID_EntidadCompra = _Entidad::smallint;
			
		END IF;
	END IF;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION view_compras_entidades_ids(character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION view_compras_entidades_ids(
    _id_usuario character varying,
    _entidad character varying,
    _tipo smallint)
  RETURNS SETOF record AS
$BODY$  
BEGIN
	IF _ID_Usuario = 'cef-su' OR (select count(*) from tbl_usuarios_submodulo_roles where id_usuario = _id_usuario and id_rol = 'cef-comp') > 0
	THEN
		IF _Entidad = 'CEF-X' -- Regresa todo el set
		THEN
			RETURN QUERY
			SELECT _id_usuario as id_usuario, e.ID_EntidadCompra AS id_entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega
			WHERE e.ID_TipoEntidad = _tipo
			ORDER BY e.ID_EntidadCompra ASC;
			
		ELSIF _Entidad = 'CEF-1'
		THEN
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, e.ID_EntidadCompra AS id_entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega
			WHERE e.ID_TipoEntidad = _tipo
			ORDER BY e.ID_EntidadCompra ASC
			LIMIT 1;
		ELSE
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, e.ID_EntidadCompra AS id_entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega
			WHERE e.ID_TipoEntidad = _tipo and e.ID_EntidadCompra = _Entidad::smallint;
		END IF;
	ELSE
		IF _Entidad = 'CEF-X' -- Regresa todo el set
		THEN
			RETURN QUERY 
			SELECT DISTINCT _ID_Usuario as ID_Usuario, e.ID_EntidadCompra AS ID_Entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega INNER JOIN
				TBL_USUARIOS_SUBMODULO_COMPRAS u ON e.ID_EntidadCompra = u.ID_EntidadCompra
			WHERE e.ID_TipoEntidad = _tipo and (u.id_usuario = _ID_Usuario OR u.id_usuario IN
				(select sr.ID_Rol from tbl_usuarios_submodulo_roles sr where sr.id_usuario = _ID_Usuario))
			ORDER BY e.ID_EntidadCompra ASC;
			
		ELSIF _Entidad = 'CEF-1'
		THEN
			RETURN QUERY 
			SELECT DISTINCT _ID_Usuario as ID_Usuario, e.ID_EntidadCompra AS ID_Entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega INNER JOIN
				TBL_USUARIOS_SUBMODULO_COMPRAS u ON e.ID_EntidadCompra = u.ID_EntidadCompra
			WHERE e.ID_TipoEntidad = _tipo and (u.id_usuario = _ID_Usuario OR u.id_usuario IN
				(select sr.ID_Rol from tbl_usuarios_submodulo_roles sr where sr.id_usuario = _ID_Usuario))
			ORDER BY e.ID_EntidadCompra ASC
			LIMIT 1;

		ELSE
			RETURN QUERY
			SELECT DISTINCT _ID_Usuario as ID_Usuario, e.ID_EntidadCompra AS ID_Entidad, e.ID_TipoEntidad AS ID_Tipo, e.Serie, e.Descripcion, e.Doc, e.Formato, e.ID_Bodega, 
				b.Nombre AS Bodega, b.AuditarAlm, b.ManejoStocks, e.Orden, e.Devolucion, e.IVA, e.Fmt_Orden, e.Fmt_Devolucion, e.Fija, e.FijaCost, e.Recepcion, e.Fmt_Recepcion, e.TipoCobro
			FROM TBL_COMPRAS_ENTIDADES e INNER JOIN 
				TBL_INVSERV_BODEGAS b ON b.ID_Bodega = e.ID_Bodega INNER JOIN
				TBL_USUARIOS_SUBMODULO_COMPRAS u ON e.ID_EntidadCompra = u.ID_EntidadCompra
			WHERE e.ID_TipoEntidad = _tipo and (u.id_usuario = _ID_Usuario OR u.id_usuario IN
				(select sr.ID_Rol from tbl_usuarios_submodulo_roles sr where sr.id_usuario = _ID_Usuario)) AND e.ID_EntidadCompra = _Entidad::smallint;
			
		END IF;
	END IF;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION view_compras_entidades_ids(character varying, character varying, smallint)
  OWNER TO [[owner]];
    
--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_compras_gastos_agregar(
    _id_entidadcompra smallint,
    _numero integer,
    _id_proveedor integer,
    _fecha timestamp without time zone,
    _referencia character varying,
    _moneda smallint,
    _tc numeric,
    _condicion smallint,
    _obs character varying,
    _importe numeric,
    _descuento numeric,
    _subtotal numeric,
    _iva numeric,
    _total numeric,
    _fsipg_efectivo numeric,
    _fsipg_bancos numeric,
    _fsipg_cambio numeric,
    _id_bodega smallint,
    _id_enlace integer,
    _id_vendedor smallint,
    _tipoenlace character,
    _uuid character varying,
    _ieps numeric,
    _ivaret numeric,
    _isrret numeric)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_Gasto int; _mes smallint; _ano smallint;	_errpart int;  _resultpart varchar(255);  _ID_Movimiento int;  _concepto varchar(80); _descripcion varchar(80); _Ref varchar(25); 
	_FijaCost bit; _ID_CXP int; _numpol int; _clase varchar(1024); _bancajmov int;
	_nombrePro varchar(120); _Cantidad numeric(19,4); _ServComp bit; _CC_PRO char(19); _CC_GND char(19); _GASNoDed numeric(19,4);  _GASDed numeric(19,4); _GASNoDedParcial numeric(19,4); _GASDedParcial numeric(19,4); _TOTALNoDed numeric(19,4);  
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  _IVANoDed numeric(19,6); _IVA_Deducible numeric(19,6);
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_COMPNP char(19); _CC_DCAF char(19);
	_CC_DCEC char(19); _diff numeric(19,4); _TotDebe numeric(19,4); _TotHaber numeric(19,4); _contPart smallint; 
	_totPart smallint; _Beneficiario varchar(80);
	_contban smallint; _totalban smallint; _ID_FormaPago smallint; _ID_BanCaj smallint; _RefPago varchar(25); _banCHQ varchar(20); _IdMon smallint; 
	_id_clasificacion varchar(10); _numPagos smallint; _RefExt int; _ID_CFD int; _TFD smallint; 
	_RFC varchar(15); _RFCBeneficiario varchar(15); _moneda_ce character(3);  /*_CuentaBeneficiario varchar(80); _BancoBeneficiario character(3); _CuentaOrigen varchar(50); _BancoOrigen character(3);*/ _ContUUIDs smallint; _NumUUIDs smallint; _uuidx character(36);
	--Iteracion
	_REC_TMP_PAGOS RECORD; _REC_TMP_COMPRAS_GASTOS_DET RECORD; _REC_TMP_INVSERV_GASTOS_PORCENTAJES RECORD; _TotalPesosMult numeric(19,4); _ParcialPesosMult numeric(19,4); _BanCaj smallint; _CC_BAN char(19);
BEGIN	
	_err := 0;
	_result := 'El gasto se ha registrado satisfactoriamente';
	--_ServComp := '1';
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAAC');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAACPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSC');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSCPN');
	_CC_IVARet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVARETC');
	_CC_ISRRet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_ISRRETC');
	_CC_Desc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSC');
	_CC_GND = (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_GND');
	_moneda_ce := (select id_satmoneda from TBL_CONT_MONEDAS where Clave = _moneda);
	_Cantidad := round((_Total * _TC), 2);
	_ImporteTotalPesos := ROUND((_Importe * _TC), 2);
	_IVAPesos :=  ROUND((_IVA * _TC), 2);
	_IEPSPesos :=  ROUND((_IEPS * _TC), 2);
	_IVARetPesos :=  ROUND((_IVARet * _TC), 2);
	_ISRRetPesos :=  ROUND((_ISRRet * _TC), 2);
	_DescPesos := ROUND((_Descuento * _TC), 2);
	_TotalPesos := ROUND((_Total * _TC), 2);	
	_IVANoDed := 0.0;
	_TOTALNoDed := 0.0;
	_concepto := 'Gasto ' || (select Descripcion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_Beneficiario := case when _ID_Proveedor = 0 then 'Al Portador' else ( select Nombre from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	_RFCBeneficiario := case when _ID_Proveedor = 0 then 'XAXX010101000' else ( select RFC from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	_RFC := case when _ID_Proveedor = 0 then 'XAXX010101000' else ( select RFC from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	--_CuentaBeneficiario := case when _ID_Proveedor = 0 then '' else ( select MetodoDePago from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	--_BancoBeneficiario := case when _ID_Proveedor = 0 then '000' else ( select ID_SatBanco from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	_CC_PRO := (select ID_CC from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor);
	_Fija := (select Fija from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	_FijaCost := (select FijaCost from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	--_FijaBAN := case when _ID_FormaPago = 0 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
		--				else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_id_clasificacion := (select ID_Clasificacion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	_numPagos = (select count(*) from _TMP_PAGOS);

	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;

	IF (select count(*) from TBL_COMPRAS_GASTOS_CAB where ID_Entidad = _ID_EntidadCompra and Numero = _Numero) > 0
	THEN
		_err := 3;
		_result := 'ERROR: La clave del gasto ya existe, No se puede duplicar';
	END IF;

	IF _Condicion = 0 
	THEN
		FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
		LOOP
			_ID_FormaPago := _REC_TMP_PAGOS.ID_FormaPago;
			_ID_BanCaj := _REC_TMP_PAGOS.ID_BanCaj;
			_FijaBAN := (case when _ID_FormaPago = 1 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			_IdMon := (case when _ID_FormaPago = 1 then (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			IF _Fija <> _FijaBAN
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar el gasto porque al pagarse de contado requiere que los bancos o cajas manejen los mismos trazos contables que la compra';
				EXIT;
			END IF;

			IF _Moneda <> _IdMon 
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra porque al pagarse de contado requiere que los bancos o cajas manejen la misma moneda que la compra';
				EXIT;
			END IF;
			/*	
			IF _ID_Proveedor = 0 and _Fija = '0' and _ID_FormaPago > 1
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar un gasto directo de mostrador pagado de contado con transferencia, porque no existe soporte para contabilidad electrónica, selecciona un proveedor o una forma de pago distinta';
				EXIT;
			END IF;
			*/
		END LOOP;
	ELSIF _Condicion = 1 -- Es a crédito, por lo tanto revisa si el total de esta factura mas el total de la deuda con el proveedor rebasa el límite de crédito, de ser asi, rechazará la compra
	THEN
		IF(select Dias from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) > 0 -- tenemos credito con este proveedor
		THEN
			IF (select LimiteCredito from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) > 0 AND 
				((_ImporteTotalPesos + getcliprosldtotmn('PR', _ID_Proveedor)) > (select LimiteCredito from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor))
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra a crédito porque la suma de nuestras deudas con este proveedor, más esta compra, sobrepasa nuestro límite de crédito';
			END IF;
		ELSE -- no tenemos crédito
			_err := 3;
			_result := 'ERROR: No se puede agregar la compra porque al pagarse a crédito requiere que el proveedor nos brinde por lo menos un día de crédito';
		END IF;
	END IF;
	
	IF _IVA > 0.0 AND ( _CC_IVA = '' OR _CC_IVAPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva por pagar no existe o no se ha enlazado';
	END IF;
	IF _IEPS > 0.0 AND ( _CC_IEPS = '' OR _CC_IEPSPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente pagado o la de IEPS pendiente de pagar no existe o no se ha enlazado';
	END IF;
	IF _IVARet > 0.0 AND ( _CC_IVARet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de IVA Retenido no existe o no se ha enlazado';
	END IF;
	IF _ISRRet > 0.0 AND ( _CC_ISRRet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de ISR Retenido no existe o no se ha enlazado';
	END IF;
	IF _Descuento > 0.0 AND _CC_Desc = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de descuento sobre compras no existe o no se ha enlazado';
	END IF;

	IF _Condicion = 3
	THEN
		_CC_COMPNP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMPNP');
		IF _CC_COMPNP = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para registro de partida doble cuando no se establece ningun metodo de pago, no existe o no se ha enlazado';
		END IF;
	END IF;
	
	IF _CC_GND = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de gastos no deducibles no existe o no se ha enlazado';
	END IF;

	--VERIFICA SI ES CFDI Asociada
	IF _uuid <> ''
	THEN
		_NumUUIDs := (char_length(_uuid) / 36);
		_TFD = 3;
		_ContUUIDs := 0;
		WHILE _ContUUIDs < _NumUUIDs
		LOOP
			_uuidx := substring(_uuid, ((_ContUUIDs * 36) + 1), 36);
			IF (select count(*) from TBL_CFDCOMP where UUID = _uuidx) = 0
			THEN
				_err := 3;
				_result := 'ERROR: No se puede enlazar el CFDI al registro, porque no existe el UUID proporcionado ' + _uuidx;
				EXIT;
			ELSE
				IF _NumUUIDs = 1 -- Si solo es un UUID asociado a este gasto, lo asocia al gasto directo
				THEN
					_ID_CFD := (select ID_CFD from TBL_CFDCOMP where UUID = _uuidx); 
				END IF;
			END IF;
			_ContUUIDs := _ContUUIDs + 1;
		END LOOP;
	END IF;
	
	IF _err = 0
	THEN
		INSERT INTO TBL_COMPRAS_GASTOS_CAB(  id_entidad, numero, id_clipro, fecha, referencia, status, moneda, tc, fechaenvio, condicion, obs, 
			importe, descuento, subtotal, iva, total, ref, id_pol, id_polcost, id_bodega, mimporte, mdescuento, msubtotal, miva, mtotal, efectivo, bancos, cambio, id_vendedor, id_cfd, tfd, 
			ieps, ivaret, isrret )
		VALUES(_ID_EntidadCompra, _Numero, _ID_Proveedor, _Fecha, _Referencia, 'G', _Moneda, _TC, _Fecha, _Condicion, _Obs,
				_Importe, _Descuento, _SubTotal, _IVA, _Total, null, null, null, _ID_Bodega, _Importe, _Descuento, _SubTotal, _IVA,  _Total, _FSIPG_Efectivo, _FSIPG_Bancos, _FSIPG_Cambio, _ID_Vendedor, _ID_CFD, _TFD,
				_ieps, _ivaret, _isrret)
		 RETURNING currval(pg_get_serial_sequence('TBL_COMPRAS_GASTOS_CAB', 'id_vc')) INTO _id_gasto;
		 
		-- actualiza el numero de GASTO
		UPDATE TBL_COMPRAS_ENTIDADES
		SET Doc = _Numero + 1
		WHERE ID_EntidadCompra = _ID_EntidadCompra;
		
		-- inserta el detalle
		INSERT INTO TBL_COMPRAS_GASTOS_DET
		SELECT _ID_Gasto, Partida, Cantidad, ID_Prod, Precio, Descuento, IVA, Obs, Importe, ImporteDesc, ImporteIVA, TotalPart, IEPS, ImporteIEPS, IVARet, ImporteIVARet, ISRRet, ImporteISRRet
		FROM _TMP_COMPRAS_FACTURAS_DET;

		_Ref := 'CGAS|' || cast(_ID_Gasto as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

		IF _uuid <> ''
		THEN
			_NumUUIDs := (char_length(_uuid) / 36);
			_ContUUIDs := 0;
			WHILE _ContUUIDs < _NumUUIDs
			LOOP
				_uuidx := substring(_uuid, ((_ContUUIDs * 36) + 1), 36);
				UPDATE TBL_CFDCOMP
				SET FSI_Tipo = 'GAS', FSI_ID = _ID_Gasto
				WHERE UUID = _uuidx;
				IF _NumUUIDs > 1 --Ingresa en la tabla auxiliar de enlaces multiples del gasto hacia los CFDIs
				THEN
					insert into TBL_COMPRAS_GASTOS_CFD
					select _ID_Gasto, ID_CFD from TBL_CFDCOMP where UUID = _uuidx;
				END IF;
				_ContUUIDs := _ContUUIDs + 1;
			END LOOP;
			
			FOR _REC_TMP_COMPRAS_GASTOS_DET IN ( select * from _TMP_COMPRAS_FACTURAS_DET order by Partida asc ) 
			LOOP
				-- Procede a agregar el enlace del producto del proveedor al id_prod en forseti
				if(select count(*) from TBL_INVSERV_PROVEE_CODIGOS where ID_RFC = _RFC and ID_Descripcion =  _REC_TMP_COMPRAS_GASTOS_DET.Obs and ID_Prod = _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod and ID_Moneda = _Moneda) = 0
				then
					insert into TBL_INVSERV_PROVEE_CODIGOS
					values(_RFC, _REC_TMP_COMPRAS_GASTOS_DET.Obs,  _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod, _Moneda, _REC_TMP_COMPRAS_GASTOS_DET.Precio, _Fecha);
				else
					update TBL_INVSERV_PROVEE_CODIGOS
					set Precio =  _REC_TMP_COMPRAS_GASTOS_DET.Precio, Fecha = _Fecha
					where ID_RFC = _RFC and ID_Descripcion = _REC_TMP_COMPRAS_GASTOS_DET.Obs and ID_Prod =   _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod and ID_Moneda = _Moneda;
				end if;
			END LOOP; 

		END IF;

		--Si no es de mostrador, actualiza los ultimos precios de proveedores
		IF _ID_Proveedor > 0
		THEN
			FOR _REC_TMP_COMPRAS_GASTOS_DET IN ( select * from _TMP_COMPRAS_FACTURAS_DET order by Partida asc ) 
			LOOP
				-- Procede a agregar el ultimo costo del producto o servicio correspondiente al Proveedor ( si existe lo actualiza de lo contrario lo inserta )
				if(select count(*) from TBL_COMPRAS_VS_INVENTARIO where ID_Tipo = 'PR' and ID_Proveedor = _ID_Proveedor and ID_Prod = _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod and ID_Moneda = _Moneda) = 0
				then
					insert into TBL_COMPRAS_VS_INVENTARIO
					values('PR', _ID_Proveedor,  _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod, _Moneda, _Fecha,  _REC_TMP_COMPRAS_GASTOS_DET.Precio,  _REC_TMP_COMPRAS_GASTOS_DET.Descuento);
				else
					update TBL_COMPRAS_VS_INVENTARIO
					set Fecha = _Fecha, Precio =  _REC_TMP_COMPRAS_GASTOS_DET.Precio, Descuento =  _REC_TMP_COMPRAS_GASTOS_DET.Descuento
					where ID_Tipo = 'PR' and ID_Proveedor = _ID_Proveedor and ID_Prod =   _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod and ID_Moneda = _Moneda;
				end if;
			END LOOP; 
		END IF; 

		--Crea tabla temporal indispensable... para contable.
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_TMP (
			Part smallint NOT NULL ,
			Cuenta char(19) NOT NULL ,
			Concepto varchar(80) NOT NULL ,
			Parcial numeric(19,4) NOT NULL ,
			Moneda smallint NOT NULL ,
			TC numeric(19,4) NOT NULL ,
			Debe numeric(19,4) NOT NULL ,
			Haber numeric(19,4) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP( 
			id_part smallint NOT NULL,
			num character varying(20) NOT NULL,
			banco character(3) NOT NULL,
			ctaori character varying(50) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			monto numeric(19,4) NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			banemisext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		); 
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP ( 
			id_part smallint NOT NULL,
			ctaori character varying(50) NOT NULL,
			bancoori character(3) NOT NULL,
			monto numeric(19,4) NOT NULL,
			ctadest character varying(50) NOT NULL,
			bancodest character(3) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			bancooriext character varying(150) NOT NULL,
			bancodestext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP (
			id_part smallint NOT NULL,
			metpagopol character(2) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			monto numeric(19,4) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP ( 
			id_part smallint NOT NULL, 
			uuid_cfdi character(36) NOT NULL, 
			monto numeric(19,4) NOT NULL, 
			rfc character varying(13) NOT NULL,
			id_tipo character varying(10) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL,
			cfd_cbb_serie character varying(10) NOT NULL,
			cfd_cbb_numfol integer NOT NULL,
			numfactext character varying(36) NOT NULL,
			taxid character varying(30) NOT NULL
		); 
		--fin polizas temporales para contable	
		
		_contPart := 1;
		FOR _REC_TMP_COMPRAS_GASTOS_DET IN ( select * from _TMP_COMPRAS_FACTURAS_DET order by Partida asc ) 
		LOOP
			--Agrega los últimos costos al catálogo de gastos
			UPDATE TBL_INVSERV_INVENTARIOS
			SET UltimoCosto = round(_REC_TMP_COMPRAS_GASTOS_DET.Precio / _TC,4)
			WHERE ID_Prod = _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod;
			
			-- Procede a agregar las partidas del gasto a la póliza
			_iva_deducible := (select IVA_Deducible from TBL_INVSERV_INVENTARIOS where ID_Prod = _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod);
			_descripcion := (select descripcion from TBL_INVSERV_INVENTARIOS where ID_Prod = _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod);
			
			IF _iva_deducible < 100.0
			THEN
				_IVANoDed := _IVANoDed + ( _REC_TMP_COMPRAS_GASTOS_DET.ImporteIVA - (( _REC_TMP_COMPRAS_GASTOS_DET.ImporteIVA * _iva_deducible) / 100 ));
			END IF;
						
			-- revisa en la tabla de cuentas contables a la que se irán los gastos ( segun este ID de gasto )
			-- por ejemplo: 50% a gastos de ventas y el otro 50% a gastos de administración.
			FOR _REC_TMP_INVSERV_GASTOS_PORCENTAJES  IN (select * from TBL_INVSERV_GASTOS_PORCENTAJES where ID_Prod = _REC_TMP_COMPRAS_GASTOS_DET.ID_Prod) 
			LOOP
				--GasNoDed debería calcularse aqui ( igual al iva no ded, y aplicarse en proporción a cada porcentaje del gasto para esta partida ). Creo que esto no lo lleva nadie como es, pero lo proporciono
				--Por si un dia alguien lo pide, aqui ya está desarrollarlo.
				_GASDedParcial := round(((_REC_TMP_COMPRAS_GASTOS_DET.Importe * _REC_TMP_INVSERV_GASTOS_PORCENTAJES.Porcentaje) / 100),2);
				_GASDed := round(round(((_REC_TMP_COMPRAS_GASTOS_DET.Importe * _REC_TMP_INVSERV_GASTOS_PORCENTAJES.Porcentaje) / 100),2) * _TC,2);
				IF _iva_deducible < 100.0
				THEN
					_GASNoDedParcial :=  round(( _GASDedParcial - (( _GASDedParcial * _iva_deducible) / 100 )),2);
					_GASNoDed :=  round(( _GASDed - (( _GASDed * _iva_deducible) / 100 )),2);
					_TOTALNoDed := _TOTALNoDed + _GASNoDedParcial;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _REC_TMP_INVSERV_GASTOS_PORCENTAJES.ID_CC, _descripcion, _GASDedParcial - _GASNoDedParcial, _Moneda, _TC, _GASDed - _GASNoDed, 0.0);
				ELSE
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _REC_TMP_INVSERV_GASTOS_PORCENTAJES.ID_CC, _descripcion, _GASDedParcial, _Moneda, _TC, _GASDed, 0.0);
				END IF;
				--Agrega contabilidad electronica a la primera partida de esta poliza (Si existe UUID)
				IF _contPart = 1 and _uuid <> ''
				THEN
					_NumUUIDs := (char_length(_uuid) / 36);
					_ContUUIDs := 0;
					WHILE _ContUUIDs < _NumUUIDs
					LOOP
						_uuidx := substring(_uuid, ((_ContUUIDs * 36) + 1), 36);
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP
						SELECT _contPart, _uuidx, Total, RFC, 'CompNal', _moneda_ce, _TC, '', 0, '', '' FROM TBL_CFDCOMP WHERE UUID = _uuidx;
						_ContUUIDs := _ContUUIDs + 1;
					END LOOP;
				END IF;
				_contPart := _contPart + 1;
			END LOOP;
		END LOOP; 

		-- procede a registrar los otros conceptos de la poliza ( EL IVA , Deuda con Proveedor ETC)
		IF _IVA > 0.0
		THEN
			IF _IVANoDed > 0.0
			THEN
				_contPart := _contPart + 1;
				_IVANoDed := round(_IVANoDed,2);
				_TOTALNoDed := round(_TOTALNoDed,2);
				IF _Condicion = 0 -- es de contado, envia todo a iva efectivamente pagado
				THEN
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_IVA, 'Impuesto de la compra', _IVA - _IVANoDed, _Moneda, _TC, round((_IVA - _IVANoDed) * _TC, 2), 0.0);
				ELSE
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_IVAPN, 'Impuesto de la compra', _IVA - _IVANoDed, _Moneda, _TC, round((_IVA - _IVANoDed) * _TC, 2), 0.0);
				END IF;
				_contPart := _contPart + 1;		
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart,_CC_GND, 'Gastos no deducibles', _IVANoDed + _TOTALNoDed, _Moneda, _TC, round(( _IVANoDed + _TOTALNoDed ) * _TC,2), 0.0);
			ELSE
				_contPart := _contPart + 1;
				IF _Condicion = 0 -- es de contado, envia todo a iva efectivamente pagado
				THEN
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_IVA, 'Impuesto de la compra', _IVA, _Moneda, _TC, _IVAPesos, 0.0);
				ELSE
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_IVAPN, 'Impuesto de la compra', _IVA, _Moneda, _TC, _IVAPesos, 0.0);
				END IF;
			END IF;
		END IF;

		IF _IEPS > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a ieps a efectivamente pagado
			THEN	
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);	
			ELSE -- de credito. o ningun método de pago. envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPSPN, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);	
			END IF;
		END IF;

		IF _IVARet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IVARet, 'Retención de IVA', _IVARet, _Moneda, _TC, 0.0, _IVARetPesos);	
		END IF;

		IF _ISRRet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_ISRRet, 'Retención de ISR', _ISRRet, _Moneda, _TC, 0.0, _ISRRetPesos);	
		END IF;
		
		IF _Descuento > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_Desc, 'Descuento explicito', _Descuento, _Moneda, _TC, 0.0, _DescPesos);
		END IF;
			
		-- procede a registrar los movimiento en la caja, banco, Cuenta por cobrar, y la poliza en caso necesario
		IF _ID_Proveedor > 0 and _Condicion = 1 -- si es a credito y no de mostrador
		THEN
			SELECT * INTO _errpart, _resultpart, _id_cxp 
			FROM sp_provee_cxp_alta(_ID_EntidadCompra, _Fecha, 'PR', _ID_Proveedor, _Concepto, _Moneda, _TC, _Total, _Cantidad, '0', 'CGAS', _ID_Gasto, _Ref) as ( err integer, res varchar, clave integer ); --este movimiento no registrara poliza porque el concepto 0 de cxp es de sistema 
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			ELSE
				_clase = 'CCXP|' || cast(_ID_CXP as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

				UPDATE TBL_COMPRAS_GASTOS_CAB
				SET ID_Pol = _ID_CXP
				WHERE ID_VC = _ID_Gasto;
				
				-- Procede a registrar la poliza si y solo si es una entidad dinamica
				-- Primero registra la deuda total del proveedor para la partida doble en tmp
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_PRO, 'Deuda total del Proveedor', _Total, _Moneda, _TC, 0.0, _TotalPesos);
					
				IF _Fija = '0'
				THEN
					-- Primero registra y crea la tabla temporal de detalle de la poliza
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
						Part smallint NOT NULL ,
						Cuenta char(19) NOT NULL ,
						Concepto varchar(80) NOT NULL ,
						Parcial numeric(19,4) NOT NULL ,
						Moneda smallint NOT NULL ,
						TC numeric(19,4) NOT NULL ,
						Debe numeric(19,4) NOT NULL ,
						Haber numeric(19,4) NOT NULL
					);
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
						id_part smallint NOT NULL, 
						uuid_cfdi character(36) NOT NULL, 
						monto numeric(19,4) NOT NULL, 
						rfc character varying(13) NOT NULL,
						id_tipo character varying(10) NOT NULL,
						moneda character(3) NOT NULL,
						tipcamb numeric(19,5) NOT NULL,
						cfd_cbb_serie character varying(10) NOT NULL,
						cfd_cbb_numfol integer NOT NULL,
						numfactext character varying(36) NOT NULL,
						taxid character varying(30) NOT NULL
 					); 			
					-- Fin de la tabla temporal

					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
					_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					IF _TotDebe > _TotHaber
					THEN
						_diff := _TotDebe - _TotHaber;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
					ELSIF _TotDebe < _TotHaber
					THEN
						_diff := _TotHaber - _TotDebe;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
					END IF;
					
					-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
					-- Agrega ahora la poliza principal
					--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
					SELECT * INTO _errpart, _resultpart, _numpol 
					FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
					ELSE
						UPDATE  TBL_PROVEE_CXP
						SET ID_Pol = _numpol
						WHERE ID_CP = _ID_CXP;
					END IF;

					DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
					DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
				END IF;
			END IF; 
		ELSIF _Condicion = 0 -- es de contado
		THEN
			-- Termina la poliza dividiendo los pagos en sus cuentas de banco o caja
			_TotalPesosMult = 0.0;
			_ParcialPesosMult = 0.0;
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_ParcialPesosMult := round((_REC_TMP_PAGOS.Total * _TC), 2);
				_TotalPesosMult := _TotalPesosMult + _ParcialPesosMult;
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				_CC_BAN := (select CC from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_CuentaOrigen := (select Descripcion from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_BancoOrigen := (select ID_SatBanco from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				_banCHQ := (select SigCheque from TBL_BANCOS_CUENTAS where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj)::varchar(20);
				_contPart := _contPart + 1;
				
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_BAN, _REC_TMP_PAGOS.RefPago, _REC_TMP_PAGOS.Total, _Moneda, _TC, 0.0, _ParcialPesosMult);
				--Ingresa contablilidad electronica en caso de bancos
				IF _BanCaj = 0 -- Es banco
				THEN
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _banCHQ, ID_SatBanco, Descripcion, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCBeneficiario, BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSIF _REC_TMP_PAGOS.ID_SatMetodosPago = '03' --Es una transferencia
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP
						SELECT _contPart, Descripcion, ID_SatBanco, _REC_TMP_PAGOS.Total, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.ID_SatBanco, _Fecha,  _Beneficiario, _RFCBeneficiario, BancoExt, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				ELSE -- BanCaj = 1 "Caja"
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque (Deposito o Retiro es lo mismo, toma los datos directos capturados en el dialogo)
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES ( 
					id_part smallint NOT NULL,
					num character varying(20) NOT NULL,
					banco character(3) NOT NULL,
					ctaori character varying(50) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					monto numeric(19,4) NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					banemisext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				); 
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS ( 
					id_part smallint NOT NULL,
					ctaori character varying(50) NOT NULL,
					bancoori character(3) NOT NULL,
					monto numeric(19,4) NOT NULL,
					ctadest character varying(50) NOT NULL,
					bancodest character(3) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					bancooriext character varying(150) NOT NULL,
					bancodestext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO (
					id_part smallint NOT NULL,
					metpagopol character(2) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					monto numeric(19,4) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL
				); 
				-- Fin de la tabla temporal

				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;

				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
				
				-- Agrega ahora la poliza principal
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('EG', _Fecha, _Concepto,'0', '', _TotalPesos, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				END IF;
				
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES; 
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
			END IF;

			--Ahora ejecuta los movimientos de caja y bancos
			_clase := '';
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				--_tipoRetiro := (select case when _REC_TMP_PAGOS.ID_FormaPago = 1 Then 'CHQ' else 'RET' end);
				SELECT * INTO _errpart, _resultpart, _bancajmov 
				FROM  sp_bancos_movs_agregar( _BanCaj, _REC_TMP_PAGOS.ID_BanCaj, _Fecha, _Concepto, _Beneficiario, 0.00, _REC_TMP_PAGOS.Total, _REC_TMP_PAGOS.TipoMov, 
						'G', _Moneda, _TC, _Ref, _REC_TMP_PAGOS.RefPago, _id_clasificacion, _numpol, _REC_TMP_PAGOS.ID_SatBanco, _RFCBeneficiario, _REC_TMP_PAGOS.ID_SatMetodosPago, 
																		_REC_TMP_PAGOS.BancoExt, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.Cheque) as ( err integer, res varchar, clave integer);
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
					EXIT;
				END IF;

				INSERT INTO TBL_COMPRAS_GASTOS_PAGOS
				VALUES(_ID_Gasto, _bancajmov);
				
				IF _numPagos > 1
				THEN
					IF _BanCaj = 0
					THEN
						_clase := _clase || 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					ELSE
						_clase := _clase || 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					END IF;
				ELSE
					IF _BanCaj = 0
					THEN
						_clase := 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					ELSE
						_clase := 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0' -- Aqui registra la referencia de las polizas de ingreso a sus movimientos de caja o bancos
			THEN
				IF _numPagos > 1
				THEN
					INSERT INTO TBL_REFERENCIAS_EXT
					VALUES(default,_clase)
					RETURNING currval(pg_get_serial_sequence(' TBL_REFERENCIAS_EXT', 'id_ref')) INTO _RefExt;
					UPDATE TBL_CONT_POLIZAS
					SET Ref = 'REXT|' || cast(_RefExt as varchar) || '|||'
					WHERE ID = _numpol;
		 		ELSE
					UPDATE TBL_CONT_POLIZAS
					SET Ref = _clase
					WHERE ID = _numpol;
				END IF;
			END IF; 
		ELSIF _Condicion = 3 -- Es de ningun pago
		THEN
			_clase = 'CGAS|' || cast(_ID_Gasto as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

			-- Procede a registrar la poliza si y solo si es una entidad dinamica
			-- Primero registra la deuda total del proveedor para la partida doble en tmp
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_COMPNP, 'Documento por pagar', _Total, _Moneda, _TC, 0.0, _TotalPesos);
					
			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL
 				); 			
				-- Fin de la tabla temporal
				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;
				
				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;

				-- Agrega ahora la poliza principal
				--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					UPDATE TBL_COMPRAS_GASTOS_CAB
					SET ID_Pol = _numpol
					WHERE ID_VC = _ID_Gasto;
				END IF;

				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
			END IF;
		END IF;
		
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP; 
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
		--Procede a agregar el movimiento al almacén
		IF _err = 0 AND _FijaCost = '0' and (select count(*) from _TMP_COMPRAS_FACTURAS_DET d inner join TBL_INVSERV_INVENTARIOS i on d.ID_Prod = i.ID_Prod where i.NoSeVende = '1') > 0
		THEN
			CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (
				ID_Bodega smallint NOT NULL ,
				ID_Prod varchar(20) NOT NULL ,
				Partida smallint NOT NULL ,
				Cantidad numeric(9, 3) NOT NULL ,
				Costo numeric(19,4) NULL 
			); 

			insert into _TMP_INVSERV_ALMACEN_MOVIM_DET
			select _ID_Bodega, d.ID_Prod, d.Partida, d.Cantidad, 0.0
			from _TMP_COMPRAS_FACTURAS_DET d inner join TBL_INVSERV_INVENTARIOS i on d.ID_Prod = i.ID_Prod where i.NoSeVende = '1'
			order by Partida ASC;
		
			SELECT * INTO _errpart, _resultpart, _ID_Movimiento 
			FROM sp_invserv_alm_utensilios_agregar(_Fecha, _ID_Bodega, 'G', '-1', _Concepto, '','ENTRADA', _Ref, 'CGAS', _ID_Gasto) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				UPDATE TBL_COMPRAS_GASTOS_CAB
				SET ID_PolCost = _ID_Movimiento
				WHERE ID_VC = _ID_Gasto;
			END IF;
	
			DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET; 
		END IF;
		-- Fin del movimiento al almacen
		
	END IF; 
	
	RETURN QUERY SELECT _err, _result, _id_gasto;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_gastos_agregar(smallint, integer, integer, timestamp without time zone, character varying, smallint, numeric, smallint, character varying, numeric, numeric, numeric, numeric, numeric, numeric, numeric, numeric, smallint, integer, smallint, character, character varying, numeric, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_compras_facturas_agregar(
    _id_entidadcompra smallint,
    _numero integer,
    _id_proveedor integer,
    _fecha timestamp without time zone,
    _referencia character varying,
    _moneda smallint,
    _tc numeric,
    _condicion smallint,
    _obs character varying,
    _importe numeric,
    _descuento numeric,
    _subtotal numeric,
    _iva numeric,
    _total numeric,
    _fsipg_efectivo numeric,
    _fsipg_bancos numeric,
    _fsipg_cambio numeric,
    _id_bodega smallint,
    _id_enlace integer,
    _id_vendedor smallint,
    _tipoenlace character,
    _uuid character,
    _ieps numeric,
    _ivaret numeric,
    _isrret numeric)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_Factura int; _mes smallint; _ano smallint;	_errpart int;  _resultpart varchar(255);  _ID_Movimiento int;  _concepto varchar(80); _conceptocost varchar(80); _Ref varchar(25);  _claseref varchar(25); 
	_FijaCost bit; _costcomp numeric(19,4); _CC_COSTCOMP char(19); _AuditarAlm bit;
	_ID_CXP int; _numpol int; _clase varchar(1024); _bancajmov int;
	_nombrePro varchar(120); _Cantidad numeric(19,4); _ServComp bit; _CC_PRO char(19); 
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_COMPNP char(19); _CC_DCAF char(19);
	_CC_DCEC char(19); _diff numeric(19,4); _TotDebe numeric(19,4); _TotHaber numeric(19,4); _contPart smallint; 
	_totPart smallint; _CC_COMP char(19); _CC_COMCONT char(19); _tipoRetiro varchar(10); _Beneficiario varchar(80);
	 _contban smallint; _totalban smallint; _ID_FormaPago smallint; _ID_BanCaj smallint; _RefPago varchar(25); _banCHQ varchar(20); _IdMon smallint; _RecepcionAsociada bit; 
	_id_clasificacion varchar(10); _numPagos smallint; _RefExt int; _ID_CFD int; _TFD smallint;  
	_RFC varchar(15); _RFCBeneficiario varchar(15); _moneda_ce character(3); /*  _CuentaBeneficiario varchar(80); _BancoBeneficiario character(3); _CuentaOrigen varchar(50); _BancoOrigen character(3);  */
	--Iteracion
	_REC_TMP_PAGOS RECORD; _REC_TMP_COMPRAS_FACTURAS_DET RECORD; _TotalPesosMult numeric(19,4); _ParcialPesosMult numeric(19,4); _BanCaj smallint; _CC_BAN char(19);
BEGIN	
	_err := 0;
	_result := 'La factura de compra se ha registrado satisfactoriamente';
	--_ServComp := '1';
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAAC');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAACPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSC');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSCPN');
	_CC_IVARet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVARETC');
	_CC_ISRRet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_ISRREC');
	_CC_Desc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSC');
	_moneda_ce := (select id_satmoneda from TBL_CONT_MONEDAS where Clave = _moneda);
	_Cantidad := round((_Total * _TC), 2);
	_ImporteTotalPesos := ROUND((_Importe * _TC), 2);
	_IVAPesos :=  ROUND((_IVA * _TC), 2);
	_IEPSPesos :=  ROUND((_IEPS * _TC), 2);
	_IVARetPesos :=  ROUND((_IVARet * _TC), 2);
	_ISRRetPesos :=  ROUND((_ISRRet * _TC), 2);
	_DescPesos := ROUND((_Descuento * _TC), 2);
	_TotalPesos := ROUND((_Total * _TC), 2);	
	_concepto := 'Compra ' || (select Descripcion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_conceptocost := 'Costo compra ' || (select Descripcion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_Beneficiario := case when _ID_Proveedor = 0 then 'Al Portador' else ( select Nombre from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	_RFCBeneficiario := case when _ID_Proveedor = 0 then 'XAXX010101000' else ( select RFC from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	--_CuentaBeneficiario := case when _ID_Proveedor = 0 then '' else ( select MetodoDePago from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	--_BancoBeneficiario := case when _ID_Proveedor = 0 then '000' else ( select ID_SatBanco from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	_CC_PRO := (select ID_CC from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor);
	_Fija := (select Fija from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	_FijaCost := (select FijaCost from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	--_FijaBAN := case when _ID_FormaPago = 0 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
		--				else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_AuditarAlm := ( select AuditarAlm from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega );
	_RecepcionAsociada := ( case when _ID_Enlace is not null and _TipoEnlace = 'CREC' then '1' else '0' end );
	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_id_clasificacion := (select ID_Clasificacion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	_numPagos = (select count(*) from _TMP_PAGOS);

	IF _ID_Enlace is not null
	THEN
		_claseref := _TipoEnlace || '|' || cast(_ID_Enlace as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';
	END IF;
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;

	IF (select count(*) from TBL_COMPRAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadCompra and Numero = _Numero) > 0
	THEN
		_err := 3;
		_result := 'ERROR: La clave de la factura ya existe, No se puede duplicar';
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF _Condicion = 0 
	THEN
		FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
		LOOP
			_ID_FormaPago := _REC_TMP_PAGOS.ID_FormaPago;
			_ID_BanCaj := _REC_TMP_PAGOS.ID_BanCaj;
			_FijaBAN := (case when _ID_FormaPago = 1 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			_IdMon := (case when _ID_FormaPago = 1 then (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			
			IF _Fija <> _FijaBAN
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra porque al pagarse de contado requiere que los bancos o cajas manejen los mismos trazos contables que la compra';
				EXIT;
			END IF;

			IF _Moneda <> _IdMon 
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra porque al pagarse de contado requiere que los bancos o cajas manejen la misma moneda que la compra';
				EXIT;
			END IF;
			/*
			IF _ID_Proveedor = 0 and _Fija = '0' and _ID_FormaPago > 0
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar una compra directa de mostrador pagada de contado con cheque o transferencia, porque no existe soporte para contabilidad electrónica, selecciona un proveedor o una entidad Fija';
				EXIT;
			END IF; */
		END LOOP;
	ELSIF _Condicion = 1 -- Es a crédito, por lo tanto revisa si el total de esta factura mas el total de la deuda con el proveedor rebasa el límite de crédito, de ser asi, rechazará la compra
	THEN
		IF(select Dias from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) > 0 -- tenemos credito con este proveedor
		THEN
			IF (select LimiteCredito from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) > 0 AND 
				((_ImporteTotalPesos + getcliprosldtotmn('PR', _ID_Proveedor)) > (select LimiteCredito from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor))
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra a crédito porque la suma de nuestras deudas con este proveedor, más esta compra, sobrepasa nuestro límite de crédito';
			END IF;
		ELSE -- no tenemos crédito
			_err := 3;
			_result := 'ERROR: No se puede agregar la compra porque al pagarse a crédito requiere que el proveedor nos brinde por lo menosu un día de crédito';
		END IF;
	END IF;
	
	IF _IVA > 0.0 AND ( _CC_IVA = '' OR _CC_IVAPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva acreditable efectivamente pagado o la de iva acreditable pendiente de pagar no existe o no se ha enlazado';
	END IF;
	IF _IEPS > 0.0 AND ( _CC_IEPS = '' OR _CC_IEPSPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente pagado o la de IEPS pendiente de pagar no existe o no se ha enlazado';
	END IF;
	IF _IVARet > 0.0 AND ( _CC_IVARet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de IVA Retenido no existe o no se ha enlazado';
	END IF;
	IF _ISRRet > 0.0 AND ( _CC_ISRRet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de ISR Retenido no existe o no se ha enlazado';
	END IF;
	IF _Descuento > 0.0 AND _CC_Desc = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de descuento sobre compras no existe o no se ha enlazado';
	END IF;
	
	-- procede a realizar verificaci?n sobre los trazos
	IF _FijaCost = '0'
	THEN
		_CC_COSTCOMP := (select cc from TBL_INVSERV_COSTOS_CONCEPTOS where ID_Concepto = 1);
		IF _CC_COSTCOMP is null
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para el costo de compras no existe o no se ha enlazado';	
		END IF;
	END IF;

	IF _Condicion = 0
	THEN
		_CC_COMCONT := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMCONT');
		IF _CC_COMCONT = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para compras de contado no existe o no se ha enlazado';	
		END IF;
	ELSE 
		_CC_COMP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
		IF _CC_COMP = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para compras a crédito no existe o no se ha enlazado';	
		END IF;
	END IF;

	IF _Condicion = 3
	THEN
		_CC_COMPNP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMPNP');
		IF _CC_COMPNP = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para registro de partida doble cuando no se establece ningun metodo de pago, no existe o no se ha enlazado';
		END IF;
	END IF;
	
	--VERIFICA SI ES CFDI Asociada
	IF _uuid <> ''
	THEN
		IF (select count(*) from TBL_CFDCOMP where UUID = _uuid) > 0
		THEN
			_ID_CFD := (select ID_CFD from TBL_CFDCOMP where UUID = _uuid) ;
			_TFD := 3;
		ELSE
			_err := 3;
			_result := 'ERROR: No se puede enlazar el CFDI al registro, porque no existe el UUID proporcionado';
		END IF;
	END IF;
	
	IF _err = 0
	THEN
		INSERT INTO TBL_COMPRAS_FACTURAS_CAB(  id_entidad, numero, id_clipro, fecha, referencia, status, moneda, tc, fechaenvio, condicion, obs, 
			importe, descuento, subtotal, iva, total, ref, id_pol, id_polcost, id_bodega, mimporte, mdescuento, msubtotal, miva, mtotal, efectivo, bancos, cambio, id_vendedor, id_cfd, tfd, 
			ieps, ivaret, isrret )
		VALUES(_ID_EntidadCompra, _Numero, _ID_Proveedor, _Fecha, _Referencia, (case when _RecepcionAsociada = '0' then (case when _AuditarAlm = '1' then 'G' else 'E' end) else 'E' end), _Moneda, _TC, _Fecha, _Condicion, _Obs,
				_Importe, _Descuento, _SubTotal, _IVA, _Total, _claseref, null, null, _ID_Bodega, _Importe, _Descuento, _SubTotal, _IVA,  _Total, _FSIPG_Efectivo, _FSIPG_Bancos, _FSIPG_Cambio, _ID_Vendedor, _ID_CFD, _TFD,
				_ieps, _ivaret, _isrret)
		 RETURNING currval(pg_get_serial_sequence('TBL_COMPRAS_FACTURAS_CAB', 'id_vc')) INTO _id_factura;
		 
		-- actualiza el numero de FACTURA
		UPDATE TBL_COMPRAS_ENTIDADES
		SET Doc = _Numero + 1
		WHERE ID_EntidadCompra = _ID_EntidadCompra;
		
		-- inserta el detalle
		INSERT INTO TBL_COMPRAS_FACTURAS_DET
		SELECT _ID_Factura, Partida, Cantidad, ID_Prod, Precio, Descuento, IVA, Obs, Importe, ImporteDesc, ImporteIVA, TotalPart, IEPS, ImporteIEPS, IVARet, ImporteIVARet, ISRRet, ImporteISRRet
		FROM _TMP_COMPRAS_FACTURAS_DET;

		_Ref := 'CFAC|' || cast(_ID_Factura as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

		-- Actualiza el status de la orden o recepcion ligando el ID de la FACTURA
		IF _ID_Enlace is not null
		THEN
			IF _TipoEnlace = 'CORD'
			THEN
				UPDATE TBL_COMPRAS_ORDENES_CAB
				SET Status = 'F', ID_Factura = _ID_Factura, TipoEnlace = 'CFAC'
				WHERE ID_VC = _ID_Enlace;
			ELSE
				UPDATE TBL_COMPRAS_RECEPCIONES_CAB
				SET ID_Factura = _ID_Factura
				WHERE ID_VC = _ID_Enlace;
			END IF;
		END IF;

		IF _uuid <> ''
		THEN
			UPDATE TBL_CFDCOMP
			SET FSI_Tipo = 'FAC', FSI_ID = _ID_Factura
			WHERE ID_CFD = _ID_CFD;

			_RFC := (select RFC from TBL_CFDCOMP where ID_CFD = _ID_CFD);
			
			FOR _REC_TMP_COMPRAS_FACTURAS_DET IN ( select * from _TMP_COMPRAS_FACTURAS_DET order by Partida asc ) 
			LOOP
				-- Procede a agregar el enlace del producto del proveedor al id_prod en forseti
				if(select count(*) from TBL_INVSERV_PROVEE_CODIGOS where ID_RFC = _RFC and ID_Descripcion =  _REC_TMP_COMPRAS_FACTURAS_DET.Obs and ID_Prod = _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda) = 0
				then
					insert into TBL_INVSERV_PROVEE_CODIGOS
					values(_RFC, _REC_TMP_COMPRAS_FACTURAS_DET.Obs,  _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod, _Moneda, _REC_TMP_COMPRAS_FACTURAS_DET.Precio, _Fecha);
				else
					update TBL_INVSERV_PROVEE_CODIGOS
					set Precio =  _REC_TMP_COMPRAS_FACTURAS_DET.Precio, Fecha = _Fecha
					where ID_RFC = _RFC and ID_Descripcion = _REC_TMP_COMPRAS_FACTURAS_DET.Obs and ID_Prod =   _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda;
				end if;
			END LOOP; 
		END IF;

		--Si no es de mostrador, actualiza los ultimos precios de proveedores
		IF _ID_Proveedor > 0
		THEN
			FOR _REC_TMP_COMPRAS_FACTURAS_DET IN ( select * from _TMP_COMPRAS_FACTURAS_DET order by Partida asc ) 
			LOOP
				-- Procede a agregar el ultimo costo del producto o servicio correspondiente al Proveedor ( si existe lo actualiza de lo contrario lo inserta )
				if(select count(*) from TBL_COMPRAS_VS_INVENTARIO where ID_Tipo = 'PR' and ID_Proveedor = _ID_Proveedor and ID_Prod = _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda) = 0
				then
					insert into TBL_COMPRAS_VS_INVENTARIO
					values('PR', _ID_Proveedor,  _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod, _Moneda, _Fecha,  _REC_TMP_COMPRAS_FACTURAS_DET.Precio,  _REC_TMP_COMPRAS_FACTURAS_DET.Descuento);
				else
					update TBL_COMPRAS_VS_INVENTARIO
					set Fecha = _Fecha, Precio =  _REC_TMP_COMPRAS_FACTURAS_DET.Precio, Descuento =  _REC_TMP_COMPRAS_FACTURAS_DET.Descuento
					where ID_Tipo = 'PR' and ID_Proveedor = _ID_Proveedor and ID_Prod =   _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda;
				end if;
			END LOOP; 
		END IF; 

		-- Actualiza el precio de compra en el inventario
		UPDATE TBL_INVSERV_INVENTARIOS
		SET PrecioComp = tm.Precio, ID_Moneda = _Moneda
		FROM TBL_INVSERV_INVENTARIOS e, _TMP_COMPRAS_FACTURAS_DET tm 
		WHERE e.ID_Prod = tm.ID_Prod
			AND e.ID_Prod = TBL_INVSERV_INVENTARIOS.ID_Prod;
		
		--Crea tablas temporales indispensables... para contable. y contabilidad electronica
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_TMP (
			Part smallint NOT NULL ,
			Cuenta char(19) NOT NULL ,
			Concepto varchar(80) NOT NULL ,
			Parcial numeric(19,4) NOT NULL ,
			Moneda smallint NOT NULL ,
			TC numeric(19,4) NOT NULL ,
			Debe numeric(19,4) NOT NULL ,
			Haber numeric(19,4) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP( 
			id_part smallint NOT NULL,
			num character varying(20) NOT NULL,
			banco character(3) NOT NULL,
			ctaori character varying(50) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			monto numeric(19,4) NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			banemisext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		); 
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP ( 
			id_part smallint NOT NULL,
			ctaori character varying(50) NOT NULL,
			bancoori character(3) NOT NULL,
			monto numeric(19,4) NOT NULL,
			ctadest character varying(50) NOT NULL,
			bancodest character(3) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			bancooriext character varying(150) NOT NULL,
			bancodestext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP (
			id_part smallint NOT NULL,
			metpagopol character(2) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			monto numeric(19,4) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP ( 
			id_part smallint NOT NULL, 
			uuid_cfdi character(36) NOT NULL, 
			monto numeric(19,4) NOT NULL, 
			rfc character varying(13) NOT NULL,
			id_tipo character varying(10) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL,
			cfd_cbb_serie character varying(10) NOT NULL,
			cfd_cbb_numfol integer NOT NULL,
			numfactext character varying(36) NOT NULL,
			taxid character varying(30) NOT NULL
		); 
		
		--fin polizas temporales para contable			
		-- Agrega a la tabla de temporal para: polizas final o detalles de bancos cajas final
		_contPart := 1;
		IF _Condicion = 0 
		THEN
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_COMCONT, 'Importe de la compra', _Importe, _Moneda, _TC, _ImporteTotalPesos, 0.0);
		ELSE
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_COMP, 'Importe de la compra', _Importe, _Moneda, _TC, _ImporteTotalPesos, 0.0);
		END IF;
		
		IF _uuid <> ''
		THEN
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP
			VALUES(_contPart, _uuid, _Total, _RFCBeneficiario, 'CompNal', _moneda_ce, _TC, '', 0, '', '');
		END IF;
		
		-- procede a registrar los otros conceptos de la poliza ( EL IVA , Deuda con Proveedor ETC)
		IF _IVA > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a iva efectivamente pagado
			THEN	
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVA, 'Impuesto de la compra', _IVA, _Moneda, _TC, _IVAPesos, 0.0);
			ELSE -- de credito. o ningun método de pago. envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVAPN, 'Impuesto de la compra', _IVA, _Moneda, _TC, _IVAPesos, 0.0);
			END IF;
		END IF;

		IF _IEPS > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a ieps a efectivamente pagado
			THEN	
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);	
			ELSE -- de credito. o ningun método de pago. envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPSPN, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);	
			END IF;
		END IF;

		IF _IVARet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IVARet, 'Retención de IVA', _IVARet, _Moneda, _TC, 0.0, _IVARetPesos);	
		END IF;

		IF _ISRRet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_ISRRet, 'Retención de ISR', _ISRRet, _Moneda, _TC, 0.0, _ISRRetPesos);	
		END IF;

		IF _Descuento > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_Desc, 'Descuento explicito', _Descuento, _Moneda, _TC, 0.0, _DescPesos);
		END IF;
			
		
		-- procede a registrar los movimiento en la caja, banco, Cuenta por cobrar, y la poliza en caso necesario
		IF _ID_Proveedor > 0 and _Condicion = 1 -- si es a credito y no de mostrador
		THEN
			SELECT * INTO _errpart, _resultpart, _id_cxp 
			FROM sp_provee_cxp_alta(_ID_EntidadCompra, _Fecha, 'PR', _ID_Proveedor, _Concepto, _Moneda, _TC, _Total, _Cantidad, '0', 'CFAC', _ID_Factura, _Ref) as ( err integer, res varchar, clave integer ); --este movimiento no registrara poliza porque el concepto 0 de cxp es de sistema 
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			ELSE
				_clase = 'CCXP|' || cast(_ID_CXP as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

				UPDATE TBL_COMPRAS_FACTURAS_CAB
				SET ID_Pol = _ID_CXP
				WHERE ID_VC = _ID_Factura;
				
				-- Procede a registrar la poliza si y solo si es una entidad dinamica
				-- Primero registra la deuda total del proveedor para la partida doble en tmp
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_PRO, 'Deuda total del Proveedor', _Total, _Moneda, _TC, 0.0, _TotalPesos);
					
				IF _Fija = '0'
				THEN
					-- Primero registra y crea la tabla temporal de detalle de la poliza
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
						Part smallint NOT NULL ,
						Cuenta char(19) NOT NULL ,
						Concepto varchar(80) NOT NULL ,
						Parcial numeric(19,4) NOT NULL ,
						Moneda smallint NOT NULL ,
						TC numeric(19,4) NOT NULL ,
						Debe numeric(19,4) NOT NULL ,
						Haber numeric(19,4) NOT NULL
					);
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
						id_part smallint NOT NULL, 
						uuid_cfdi character(36) NOT NULL, 
						monto numeric(19,4) NOT NULL, 
						rfc character varying(13) NOT NULL,
						id_tipo character varying(10) NOT NULL,
						moneda character(3) NOT NULL,
						tipcamb numeric(19,5) NOT NULL,
						cfd_cbb_serie character varying(10) NOT NULL,
						cfd_cbb_numfol integer NOT NULL,
						numfactext character varying(36) NOT NULL,
						taxid character varying(30) NOT NULL
 					); 
					-- Fin de la tabla temporal

					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
					_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					IF _TotDebe > _TotHaber
					THEN
						_diff := _TotDebe - _TotHaber;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
					ELSIF _TotDebe < _TotHaber
					THEN
						_diff := _TotHaber - _TotDebe;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
					END IF;
					
					-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
					-- Agrega ahora la poliza principal
					--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
					SELECT * INTO _errpart, _resultpart, _numpol 
					FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
					ELSE
						UPDATE  TBL_PROVEE_CXP
						SET ID_Pol = _numpol
						WHERE ID_CP = _ID_CXP;
					END IF;

					DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
					DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
				END IF;
			END IF; 
		ELSIF _Condicion = 0 -- es de contado
		THEN
			-- Termina la poliza dividiendo los pagos en sus cuentas de banco o caja
			_TotalPesosMult = 0.0;
			_ParcialPesosMult = 0.0;
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_ParcialPesosMult := round((_REC_TMP_PAGOS.Total * _TC), 2);
				_TotalPesosMult := _TotalPesosMult + _ParcialPesosMult;
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				_CC_BAN := (select CC from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_CuentaOrigen := (select Descripcion from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_BancoOrigen := (select ID_SatBanco from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				_banCHQ := (select SigCheque from TBL_BANCOS_CUENTAS where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj)::varchar(20);
				_contPart := _contPart + 1;
				
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_BAN, _REC_TMP_PAGOS.RefPago, _REC_TMP_PAGOS.Total, _Moneda, _TC, 0.0, _ParcialPesosMult);
				--Ingresa contablilidad electronica del pago...
							-- Actualiza la contabilidad electronica de este BANCO o CAJA a esta partida, ya sea por cheque, transferencia u otro metodo de pago
				IF _BanCaj = 0 -- Es banco
				THEN
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _banCHQ, ID_SatBanco, Descripcion, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCBeneficiario, BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSIF _REC_TMP_PAGOS.ID_SatMetodosPago = '03' --Es una transferencia
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP
						SELECT _contPart, Descripcion, ID_SatBanco, _REC_TMP_PAGOS.Total, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.ID_SatBanco, _Fecha,  _Beneficiario, _RFCBeneficiario, BancoExt, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				ELSE -- BanCaj = 1 "Caja"
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque (Deposito o Retiro es lo mismo, toma los datos directos capturados en el dialogo)
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES ( 
					id_part smallint NOT NULL,
					num character varying(20) NOT NULL,
					banco character(3) NOT NULL,
					ctaori character varying(50) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					monto numeric(19,4) NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					banemisext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				); 
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS ( 
					id_part smallint NOT NULL,
					ctaori character varying(50) NOT NULL,
					bancoori character(3) NOT NULL,
					monto numeric(19,4) NOT NULL,
					ctadest character varying(50) NOT NULL,
					bancodest character(3) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					bancooriext character varying(150) NOT NULL,
					bancodestext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO (
					id_part smallint NOT NULL,
					metpagopol character(2) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					monto numeric(19,4) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL
				); 
				-- Fin de la tabla temporal

				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;

				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP y DEMAS
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
				-- Agrega ahora la poliza principal
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('EG', _Fecha, _Concepto,'0', '', _TotalPesos, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				END IF;
				
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES; 
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
			END IF;

			--Ahora ejecuta los movimientos de caja y bancos
			_clase := '';
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				--_tipoRetiro := (select case when _REC_TMP_PAGOS.ID_FormaPago = 1 Then 'CHQ' else 'RET' end);
				SELECT * INTO _errpart, _resultpart, _bancajmov 
				FROM  sp_bancos_movs_agregar( _BanCaj, _REC_TMP_PAGOS.ID_BanCaj, _Fecha, _Concepto, _Beneficiario, 0.00, _REC_TMP_PAGOS.Total, _REC_TMP_PAGOS.TipoMov, 
						'G', _Moneda, _TC, _Ref, _REC_TMP_PAGOS.RefPago, _id_clasificacion, _numpol, _REC_TMP_PAGOS.ID_SatBanco, _RFCBeneficiario, _REC_TMP_PAGOS.ID_SatMetodosPago, 
																		_REC_TMP_PAGOS.BancoExt, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.Cheque) as ( err integer, res varchar, clave integer);
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
					EXIT;
				END IF;

				INSERT INTO TBL_COMPRAS_FACTURAS_PAGOS
				VALUES(_ID_Factura, _bancajmov);
				
				IF _numPagos > 1
				THEN
					IF _BanCaj = 0
					THEN
						_clase := _clase || 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					ELSE
						_clase := _clase || 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					END IF;
				ELSE
					IF _BanCaj = 0
					THEN
						_clase := 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					ELSE
						_clase := 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0' -- Aqui registra la referencia de las polizas de ingreso a sus movimientos de caja o bancos
			THEN
				IF _numPagos > 1
				THEN
					INSERT INTO TBL_REFERENCIAS_EXT
					VALUES(default,_clase)
					RETURNING currval(pg_get_serial_sequence(' TBL_REFERENCIAS_EXT', 'id_ref')) INTO _RefExt;
					UPDATE TBL_CONT_POLIZAS
					SET Ref = 'REXT|' || cast(_RefExt as varchar) || '|||'
					WHERE ID = _numpol;
		 		ELSE
					UPDATE TBL_CONT_POLIZAS
					SET Ref = _clase
					WHERE ID = _numpol;
				END IF;
			END IF; 
		ELSIF _Condicion = 3 -- Es de ningun pago
		THEN
			_clase = 'CFAC|' || cast(_ID_Factura as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

			-- Procede a registrar la poliza si y solo si es una entidad dinamica
			-- Primero registra la deuda total del proveedor para la partida doble en tmp
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_COMPNP, 'Documento por pagar', _Total, _Moneda, _TC, 0.0, _TotalPesos);
					
			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL
 				); 
				-- Fin de la tabla temporal
					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;
				
				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;

				-- Agrega ahora la poliza principal
				--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
 				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					UPDATE TBL_COMPRAS_FACTURAS_CAB
					SET ID_Pol = _numpol
					WHERE ID_VC = _ID_Factura;
				END IF;

				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
			END IF;
		END IF;
		
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP; 
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;

		--Procede a agregar el movimiento al almacén
		IF _err = 0 AND _FijaCost = '0' AND _RecepcionAsociada = '0' and (select count(*) from _TMP_COMPRAS_FACTURAS_DET where Tipo = 'P') > 0
		THEN
			CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (
				ID_Bodega smallint NOT NULL ,
				ID_Prod varchar(20) NOT NULL ,
				Partida smallint NOT NULL ,
				Cantidad numeric(9, 3) NOT NULL ,
				Costo numeric(19,4) NULL 
			); 

			insert into _TMP_INVSERV_ALMACEN_MOVIM_DET
			select _ID_Bodega, ID_Prod, Partida, Cantidad, round(Precio * _TC,4)  --Precio es el costo de la compra
			from _TMP_COMPRAS_FACTURAS_DET
			where Tipo = 'P'
			order by Partida ASC;
		
			SELECT * INTO _errpart, _resultpart, _ID_Movimiento 
			FROM sp_invserv_alm_movs_agregar(_Fecha, _ID_Bodega, (case when _AuditarAlm = '1' then 'G' else 'U' end), '1', _ConceptoCost, '',/*1 ENT 2 SAL*/ '1', _Ref, 'CFAC', _ID_Factura) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				UPDATE TBL_COMPRAS_FACTURAS_CAB
				SET ID_PolCost = _ID_Movimiento
				WHERE ID_VC = _ID_Factura;
			END IF;
	
			DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET; 
		END IF;
		-- Fin del movimiento al almacen
		
	END IF; 
	
	RETURN QUERY SELECT _err, _result, _id_factura;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_facturas_agregar(smallint, integer, integer, timestamp without time zone, character varying, smallint, numeric, smallint, character varying, numeric, numeric, numeric, numeric, numeric, numeric, numeric, numeric, smallint, integer, smallint, character, character, numeric, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ventas_facturas_cancelar(
    _id_factura integer,
    _id_entidadventa smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_Numero int; _Fecha timestamp; _ID_Pol int; _ID_PolCost int; _err int; _result varchar(255); _errpart int; 
	_resultpart varchar(255); _Ref varchar(25); _ID_CXC int; _ID_Movimiento int; _ID_Bodega smallint;
 	_ID_BanCaj int; _Condicion smallint; _mes smallint; _ano smallint; _ID_Pedido int; _ID_Cotizacion int; _ID_Remision int;
 	_ID_CFD int; _TFD smallint; _EntCFD bit(2); _ID_CFDRES int;
 	_REC_TMP_PAGOS RECORD; 
BEGIN
	_err := 0;
	_result := 'La Factura se ha cancelado satisfactoriamente';
	_Fecha := (select Fecha from TBL_VENTAS_FACTURAS_CAB where ID_VC = _ID_Factura);
	_Numero := (select Numero from TBL_VENTAS_FACTURAS_CAB where ID_VC = _ID_Factura);
	_ID_Movimiento := (select ID_PolCost from TBL_VENTAS_FACTURAS_CAB where ID_VC = _ID_Factura); -- el id del movimiento al almacen
	_ID_CXC := (select ID_Pol from TBL_VENTAS_FACTURAS_CAB where ID_VC = _ID_Factura); 
	_ID_Bodega := (select ID_Bodega from TBL_VENTAS_FACTURAS_CAB where ID_VC = _ID_Factura);
	_Condicion := (select Condicion from TBL_VENTAS_FACTURAS_CAB where ID_VC = _ID_Factura);
	
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	
	_ID_Cotizacion := (select ID_VC from TBL_VENTAS_COTIZACIONES_CAB where ID_Factura = _ID_Factura and TipoEnlace = 'VFAC');
	_ID_Pedido := (select ID_VC from TBL_VENTAS_PEDIDOS_CAB where ID_Factura = _ID_Factura and TipoEnlace = 'VFAC');
	_ID_Remision := (select ID_VC from TBL_VENTAS_REMISIONES_CAB where ID_Factura = _ID_Factura);

	_ID_CFD := (select ID_CFD from TBL_VENTAS_FACTURAS_CAB WHERE ID_VC = _ID_Factura);
	_TFD := (select TFD from TBL_VENTAS_FACTURAS_CAB WHERE ID_VC = _ID_Factura);
	_EntCFD := (select CFD from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);

	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF( _TFD = 1 or _TFD = 2 )
	THEN
		_err := 3;
		_result := 'ERROR: La factura no esta sellada completamente, puede que falte generar el PDF. Generalo para poderla cancelar';	
	END IF;

	IF (select count(*) from tbl_ventas_cierres_facturas where id_entidad = _id_entidadventa and numero = _Numero) > 0
	THEN
		_err := 3;
		_result := 'ERROR: Esta factura ya pertenece a un cierre de caja, por lo tanto no se puede cancelar. Debes ingresar una devolución en caso de que sea estrictamente necesario este ajuste';	
	END IF;
  
	IF _err = 0
	THEN
		UPDATE TBL_VENTAS_FACTURAS_CAB
		SET Status = 'C'
		WHERE ID_VC = _ID_Factura;
		
		-- procede a cancelar el CFD
		IF _ID_CFD is not null
		THEN
			IF (select count(*) from tbl_cfdven where id_cfd = _id_cfd) = 0 -- El CFDI se generó internamente
			THEN
				select * into _errpart, _resultpart, _id_cfdres from sp_cfd_cancelar(_ID_CFD) as (err int,  resultpart varchar, clave int);
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
				ELSE
					UPDATE TBL_VENTAS_FACTURAS_CAB
					SET TFD = 3
					WHERE ID_VC = _ID_Factura;
				END IF;
			ELSE -- Este CFDI fue cargado y enlazado a este registro
				IF (select FSI_Tipo from TBL_CFDVEN where ID_CFD = _ID_CFD) = 'FAC' and (select FSI_ID from TBL_CFDVEN where ID_CFD = _ID_CFD) = _ID_Factura
				THEN 
					UPDATE TBL_CFDVEN
					SET FSI_Tipo = 'ENT', FSI_ID = _ID_EntidadVenta
					WHERE ID_CFD = _ID_CFD;
					UPDATE TBL_CFD
					SET FSI_Tipo = 'ENT', FSI_ID = _ID_EntidadVenta
					WHERE ID_CFD = _ID_CFD;
				END IF;
				
				UPDATE TBL_VENTAS_FACTURAS_CAB
				SET ID_CFD = null
				WHERE ID_VC = _ID_Factura;
			END IF;
		ELSIF _EntCFD <> '00'
		THEN
			UPDATE TBL_VENTAS_FACTURAS_CAB
			SET TFD = 3
			WHERE ID_VC = _ID_Factura;
		END IF;
		-- FIN CFD

		-- procede a desligar la cotizacion asociada ( SI LO HAY PARA ESTA REMISION )
		IF _ID_Cotizacion is not null
		THEN
			UPDATE TBL_VENTAS_COTIZACIONES_CAB
			SET Status = 'G', ID_Factura = 0, TipoEnlace = null
			WHERE ID_VC = _ID_Cotizacion;
		END IF;

		-- procede a desligar el pedido asociado ( SI LO HAY PARA ESTA REMISION )
		IF _ID_Pedido is not null
		THEN
			UPDATE TBL_VENTAS_PEDIDOS_CAB
			SET Status = 'G', ID_Factura = 0, TipoEnlace = null
			WHERE ID_VC = _ID_Pedido;
		END IF;

		IF _ID_Remision is not null
		THEN
			UPDATE TBL_VENTAS_REMISIONES_CAB
			SET ID_Factura = 0
			WHERE ID_VC = _ID_Remision;
		END IF;

		--Procede a la cancelacion de la CXC, PAGOS o Poliza (Ningun pago)
		IF _Condicion = 0
		THEN
			FOR _REC_TMP_PAGOS IN  (select ID_Mov from  tbl_ventas_facturas_pagos where ID_Factura = _ID_Factura)
			LOOP
				SELECT * INTO _errpart, _resultpart, _id_bancaj FROM sp_bancos_movs_cancelar(_REC_TMP_PAGOS.ID_Mov) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
					EXIT;
				END IF;
			END LOOP;
		ELSIF _Condicion = 1
		THEN
			SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_client_cxc_cancelar(_id_cxc) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			END IF;
		ELSE --Ningun pago... id_cxc es en realidad la poliza del documento por cobrar
			--Procede a cancelar la poliza
			IF _ID_CXC is not null
			THEN
				SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_cont_polizas_cancelar(_ID_CXC, _Fecha) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
				END IF;
			END IF;
		END IF;
		
		-- procede a cancelar los detalles del movimiento al almacen
		IF _err = 0 AND _ID_Movimiento is not null
		THEN
			SELECT * INTO _errpart, _resultpart, _id_polcost FROM sp_invserv_alm_movs_cancelar( _id_movimiento ) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			END IF;
		END IF;

	END IF;		

	RETURN QUERY SELECT _err, _result, _ID_Factura;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_ventas_facturas_cancelar(integer, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_compras_facturas_cancelar(
    _id_factura integer,
    _id_entidadcompra smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_Numero int; _Fecha timestamp; _ID_Pol int; _ID_PolCost int; _err int; _result varchar(255); _errpart int; 
	_resultpart varchar(255); _Ref varchar(25); _ID_CXP int; _ID_Movimiento int; _ID_Bodega smallint;
 	_ID_BanCaj int; _Condicion smallint; _mes smallint; _ano smallint; _ID_Orden int; _ID_Recepcion int;
 	_ID_CFD int; _TFD smallint; 
 	_REC_TMP_PAGOS RECORD; 
BEGIN
	_err := 0;
	_result := 'La Compra Factura se ha cancelado satisfactoriamente';
	_Fecha := (select Fecha from TBL_COMPRAS_FACTURAS_CAB where ID_VC = _ID_Factura);
	_Numero := (select Numero from TBL_COMPRAS_FACTURAS_CAB where ID_VC = _ID_Factura);
	_ID_Movimiento := (select ID_PolCost from TBL_COMPRAS_FACTURAS_CAB where ID_VC = _ID_Factura); -- el id del movimiento al almacen
	_ID_CXP := (select ID_Pol from TBL_COMPRAS_FACTURAS_CAB where ID_VC = _ID_Factura); 
	_ID_Bodega := (select ID_Bodega from TBL_COMPRAS_FACTURAS_CAB where ID_VC = _ID_Factura);
	_Condicion := (select Condicion from TBL_COMPRAS_FACTURAS_CAB where ID_VC = _ID_Factura);
	
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	
	_ID_Orden := (select ID_VC from TBL_COMPRAS_ORDENES_CAB where ID_Factura = _ID_Factura and TipoEnlace = 'CFAC');
	_ID_Recepcion := (select ID_VC from TBL_COMPRAS_RECEPCIONES_CAB where ID_Factura = _ID_Factura);

	_ID_CFD := (select ID_CFD from TBL_COMPRAS_FACTURAS_CAB WHERE ID_VC = _ID_Factura);
	_TFD := (select TFD from TBL_COMPRAS_FACTURAS_CAB WHERE ID_VC = _ID_Factura);
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_COMPRAS_FACTURAS_CAB
		SET Status = 'C'
		WHERE ID_VC = _ID_Factura;
		
		--procede a cancelar el CFD
		IF _ID_CFD is not null
		THEN
			IF (select FSI_Tipo from TBL_CFDCOMP where ID_CFD = _ID_CFD) = 'FAC' and (select FSI_ID from TBL_CFDCOMP where ID_CFD = _ID_CFD) = _ID_Factura
			THEN 
				UPDATE TBL_CFDCOMP
				SET FSI_Tipo = 'ENT', FSI_ID = _ID_EntidadCompra
				WHERE ID_CFD = _ID_CFD;
			END IF;
			
			UPDATE TBL_COMPRAS_FACTURAS_CAB
			SET ID_CFD = null
			WHERE ID_VC = _ID_Factura;
		END IF;

		-- procede a desligar la orden asociada ( SI LO HAY PARA ESTA FACTURA )
		IF _ID_Orden is not null
		THEN
			UPDATE TBL_COMPRAS_ORDENES_CAB
			SET Status = 'G', ID_Factura = 0, TipoEnlace = null
			WHERE ID_VC = _ID_Orden;
		END IF;

		IF _ID_Recepcion is not null
		THEN
			UPDATE TBL_COMPRAS_RECEPCIONES_CAB
			SET ID_Factura = 0
			WHERE ID_VC = _ID_Recepcion;
		END IF;

		--Procede a la cancelacion de la CXP o PAGOS
		IF _Condicion = 0
		THEN
			FOR _REC_TMP_PAGOS IN  (select ID_Mov from  tbl_compras_facturas_pagos where ID_Factura = _ID_Factura)
			LOOP
				SELECT * INTO _errpart, _resultpart, _id_bancaj FROM sp_bancos_movs_cancelar(_REC_TMP_PAGOS.ID_Mov) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
					EXIT;
				END IF;
			END LOOP;
		ELSIF _Condicion = 1
		THEN
			SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_provee_cxp_cancelar(_id_cxp) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			END IF;
		ELSE --Ningun pago... id_cxp es en realidad la poliza del documento por pagar
			--Procede a cancelar la poliza
			IF _ID_CXP is not null
			THEN
				SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_cont_polizas_cancelar(_ID_CXP, _Fecha) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
				END IF;
			END IF;
		END IF;
		
		-- procede a cancelar los detalles del movimiento al almacen
		IF _err = 0 AND _ID_Movimiento is not null
		THEN
			SELECT * INTO _errpart, _resultpart, _id_polcost FROM sp_invserv_alm_movs_cancelar( _id_movimiento ) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			END IF;
		END IF;

	END IF;		

	RETURN QUERY SELECT _err, _result, _ID_Factura;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_facturas_cancelar(integer, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_compras_gastos_cancelar(
    _id_gasto integer,
    _id_entidadcompra smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_Numero int; _Fecha timestamp; _ID_Pol int; _ID_PolCost int; _err int; _result varchar(255); _errpart int; 
	_resultpart varchar(255); _Ref varchar(25); _ID_CXP int; _ID_Movimiento int; _ID_Bodega smallint;
 	_ID_BanCaj int; _Condicion smallint; _mes smallint; _ano smallint;
 	_ID_CFD int; _TFD smallint; 
 	_REC_TMP_PAGOS RECORD; 
BEGIN
	_err := 0;
	_result := 'La Compra Gasto se ha cancelado satisfactoriamente';
	_Fecha := (select Fecha from TBL_COMPRAS_GASTOS_CAB where ID_VC = _ID_Gasto);
	_Numero := (select Numero from TBL_COMPRAS_GASTOS_CAB where ID_VC = _ID_Gasto);
	_ID_Movimiento := (select ID_PolCost from TBL_COMPRAS_GASTOS_CAB where ID_VC = _ID_Gasto); -- el id del movimiento al almacen
	_ID_CXP := (select ID_Pol from TBL_COMPRAS_GASTOS_CAB where ID_VC = _ID_Gasto); 
	_ID_Bodega := (select ID_Bodega from TBL_COMPRAS_GASTOS_CAB where ID_VC = _ID_Gasto);
	_Condicion := (select Condicion from TBL_COMPRAS_GASTOS_CAB where ID_VC = _ID_Gasto);
	
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	
	_ID_CFD := (select ID_CFD from TBL_COMPRAS_GASTOS_CAB WHERE ID_VC = _ID_Gasto);
	_TFD := (select TFD from TBL_COMPRAS_GASTOS_CAB WHERE ID_VC = _ID_Gasto);
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_COMPRAS_GASTOS_CAB
		SET Status = 'C'
		WHERE ID_VC = _ID_Gasto;
		
		--procede a cancelar el CFD
		IF _ID_CFD is not null OR _TFD is not null
		THEN
			IF _ID_CFD is not null --El gasto solo esta enlazado a un UUID
			THEN
				IF (select FSI_Tipo from TBL_CFDCOMP where ID_CFD = _ID_CFD) = 'GAS' and (select FSI_ID from TBL_CFDCOMP where ID_CFD = _ID_CFD) = _ID_Gasto
				THEN 
					UPDATE TBL_CFDCOMP
					SET FSI_Tipo = 'ENT', FSI_ID = _ID_EntidadCompra
					WHERE ID_CFD = _ID_CFD;
				END IF;
			ELSE -- ENLAZADO A VARIOS UUIDs
				UPDATE TBL_CFDCOMP
				SET FSI_Tipo = 'ENT', FSI_ID = _ID_EntidadCompra
				WHERE FSI_Tipo = 'GAS' AND FSI_ID = _ID_Gasto;
				--Elimina las asociaciones al gasto
				DELETE FROM TBL_COMPRAS_GASTOS_CFD
				WHERE ID_Gasto = _ID_Gasto;
			END IF;
			
			UPDATE TBL_COMPRAS_GASTOS_CAB
			SET ID_CFD = null
			WHERE ID_VC = _ID_Gasto;
		END IF;

		--Procede a la cancelacion de la CXP o PAGOS
		IF _Condicion = 0
		THEN
			FOR _REC_TMP_PAGOS IN  (select ID_Mov from  tbl_compras_gastos_pagos where ID_Gasto = _ID_Gasto)
			LOOP
				SELECT * INTO _errpart, _resultpart, _id_bancaj FROM sp_bancos_movs_cancelar(_REC_TMP_PAGOS.ID_Mov) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
					EXIT;
				END IF;
			END LOOP;
		ELSIF _Condicion = 1
		THEN
			SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_provee_cxp_cancelar(_id_cxp) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			END IF;
		ELSE --Ningun pago... id_cxp es en realidad la poliza del documento por pagar
			--Procede a cancelar la poliza
			IF _ID_CXP is not null
			THEN
				SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_cont_polizas_cancelar(_ID_CXP, _Fecha) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
				END IF;
			END IF;
		END IF;
		
		-- procede a cancelar los detalles del movimiento al almacen
		IF _err = 0 AND _ID_Movimiento is not null
		THEN
			SELECT * INTO _errpart, _resultpart, _id_polcost FROM sp_invserv_alm_utensilios_cancelar( _id_movimiento ) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			END IF;
		END IF;
		
	END IF;		

	RETURN QUERY SELECT _err, _result, _ID_Gasto;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_gastos_cancelar(integer, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ventas_devoluciones_agregar(
    _id_entidadventa smallint,
    _numero integer,
    _id_cliente integer,
    _fecha timestamp without time zone,
    _referencia character varying,
    _moneda smallint,
    _tc numeric,
    _condicion smallint,
    _obs character varying,
    _importe numeric,
    _descuento numeric,
    _subtotal numeric,
    _iva numeric,
    _total numeric,
    _fsipg_efectivo numeric,
    _fsipg_bancos numeric,
    _fsipg_cambio numeric,
    _id_bodega smallint,
    _id_factura integer,
    _id_vendedor smallint,
    _fsipg_id_concepto smallint,
    _fsipg_desc_concepto character varying,
    _devreb character,
    _ieps numeric,
    _ivaret numeric,
    _isrret numeric)
  RETURNS SETOF record AS
$BODY$ 
DECLARE 
	_err int; _result varchar(255); _ID_Devolucion int; _mes smallint; _ano smallint; _errpart int;  _resultpart varchar(255); _ID_Movimiento int; _concepto varchar(80); _conceptocost varchar(80); _conceptoDescuento varchar(80); _Ref varchar(25); _claseref varchar(25);  
	_FijaCost bit; _costven numeric(19,4); _CC_COSTVEN char(19); _AuditarAlm bit; 
	_ID_CXC int; _ID_CXCSALDO int; _numpol int; _clase varchar(1024); _bancajmov int;
	_tipoRetiro varchar(10); _Cantidad numeric(19,4);/* _ServComp bit; */_CC_CLI char(19); 
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_VENNP char(19); _CC_DCAF char(19);
	_CC_DCEC char(19); _diff numeric(19,4); _TotDebe numeric(19,4); _TotHaber numeric(19,4); _contPart smallint; 
	/*_totPart smallint; */_CC_DEVREB char(19); _DesgloseCLIENT bit;
	_contban smallint; _totalban smallint; _ID_FormaPago smallint; _ID_BanCaj smallint; _RefPago varchar(25); _banCHQ varchar(20); _IdMon smallint; /*_RemisionAsociada bit;*/ 
	_id_clasificacion varchar(10); _numPagos smallint; _RefExt int;
	--contabilidad electronica
	_RFCBeneficiario varchar(15); _Beneficiario varchar(80); _moneda_ce character(3); /*_CuentaBeneficiario varchar(80); _BancoBeneficiario character(3); _CuentaOrigen varchar(50); _BancoOrigen character(3); */
	--Iteracion
	_REC_TMP_PAGOS RECORD; _REC_TMP_VENTAS_FACTURAS_DET RECORD; _TotalPesosMult numeric(19,4); _ParcialPesosMult numeric(19,4); _BanCaj smallint; _CC_BAN char(19);
BEGIN	
	_err := 0;
	_result := 'La devolucion se ha registrado satisfactoriamente';
	--_ServComp := '1';
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPP');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPPPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSV');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSVPN');
	_CC_IVARet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVARETV');
	_CC_ISRRet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_ISRRETV');
	_CC_Desc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSV');
	IF _Condicion = 0
	THEN
		_CC_DEVREB := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSVCONT');
		_ConceptoDescuento := 'Devolucion y/o rebaja sobre venta de contado';
	ELSIF _Condicion = 1	
	THEN
		_CC_DEVREB := (select cc from TBL_CLIENT_CXC_CONCEPTOS where ID_Concepto = _fsipg_id_concepto);
		_ConceptoDescuento := _fsipg_desc_concepto;
	ELSE --Ningun pago
		_CC_DEVREB := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSV');
		_ConceptoDescuento := 'Devolucion y/o rebaja sobre venta de crédito';
	END IF;
	_moneda_ce := (select id_satmoneda from TBL_CONT_MONEDAS where Clave = _moneda);
	_Cantidad := round((_Total * _TC), 2);
	_ImporteTotalPesos := ROUND((_Importe * _TC), 2);
	_IVAPesos :=  ROUND((_IVA * _TC), 2);
	_IEPSPesos :=  ROUND((_IEPS * _TC), 2);
	_IVARetPesos :=  ROUND((_IVARet * _TC), 2);
	_ISRRetPesos :=  ROUND((_ISRRet * _TC), 2);
	_DescPesos := ROUND((_Descuento * _TC), 2);
	_TotalPesos := ROUND((_Total * _TC), 2);	
	_concepto := 'Devolucion s/venta ' || (select Descripcion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_conceptocost := 'Costo dev/ven ' || (select Descripcion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_CC_CLI := (select ID_CC from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente);

	_Beneficiario := case when _ID_Cliente = 0 then 'Al Portador' else ( select Nombre from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente ) end;
	_RFCBeneficiario := case when _ID_Cliente = 0 then 'XAXX010101000' else ( select RFC from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente ) end;
	--_CuentaBeneficiario := case when _ID_Cliente = 0 then '' else ( select MetodoDePago from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente ) end;
	--_BancoBeneficiario := case when _ID_Cliente = 0 then '000' else ( select ID_SatBanco from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente ) end;

	_DesgloseCLIENT := (select DesgloseCLIENT from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_Fija := (select Fija from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_FijaCost := (select FijaCost from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_AuditarAlm := ( select AuditarAlm from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega );
	_ID_CXC := ( select ID_CP from TBL_CLIENT_CXC where id_tipodocorig = 'VFAC' and id_clavedocorig = _ID_Factura);

	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_id_clasificacion := (select ID_Clasificacion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_numPagos := (select count(*) from _TMP_PAGOS);

	_claseref := 'VFAC|' || cast(_ID_Factura as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;

	IF (select count(*) from TBL_VENTAS_DEVOLUCIONES_CAB where ID_Entidad = _ID_EntidadVenta and Numero = _Numero) > 0
	THEN
		_err := 3;
		_result := 'ERROR: La clave de la devolucion ya existe, No se puede duplicar';
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF _Condicion = 0 
	THEN
		FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
		LOOP
			_ID_FormaPago := _REC_TMP_PAGOS.ID_FormaPago;
			_ID_BanCaj := _REC_TMP_PAGOS.ID_BanCaj;
			_FijaBAN := (case when _ID_FormaPago = 1 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			_IdMon := (case when _ID_FormaPago = 1 then (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);

			IF _Fija <> _FijaBAN
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la devolucion/rebaja porque al pagarse de contado requiere que los bancos manejen los mismos trazos contables que la venta';
				EXIT;
			END IF;

			IF _Moneda <> _IdMon 
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la devolucion/rebaja porque al pagarse de contado requiere que los bancos manejen la misma moneda que la venta';
				EXIT;
			END IF;
		END LOOP;
	END IF;
	
	IF _IVA > 0.0 AND ( _CC_IVA = '' OR _CC_IVAPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva por pagar efectivamente cobrado o la de iva por pagar pendiente de cobrar, no existe o no se ha enlazado';
	END IF;
	IF _IEPS > 0.0 AND ( _CC_IEPS = '' OR _CC_IEPSPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente cobrado o la de IEPS pendiente de cobrar no existe o no se ha enlazado';
	END IF;
	IF _IVARet > 0.0 AND ( _CC_IVARet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de IVA Retenido no existe o no se ha enlazado';
	END IF;
	IF _ISRRet > 0.0 AND ( _CC_ISRRet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de ISR Retenido no existe o no se ha enlazado';
	END IF;
	IF _Descuento > 0.0 AND _CC_Desc = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de descuento sobre ventas no existe o no se ha enlazado';
	END IF;

	IF _CC_DEVREB is null or _CC_DEVREB = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de devoluciones sobre ventas de contado o crédito no se ha enlazado en variables, o en enlaces de cuentas por cobrar';
	END IF;

	-- procede a realizar verificaci?n sobre los trazos
	IF _FijaCost = '0'
	THEN
		_CC_COSTVEN := (select cc from TBL_INVSERV_COSTOS_CONCEPTOS where ID_Concepto = 2);
		IF _CC_COSTVEN is null
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para el costo de ventas no existe o no se ha enlazado';	
		END IF;
	END IF;

	IF _Condicion = 3
	THEN
		_CC_VENNP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VENNP');
		IF _CC_VENNP = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para registro de partida doble cuando no se establece ningun metodo de pago, no existe o no se ha enlazado';
		END IF;
	END IF;

	--VERIFICA SI ES CFD
	IF (select CFD from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta) <> '00'
	THEN
		IF (select count(*) from TBL_CFD_REPORTE_MENSUAL where Mes = _mes and Ano = _ano and Cerrado = '0') > 0
		THEN
			IF (select count(*) from TBL_VENTAS_DEVOLUCIONES_CAB where ID_Entidad = _ID_EntidadVenta and Numero = (_Numero -1)) is not null
			THEN
				IF (select TFD from TBL_VENTAS_DEVOLUCIONES_CAB where ID_Entidad = _ID_EntidadVenta and Numero = (_Numero -1)) is null
						or (select TFD from TBL_VENTAS_DEVOLUCIONES_CAB where ID_Entidad = _ID_EntidadVenta and Numero = (_Numero -1)) <> 3
				THEN 
					_err := 3;
					_result := 'ERROR: No se puede agregar la devolucion porque la dev anterior no est&aacute; sellada. Primero debes sellar la dev anterior para poder agregar esta dev';
				END IF;
			END IF;
		ELSE
			_err := 3;
			_result := 'ERROR: No se puede agregar la dev porque el mes de comprobantes fiscales digitaya';
		END IF;
	END IF;

	IF _err = 0
	THEN
		INSERT INTO TBL_VENTAS_DEVOLUCIONES_CAB (  id_entidad, numero, id_clipro, fecha, referencia, status, moneda, tc, fechaenvio, condicion, obs, 
			importe, descuento, subtotal, iva, total, ref, id_pol, id_polcost, id_bodega, mimporte, mdescuento, msubtotal, miva, mtotal, efectivo, bancos, cambio, id_vendedor, id_cfd, tfd, 
			ieps, ivaret, isrret, id_factura, devreb )
		VALUES(_ID_EntidadVenta, _Numero, _ID_Cliente, _Fecha, _Referencia, (case when _AuditarAlm = '1' and _devreb = 'DEV' then 'G' else 'E' end) , _Moneda, _TC, _Fecha, _Condicion, _Obs,
				_Importe, _Descuento, _SubTotal, _IVA, _Total, _claseref, null, null, _ID_Bodega, _Importe, _Descuento, _SubTotal, _IVA,  _Total, _FSIPG_Efectivo, _FSIPG_Bancos, _FSIPG_Cambio, _ID_Vendedor, null, null, 
				_ieps, _ivaret, _isrret, _id_factura, _devreb )
		RETURNING currval(pg_get_serial_sequence('TBL_VENTAS_DEVOLUCIONES_CAB', 'id_vc')) INTO _id_devolucion;
		 
		-- actualiza el numero de FACTURA
		UPDATE TBL_VENTAS_ENTIDADES
		SET Devolucion = _Numero + 1
		WHERE ID_EntidadVenta = _ID_EntidadVenta;
		
		-- inserta el detalle
		INSERT INTO TBL_VENTAS_DEVOLUCIONES_DET
		SELECT _ID_Devolucion, Partida, Cantidad, ID_Prod, Precio, Descuento, IVA, Obs, Importe, ImporteDesc, ImporteIVA, TotalPart, IEPS, ImporteIEPS, IVARet, ImporteIVARet, ISRRet, ImporteISRRet
		FROM _TMP_VENTAS_FACTURAS_DET;

		_Ref := 'VDEV|' || cast(_ID_Devolucion as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';

		--Crea tabla temporal indispensable... para contable.
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_TMP (
			Part smallint NOT NULL ,
			Cuenta char(19) NOT NULL ,
			Concepto varchar(80) NOT NULL ,
			Parcial numeric(19,4) NOT NULL ,
			Moneda smallint NOT NULL ,
			TC numeric(19,4) NOT NULL ,
			Debe numeric(19,4) NOT NULL ,
			Haber numeric(19,4) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP( 
			id_part smallint NOT NULL,
			num character varying(20) NOT NULL,
			banco character(3) NOT NULL,
			ctaori character varying(50) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			monto numeric(19,4) NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			banemisext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		); 
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP ( 
			id_part smallint NOT NULL,
			ctaori character varying(50) NOT NULL,
			bancoori character(3) NOT NULL,
			monto numeric(19,4) NOT NULL,
			ctadest character varying(50) NOT NULL,
			bancodest character(3) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			bancooriext character varying(150) NOT NULL,
			bancodestext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP (
			id_part smallint NOT NULL,
			metpagopol character(2) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			monto numeric(19,4) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		-- Agrega a la tabla de temporal para: polizas final o detalles de bancos cajas final
		_contPart := 1;
		INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
		VALUES(_contPart, _CC_DEVREB, _ConceptoDescuento, _Importe, _Moneda, _TC, _ImporteTotalPesos, 0.0);

		-- procede a registrar los otros conceptos de la poliza ( EL IVA , Deuda con Cliente ETC)
		IF _IVA > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a iva efectivamente pagado
			THEN	
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVA, 'Parte de Impuesto devuelta', _IVA, _Moneda, _TC, _IVAPesos, 0.0);
			ELSE -- de credito o sin pago... envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVAPN, 'Parte de Impuesto devuelta', _IVA, _Moneda, _TC, _IVAPesos, 0.0);
			END IF;
		END IF;

		IF _IEPS > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a ieps efectivamente pagado
			THEN	
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);
			ELSE -- de credito o sin pago... envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPSPN, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);
			END IF;	
		END IF;

		IF _IVARet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IVARet, 'Retención de IVA', _IVARet, _Moneda, _TC, 0.0, _IVARetPesos);	
		END IF;

		IF _ISRRet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_ISRRet, 'Retención de ISR', _ISRRet, _Moneda, _TC, 0.0, _ISRRetPesos);	
		END IF;

		IF _Descuento > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_Desc, 'Parte del descuento explicito devuelta', _Descuento, _Moneda, _TC, 0.0, _DescPesos);
		END IF;
			
		-- procede a registrar los movimientos en la caja, banco, Cuenta por cobrar, y la poliza en caso necesario
		IF _ID_Cliente > 0 and _Condicion = 1 -- si es a credito y no de mostrador
		THEN
			SELECT * INTO _errpart, _resultpart, _id_cxcsaldo 
			FROM sp_client_cxc_pagar(_ID_EntidadVenta, _ID_CXC, _Fecha,  '', _Moneda, _TC, _Total, '0', '0', _Cantidad, _ConceptoDescuento, cast(_ID_CXC as varchar), '1', _fsipg_id_concepto, false, 'VDEV', _ID_Devolucion, _Ref,          	null,  		null, 						null, 		null, 			null,	null ) as ( err integer, res varchar, clave integer ); --este movimiento registrará la póliza externa _numpol 
			--sp_client_cxc_pagar(		_id_entidad 		_id_cxc, _fecha,    _moneda   _tc   _total 	    _cantidad,     _obs 				    _docamparado     _essaldo     _id_concepto      _id_tipodocorig _id_clavedocorig_claseref    _tipomov,_id_satbanco,_id_satmetodospago, _bancoext,_cuentabanco,_cheque)
    			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			ELSE
				_clase = 'VCXC|' || cast(_ID_CXCSALDO as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';

				UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
				SET ID_Pol = _ID_CXCSALDO
				WHERE ID_VC = _ID_Devolucion;
				
				-- Procede a registrar la poliza si y solo si es una entidad dinamica
				-- Primero registra la deuda total del cliente para la partida doble en tmp
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_CLI, 'Devolución al Cliente', _Total, _Moneda, _TC, 0.0, _TotalPesos);
					
				IF _Fija = '0'
				THEN
					-- Primero registra y crea la tabla temporal de detalle de la poliza
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
						Part smallint NOT NULL ,
						Cuenta char(19) NOT NULL ,
						Concepto varchar(80) NOT NULL ,
						Parcial numeric(19,4) NOT NULL ,
						Moneda smallint NOT NULL ,
						TC numeric(19,4) NOT NULL ,
						Debe numeric(19,4) NOT NULL ,
						Haber numeric(19,4) NOT NULL
					);
					-- Fin de la tabla temporal

					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
					_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					IF _TotDebe > _TotHaber
					THEN
						_diff := _TotDebe - _TotHaber;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
					ELSIF _TotDebe < _TotHaber
					THEN
						_diff := _TotHaber - _TotDebe;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
					END IF;
					
					-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;

					-- Agrega ahora la poliza principal
					--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
					SELECT * INTO _errpart, _resultpart, _numpol 
					FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
					ELSE
						UPDATE  TBL_CLIENT_CXC
						SET ID_Pol = _numpol
						WHERE ID_CP = _ID_CXCSALDO;
					END IF;

					DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
			
				END IF;
			END IF; 
		ELSIF _Condicion = 0 -- es de contado
		THEN
			-- Termina la poliza dividiendo los pagos en sus cuentas de banco o caja
			_TotalPesosMult = 0.0;
			_ParcialPesosMult = 0.0;
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_ParcialPesosMult := round((_REC_TMP_PAGOS.Total * _TC), 2);
				_TotalPesosMult := _TotalPesosMult + _ParcialPesosMult;
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				_CC_BAN := (select CC from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				_banCHQ := (select SigCheque from TBL_BANCOS_CUENTAS where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj)::varchar(20);
				_contPart := _contPart + 1;
				
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_BAN, _REC_TMP_PAGOS.RefPago, _REC_TMP_PAGOS.Total, _Moneda, _TC, 0.0, _ParcialPesosMult);
				--Ingresa contablilidad electronica en caso de bancos
				IF _BanCaj = 0 -- Es banco
				THEN
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _banCHQ, ID_SatBanco, Descripcion, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCBeneficiario, BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSIF _REC_TMP_PAGOS.ID_SatMetodosPago = '03' --Es una transferencia
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP
						SELECT _contPart, Descripcion, ID_SatBanco, _REC_TMP_PAGOS.Total, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.ID_SatBanco, _Fecha,  _Beneficiario, _RFCBeneficiario, BancoExt, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				ELSE -- BanCaj = 1 "Caja"
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque (Deposito o Retiro es lo mismo, toma los datos directos capturados en el dialogo)
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES ( 
					id_part smallint NOT NULL,
					num character varying(20) NOT NULL,
					banco character(3) NOT NULL,
					ctaori character varying(50) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					monto numeric(19,4) NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					banemisext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				); 
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS ( 
					id_part smallint NOT NULL,
					ctaori character varying(50) NOT NULL,
					bancoori character(3) NOT NULL,
					monto numeric(19,4) NOT NULL,
					ctadest character varying(50) NOT NULL,
					bancodest character(3) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					bancooriext character varying(150) NOT NULL,
					bancodestext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO (
					id_part smallint NOT NULL,
					metpagopol character(2) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					monto numeric(19,4) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				-- Fin de la tabla temporal

				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;

				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
				
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('EG', _Fecha, _Concepto,'0', '', _TotalPesos, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				END IF;
				
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO;
			END IF;

			--Ahora ejecuta los movimientos de caja y bancos
			_clase := '';
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				
				SELECT * INTO _errpart, _resultpart, _bancajmov 
				FROM  sp_bancos_movs_agregar( _BanCaj, _REC_TMP_PAGOS.ID_BanCaj, _Fecha, _Concepto, _Beneficiario, 0.00, _REC_TMP_PAGOS.Total, _REC_TMP_PAGOS.TipoMov, 
						'G', _Moneda, _TC, _Ref, _REC_TMP_PAGOS.RefPago, _id_clasificacion, _numpol, _REC_TMP_PAGOS.ID_SatBanco, _RFCBeneficiario, _REC_TMP_PAGOS.ID_SatMetodosPago, 
																		_REC_TMP_PAGOS.BancoExt, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.Cheque) as ( err integer, res varchar, clave integer);
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
					EXIT;
				END IF;

				INSERT INTO TBL_VENTAS_DEVOLUCIONES_PAGOS
				VALUES(_ID_Devolucion, _bancajmov);
				
				IF _numPagos > 1
				THEN
					IF _BanCaj = 0
					THEN
						_clase := _clase || 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					ELSE
						_clase := _clase || 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					END IF;
				ELSE
					IF _BanCaj = 0
					THEN
						_clase := 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					ELSE
						_clase := 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0' -- Aqui registra la referencia de las polizas de ingreso a sus movimientos de caja o bancos
			THEN
				IF _numPagos > 1
				THEN
					INSERT INTO TBL_REFERENCIAS_EXT
					VALUES(default,_clase)
					RETURNING currval(pg_get_serial_sequence(' TBL_REFERENCIAS_EXT', 'id_ref')) INTO _RefExt;
					UPDATE TBL_CONT_POLIZAS
					SET Ref = 'REXT|' || cast(_RefExt as varchar) || '|||'
					WHERE ID = _numpol;
		 		ELSE
					UPDATE TBL_CONT_POLIZAS
					SET Ref = _clase
					WHERE ID = _numpol;
				END IF;
			END IF;
		ELSIF _Condicion = 3 -- Ningun pago
		THEN
			_clase = 'VDEV|' || cast(_ID_Devolucion as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';

			-- Procede a registrar la poliza si y solo si es una entidad dinamica
			-- Primero registra la deuda total del cliente para la partida doble en tmp
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_VENNP, 'Devolución/rebaja de documento por cobrar', _Total, _Moneda, _TC, 0.0, _TotalPesos);
					
			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				-- Fin de la tabla temporal
					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;
				
				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				-- Agrega ahora la poliza principal
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
					SET ID_Pol = _numpol
					WHERE ID_VC = _ID_Devolucion;
				END IF;

				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
			
			END IF;
		END IF;
		
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
			
		--Procede a agregar el movimiento al almacén
		IF _err = 0 AND _FijaCost = '0' AND _DevReb = 'DEV' 
		THEN
			CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (
				ID_Bodega smallint NOT NULL ,
				ID_Prod varchar(20) NOT NULL ,
				Partida smallint NOT NULL ,
				Cantidad numeric(9, 3) NOT NULL ,
				Costo numeric(19,4) NULL 
			); 

			insert into _TMP_INVSERV_ALMACEN_MOVIM_DET
			select _ID_Bodega, ID_Prod, Partida, Cantidad, Precio --Precio no es el costo, sin embargo lo ignorará en la tarjeta de almacen y se calculará el costo porque el concepto de venta 50, indica recalcular costo
			from _TMP_VENTAS_FACTURAS_DET
			order by Partida ASC;
		
			SELECT * INTO _errpart, _resultpart, _ID_Movimiento 
			FROM sp_invserv_alm_movs_agregar(_Fecha, _ID_Bodega, (case when _AuditarAlm = '1' then 'G' else 'U' end), '2', _ConceptoCost, '',/*1 ENT 2 SAL*/ '1', _Ref, 'VDEV', _ID_Devolucion) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
				SET ID_PolCost = _ID_Movimiento
				WHERE ID_VC = _ID_Devolucion;
			END IF;
	
			DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET; 
		END IF;
		-- Fin del movimiento al almacen
		
	END IF; 
	
	RETURN QUERY SELECT _err, _result, _id_devolucion;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_ventas_devoluciones_agregar(smallint, integer, integer, timestamp without time zone, character varying, smallint, numeric, smallint, character varying, numeric, numeric, numeric, numeric, numeric, numeric, numeric, numeric, smallint, integer, smallint, smallint, character varying, character, numeric, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_compras_devoluciones_agregar(
    _id_entidadcompra smallint,
    _numero integer,
    _id_proveedor integer,
    _fecha timestamp without time zone,
    _referencia character varying,
    _moneda smallint,
    _tc numeric,
    _condicion smallint,
    _obs character varying,
    _importe numeric,
    _descuento numeric,
    _subtotal numeric,
    _iva numeric,
    _total numeric,
    _fsipg_efectivo numeric,
    _fsipg_bancos numeric,
    _fsipg_cambio numeric,
    _id_bodega smallint,
    _id_factura integer,
    _id_vendedor smallint,
    _fsipg_id_concepto smallint,
    _fsipg_desc_concepto character varying,
    _devreb character,
    _ieps numeric,
    _ivaret numeric,
    _isrret numeric)
  RETURNS SETOF record AS
$BODY$ 
DECLARE 
	_err int; _result varchar(255); _ID_Devolucion int; _mes smallint; _ano smallint; _errpart int;  _resultpart varchar(255); _ID_Movimiento int; _concepto varchar(80); _conceptocost varchar(80); _conceptoDescuento varchar(80); _Ref varchar(25); _claseref varchar(25);  
	_FijaCost bit; _costcom numeric(19,4); _CC_COSTCOM char(19); _AuditarAlm bit;
	_ID_CXP int; _ID_CXPSALDO int; _numpol int; _clase varchar(1024); _bancajmov int;
	_tipoRetiro varchar(10); _Cantidad numeric(19,4);/* _ServComp bit; */_CC_PRO char(19); 
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_COMPNP char(19); _CC_DCAF char(19);
	_CC_DCEC char(19); _diff numeric(19,4); _TotDebe numeric(19,4); _TotHaber numeric(19,4); _contPart smallint; 
	/*_totPart smallint; */_CC_DEVREB char(19);
	_contban smallint; _totalban smallint; _ID_FormaPago smallint; _ID_BanCaj smallint; _RefPago varchar(25); _IdMon smallint;
	_id_clasificacion varchar(10); _numPagos smallint; _RefExt int;
	_Beneficiario varchar(80); _RFCOrigen varchar(15); _moneda_ce character(3); /*_CuentaOrigen varchar(80); _BancoOrigen character(3); _CuentaDestino varchar(50); _BancoDestino character(3);*/
	_RFC varchar(15);	
	--Iteracion
	_REC_TMP_PAGOS RECORD; _REC_TMP_COMPRAS_FACTURAS_DET RECORD; _TotalPesosMult numeric(19,4); _ParcialPesosMult numeric(19,4); _BanCaj smallint; _CC_BAN char(19);
BEGIN	
	_err := 0;
	_result := 'La devolucion se ha registrado satisfactoriamente';
	--_ServComp := '1';
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAAC');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAACPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSC');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSCPN');
	_CC_IVARet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVARETC');
	_CC_ISRRet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_ISRRETC');
	_CC_Desc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSC');
	IF _Condicion = 0
	THEN
		_CC_DEVREB := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSCCONT');
		_ConceptoDescuento := 'Devolucion y/o rebaja sobre compra de contado';
	ELSIF _Condicion = 1
	THEN	
		_CC_DEVREB := (select cc from TBL_PROVEE_CXP_CONCEPTOS where ID_Concepto = _fsipg_id_concepto);
		_ConceptoDescuento := _fsipg_desc_concepto;
	ELSE
		_CC_DEVREB := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSC');
		_ConceptoDescuento := 'Devolucion y/o rebaja sobre compra de crédito';
	END IF;
	_moneda_ce := (select id_satmoneda from TBL_CONT_MONEDAS where Clave = _moneda);
	_Cantidad := round((_Total * _TC), 2);
	_ImporteTotalPesos := ROUND((_Importe * _TC), 2);
	_IVAPesos :=  ROUND((_IVA * _TC), 2);
	_IEPSPesos :=  ROUND((_IEPS * _TC), 2);
	_IVARetPesos :=  ROUND((_IVARet * _TC), 2);
	_ISRRetPesos :=  ROUND((_ISRRet * _TC), 2);
	_DescPesos := ROUND((_Descuento * _TC), 2);
	_TotalPesos := ROUND((_Total * _TC), 2);	
	_concepto := 'Devolucion s/compra ' || (select Descripcion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_conceptocost := 'Costo dev/com ' || (select Descripcion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);

	_Beneficiario := ( select VAlfanumerico from TBL_VARIABLES where ID_Variable = 'EMPRESA');
	_RFCOrigen := case when _ID_Proveedor = 0 then 'XAXX010101000' else ( select RFC from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	--_CuentaOrigen := case when _ID_Proveedor = 0 then '' else ( select MetodoDePago from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	--_BancoOrigen := case when _ID_Proveedor = 0 then '000' else ( select ID_SatBanco from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;

	_CC_PRO := (select ID_CC from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor);
	_Fija := (select Fija from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	_FijaCost := (select FijaCost from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_AuditarAlm := ( select AuditarAlm from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega );
	_ID_CXP := ( select ID_CP from TBL_PROVEE_CXP where id_tipodocorig = 'CFAC' and id_clavedocorig = _ID_Factura);

	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_id_clasificacion := (select ID_Clasificacion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	_numPagos := (select count(*) from _TMP_PAGOS);

	_claseref := 'CFAC|' || cast(_ID_Factura as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;

	IF (select count(*) from TBL_COMPRAS_DEVOLUCIONES_CAB where ID_Entidad = _ID_EntidadCompra and Numero = _Numero) > 0
	THEN
		_err := 3;
		_result := 'ERROR: La clave de la devolucion ya existe, No se puede duplicar';
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF _Condicion = 0 
	THEN
		FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
		LOOP
			_ID_FormaPago := _REC_TMP_PAGOS.ID_FormaPago;
			_ID_BanCaj := _REC_TMP_PAGOS.ID_BanCaj;
			_FijaBAN := (case when _ID_FormaPago = 1 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			_IdMon := (case when _ID_FormaPago = 1 then (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);

			IF _Fija <> _FijaBAN
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra porque al pagarse de contado requiere que los bancos manejen los mismos trazos contables que la compra';
				EXIT;
			END IF;

			IF _Moneda <> _IdMon 
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra porque al pagarse de contado requiere que los bancos manejen la misma moneda que la compra';
				EXIT;
			END IF;
		END LOOP;
	END IF;
	
	IF _IVA > 0.0 AND ( _CC_IVA = '' OR _CC_IVAPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva acreditable efectivamente pagado o la de iva acreditable pendiente de pagar, no existe o no se ha enlazado';
	END IF;
	IF _IEPS > 0.0 AND ( _CC_IEPS = '' OR _CC_IEPSPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente pagado o la de IEPS pendiente de pagar no existe o no se ha enlazado';
	END IF;
	IF _IVARet > 0.0 AND ( _CC_IVARet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de IVA Retenido no existe o no se ha enlazado';
	END IF;
	IF _ISRRet > 0.0 AND ( _CC_ISRRet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de ISR Retenido no existe o no se ha enlazado';
	END IF;
	IF _Descuento > 0.0 AND _CC_Desc = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de descuento sobre compras no existe o no se ha enlazado';
	END IF;

	IF _CC_DEVREB is null or _CC_DEVREB = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de devoluciones sobre compras de contado o crédito no se ha enlazado en variables, o en enlaces de cuentas por cobrar';
	END IF;

	-- procede a realizar verificaci?n sobre los trazos
	IF _FijaCost = '0'
	THEN
		_CC_COSTCOM := (select cc from TBL_INVSERV_COSTOS_CONCEPTOS where ID_Concepto = 53);
		IF _CC_COSTCOM is null
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para el costo de compras no existe o no se ha enlazado bien';	
		END IF;
	END IF;

	IF _Condicion = 3
	THEN
		_CC_COMPNP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMPNP');
		IF _CC_COMPNP = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para registro de partida doble cuando no se establece ningun metodo de pago, no existe o no se ha enlazado';
		END IF;
	END IF;
	
	IF _err = 0
	THEN
		INSERT INTO TBL_COMPRAS_DEVOLUCIONES_CAB(  id_entidad, numero, id_clipro, fecha, referencia, status, moneda, tc, fechaenvio, condicion, obs, 
			importe, descuento, subtotal, iva, total, ref, id_pol, id_polcost, id_bodega, mimporte, mdescuento, msubtotal, miva, mtotal, efectivo, bancos, cambio, id_vendedor, id_cfd, tfd, 
			ieps, ivaret, isrret, id_factura, devreb )
		VALUES(_ID_EntidadCompra, _Numero, _ID_Proveedor, _Fecha, _Referencia, (case when _AuditarAlm = '1' and _devreb = 'DEV' then 'G' else 'E' end) , _Moneda, _TC, _Fecha, _Condicion, _Obs,
				_Importe, _Descuento, _SubTotal, _IVA, _Total, _claseref, null, null, _ID_Bodega, _Importe, _Descuento, _SubTotal, _IVA,  _Total, _FSIPG_Efectivo, _FSIPG_Bancos, _FSIPG_Cambio, _ID_Vendedor, null, null, 
				_ieps, _ivaret, _isrret, _id_factura, _devreb)
		RETURNING currval(pg_get_serial_sequence('TBL_COMPRAS_DEVOLUCIONES_CAB', 'id_vc')) INTO _id_devolucion;
		 
		-- actualiza el numero de FACTURA
		UPDATE TBL_COMPRAS_ENTIDADES
		SET Devolucion = _Numero + 1
		WHERE ID_EntidadCompra = _ID_EntidadCompra;
		
		-- inserta el detalle
		INSERT INTO TBL_COMPRAS_DEVOLUCIONES_DET
		SELECT _ID_Devolucion, Partida, Cantidad, ID_Prod, Precio, Descuento, IVA, Obs, Importe, ImporteDesc, ImporteIVA, TotalPart, IEPS, ImporteIEPS, IVARet, ImporteIVARet, ISRRet, ImporteISRRet
		FROM _TMP_COMPRAS_FACTURAS_DET;

		_Ref := 'CDEV|' || cast(_ID_Devolucion as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

		--Crea tabla temporal indispensable... para contable.
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_TMP (
			Part smallint NOT NULL ,
			Cuenta char(19) NOT NULL ,
			Concepto varchar(80) NOT NULL ,
			Parcial numeric(19,4) NOT NULL ,
			Moneda smallint NOT NULL ,
			TC numeric(19,4) NOT NULL ,
			Debe numeric(19,4) NOT NULL ,
			Haber numeric(19,4) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP( 
			id_part smallint NOT NULL,
			num character varying(20) NOT NULL,
			banco character(3) NOT NULL,
			ctaori character varying(50) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			monto numeric(19,4) NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			banemisext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		); 
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP ( 
			id_part smallint NOT NULL,
			ctaori character varying(50) NOT NULL,
			bancoori character(3) NOT NULL,
			monto numeric(19,4) NOT NULL,
			ctadest character varying(50) NOT NULL,
			bancodest character(3) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			bancooriext character varying(150) NOT NULL,
			bancodestext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP (
			id_part smallint NOT NULL,
			metpagopol character(2) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			monto numeric(19,4) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		-- Agrega a la tabla de temporal para: polizas final o detalles de bancos cajas final
		_contPart := 1;
		INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
		VALUES(_contPart, _CC_DEVREB, _ConceptoDescuento, _Importe, _Moneda, _TC, 0.0, _ImporteTotalPesos);

		-- procede a registrar los otros conceptos de la poliza ( EL IVA , Deuda con Proveedor ETC)
		IF _IVA > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a descuento de iva efectivamente pagado
			THEN
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVA, 'Parte de Impuesto devuelta', _IVA, _Moneda, _TC, 0.0, _IVAPesos);
			ELSE
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVAPN, 'Parte de Impuesto devuelta', _IVA, _Moneda, _TC, 0.0, _IVAPesos);
			END IF;
		END IF;

		IF _IEPS > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a ieps a efectivamente pagado
			THEN	
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, 0.0, _IEPSPesos);	
			ELSE -- de credito. o ningun método de pago. envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPSPN, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, 0.0, _IEPSPesos);	
			END IF;
		END IF;

		IF _IVARet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IVARet, 'Retención de IVA', _IVARet, _Moneda, _TC, _IVARetPesos, 0.0);	
		END IF;

		IF _ISRRet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_ISRRet, 'Retención de ISR', _ISRRet, _Moneda, _TC, _ISRRetPesos, 0.0);	
		END IF;
		
		IF _Descuento > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_Desc, 'Parte del descuento explicito devuelta', _Descuento, _Moneda, _TC, _DescPesos, 0.0);
		END IF;
			
		
		-- procede a registrar los movimientos en la caja, banco, Cuenta por cobrar, y la poliza en caso necesario
		IF _ID_Proveedor > 0 and _Condicion = 1 -- si es a credito y no de mostrador
		THEN
			SELECT * INTO _errpart, _resultpart, _id_cxpsaldo 
			FROM sp_provee_cxp_pagar(_ID_EntidadCompra, _ID_CXP, _Fecha,  '', _Moneda, _TC, _Total, '0', '0', _Cantidad, _ConceptoDescuento, cast(_ID_CXP as varchar), '1', _fsipg_id_concepto, false, 'CDEV', _ID_Devolucion, _Ref, null, null, null, null, null, null ) as ( err integer, res varchar, clave integer ); --este movimiento registrará la póliza externa _numpol 
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			ELSE
				_clase = 'CCXP|' || cast(_ID_CXPSALDO as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

				UPDATE TBL_COMPRAS_DEVOLUCIONES_CAB
				SET ID_Pol = _ID_CXPSALDO
				WHERE ID_VC = _ID_Devolucion;
				
				-- Procede a registrar la poliza si y solo si es una entidad dinamica
				-- Primero registra la deuda total del proveee para la partida doble en tmp
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_PRO, 'Devolución del Proveedor', _Total, _Moneda, _TC, _TotalPesos, 0.0);
					
				IF _Fija = '0'
				THEN
					-- Primero registra y crea la tabla temporal de detalle de la poliza
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
						Part smallint NOT NULL ,
						Cuenta char(19) NOT NULL ,
						Concepto varchar(80) NOT NULL ,
						Parcial numeric(19,4) NOT NULL ,
						Moneda smallint NOT NULL ,
						TC numeric(19,4) NOT NULL ,
						Debe numeric(19,4) NOT NULL ,
						Haber numeric(19,4) NOT NULL
					);
					-- Fin de la tabla temporal

					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
					_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					IF _TotDebe > _TotHaber
					THEN
						_diff := _TotDebe - _TotHaber;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
					ELSIF _TotDebe < _TotHaber
					THEN
						_diff := _TotHaber - _TotDebe;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
					END IF;
					
					-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;

					-- Agrega ahora la poliza principal
					--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
					SELECT * INTO _errpart, _resultpart, _numpol 
					FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
					ELSE
						UPDATE  TBL_PROVEE_CXP
						SET ID_Pol = _numpol
						WHERE ID_CP = _ID_CXPSALDO;
					END IF;

					DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
			
				END IF;
			END IF; 
		ELSIF _Condicion = 0 -- es de contado
		THEN
			-- Termina la poliza dividiendo los pagos en sus cuentas de banco o caja
			_TotalPesosMult = 0.0;
			_ParcialPesosMult = 0.0;
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_ParcialPesosMult := round((_REC_TMP_PAGOS.Total * _TC), 2);
				_TotalPesosMult := _TotalPesosMult + _ParcialPesosMult;
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				_CC_BAN := (select CC from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_CuentaDestino := (select Descripcion from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_BancoDestino := (select ID_SatBanco from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				_contPart := _contPart + 1;
				
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_BAN, _REC_TMP_PAGOS.RefPago, _REC_TMP_PAGOS.Total, _Moneda, _TC, _ParcialPesosMult, 0.0);
				--Ingresa contablilidad electronica del pago...
				-- Actualiza la contabilidad electronica de este BANCO o CAJA a esta partida, ya sea por cheque, transferencia u otro metodo de pago
				IF _BanCaj = 0 -- Es banco
				THEN
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSIF _REC_TMP_PAGOS.ID_SatMetodosPago = '03' --Es una transferencia
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP
						SELECT _contPart, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.Total, Descripcion, ID_SatBanco, _Fecha,  _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.BancoExt, BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				ELSE -- BanCaj = 1 "Caja"
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque (Deposito o Retiro es lo mismo, toma los datos directos capturados en el dialogo)
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				END IF;
			
			END LOOP;

			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES ( 
					id_part smallint NOT NULL,
					num character varying(20) NOT NULL,
					banco character(3) NOT NULL,
					ctaori character varying(50) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					monto numeric(19,4) NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					banemisext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				); 
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS ( 
					id_part smallint NOT NULL,
					ctaori character varying(50) NOT NULL,
					bancoori character(3) NOT NULL,
					monto numeric(19,4) NOT NULL,
					ctadest character varying(50) NOT NULL,
					bancodest character(3) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					bancooriext character varying(150) NOT NULL,
					bancodestext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO (
					id_part smallint NOT NULL,
					metpagopol character(2) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					monto numeric(19,4) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				-- Fin de la tabla temporal

				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;

				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
								
				-- Agrega ahora la poliza principal
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('IG', _Fecha, _Concepto,'0', '', _TotalPesos, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				END IF;
				
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES; 
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO;
			END IF;

			--Ahora ejecuta los movimientos de caja y bancos
			_clase := '';
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
					
				SELECT * INTO _errpart, _resultpart, _bancajmov 
				FROM  sp_bancos_movs_agregar( _BanCaj, _REC_TMP_PAGOS.ID_BanCaj, _Fecha, _Concepto, _Beneficiario, _REC_TMP_PAGOS.Total, 0.00, _REC_TMP_PAGOS.TipoMov, 
					'G', _Moneda, _TC, _Ref, _REC_TMP_PAGOS.RefPago, _id_clasificacion, _numpol, _REC_TMP_PAGOS.ID_SatBanco, _RFCOrigen, _REC_TMP_PAGOS.ID_SatMetodosPago, 
																		_REC_TMP_PAGOS.BancoExt, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.Cheque) as ( err integer, res varchar, clave integer);
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
					EXIT;
				END IF;

				INSERT INTO TBL_COMPRAS_DEVOLUCIONES_PAGOS
				VALUES(_ID_Devolucion, _bancajmov);
				
				IF _numPagos > 1
				THEN
					IF _BanCaj = 0
					THEN
						_clase := _clase || 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					ELSE
						_clase := _clase || 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					END IF;
				ELSE
					IF _BanCaj = 0
					THEN
						_clase := 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					ELSE
						_clase := 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0' -- Aqui registra la referencia de las polizas de ingreso a sus movimientos de caja o bancos
			THEN
				IF _numPagos > 1
				THEN
					INSERT INTO TBL_REFERENCIAS_EXT
					VALUES(default,_clase)
					RETURNING currval(pg_get_serial_sequence(' TBL_REFERENCIAS_EXT', 'id_ref')) INTO _RefExt;
					UPDATE TBL_CONT_POLIZAS
					SET Ref = 'REXT|' || cast(_RefExt as varchar) || '|||'
					WHERE ID = _numpol;
		 		ELSE
					UPDATE TBL_CONT_POLIZAS
					SET Ref = _clase
					WHERE ID = _numpol;
				END IF;
			END IF;
		ELSIF _Condicion = 3 -- ningun metodo de pago
		THEN
			_clase = 'CDEV|' || cast(_ID_Devolucion as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';
				
			-- Procede a registrar la poliza si y solo si es una entidad dinamica
			-- Primero registra la deuda total del proveee para la partida doble en tmp
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_COMPNP, 'Devolución/rebaja del documento por pagar', _Total, _Moneda, _TC, _TotalPesos, 0.0);
					
			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				-- Fin de la tabla temporal
				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;
				
				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				-- Agrega ahora la poliza principal
				--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					UPDATE TBL_COMPRAS_DEVOLUCIONES_CAB
					SET ID_Pol = _numpol
					WHERE ID_VC = _ID_Devolucion;
				END IF;

				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
			
			END IF;
		END IF;
		
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP; 
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
		
		--Procede a agregar el movimiento al almacén
		IF _err = 0 AND _FijaCost = '0' AND _DevReb = 'DEV' and (select count(*) from _TMP_COMPRAS_FACTURAS_DET where Tipo = 'P') > 0
		THEN
			CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (
				ID_Bodega smallint NOT NULL ,
				ID_Prod varchar(20) NOT NULL ,
				Partida smallint NOT NULL ,
				Cantidad numeric(9, 3) NOT NULL ,
				Costo numeric(19,4) NULL 
			); 

			insert into _TMP_INVSERV_ALMACEN_MOVIM_DET
			select _ID_Bodega, ID_Prod, Partida, Cantidad, Precio 
			from _TMP_COMPRAS_FACTURAS_DET
			order by Partida ASC;
		
			SELECT * INTO _errpart, _resultpart, _ID_Movimiento 
			FROM sp_invserv_alm_movs_agregar(_Fecha, _ID_Bodega, (case when _AuditarAlm = '1' then 'P' else 'U' end), '53', _ConceptoCost, '',/*1 ENT 2 SAL*/ '2', _Ref, 'CDEV', _ID_Devolucion) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				UPDATE TBL_COMPRAS_DEVOLUCIONES_CAB
				SET ID_PolCost = _ID_Movimiento
				WHERE ID_VC = _ID_Devolucion;
			END IF;
	
			DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET; 
		END IF;
		-- Fin del movimiento al almacen
		
	END IF; 
	
	RETURN QUERY SELECT _err, _result, _id_devolucion;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_devoluciones_agregar(smallint, integer, integer, timestamp without time zone, character varying, smallint, numeric, smallint, character varying, numeric, numeric, numeric, numeric, numeric, numeric, numeric, numeric, smallint, integer, smallint, smallint, character varying, character, numeric, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ventas_devoluciones_cancelar(
    _id_devolucion integer,
    _id_entidadventa smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_Numero int; _Fecha timestamp; _ID_Pol int; _ID_PolCost int; _err int; _result varchar(255); _errpart int; 
	_resultpart varchar(255); _Ref varchar(25); _ID_CXC int; _ID_Movimiento int; _ID_Bodega smallint;
 	_ID_BanCaj int; _Condicion smallint; _mes smallint; _ano smallint; 
 	_ID_CFD int; _TFD smallint; _EntCFD bit(2); _ID_CFDRES int;
 	_REC_TMP_PAGOS RECORD; 
BEGIN
	_err := 0;
	_result := 'La Devolución se ha cancelado satisfactoriamente';
	_Fecha := (select Fecha from TBL_VENTAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion);
	_Numero := (select Numero from TBL_VENTAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion);
	_ID_Movimiento := (select ID_PolCost from TBL_VENTAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion); -- el id del movimiento al almacen
	_ID_CXC := (select ID_Pol from TBL_VENTAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion); 
	_ID_Bodega := (select ID_Bodega from TBL_VENTAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion);
	_Condicion := (select Condicion from TBL_VENTAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion);
	
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	
	_ID_CFD := (select ID_CFD from TBL_VENTAS_DEVOLUCIONES_CAB WHERE ID_VC = _ID_Devolucion);
	_TFD := (select TFD from TBL_VENTAS_DEVOLUCIONES_CAB WHERE ID_VC = _ID_Devolucion);
	_EntCFD := (select CFD from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);

	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF( _TFD = 1 or _TFD = 2 )
	THEN
		_err := 3;
		_result := 'ERROR: La factura no esta sellada completamente, puede que falte generar el PDF. Generalo para poderla cancelar';	
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
		SET Status = 'C'
		WHERE ID_VC = _ID_Devolucion;
		
		-- procede a cancelar el CFD
		IF _ID_CFD is not null
		THEN
			IF (select count(*) from tbl_cfdven where id_cfd = _id_cfd) = 0 -- El CFDI se generó internamente
			THEN
				select * into _errpart, _resultpart, _id_cfdres from sp_cfd_cancelar(_ID_CFD) as (err int,  resultpart varchar, clave int);
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
				ELSE
					UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
					SET TFD = 3
					WHERE ID_VC = _ID_Devolucion;
				END IF;
			ELSE -- Este CFDI fue cargado y enlazado a este registro
				IF (select FSI_Tipo from TBL_CFDVEN where ID_CFD = _ID_CFD) = 'DSV' and (select FSI_ID from TBL_CFDVEN where ID_CFD = _ID_CFD) = _ID_Devolucion
				THEN 
					UPDATE TBL_CFDVEN
					SET FSI_Tipo = 'ENT', FSI_ID = _ID_EntidadVenta
					WHERE ID_CFD = _ID_CFD;
					UPDATE TBL_CFD
					SET FSI_Tipo = 'ENT', FSI_ID = _ID_EntidadVenta
					WHERE ID_CFD = _ID_CFD;
				END IF;
				
				UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
				SET ID_CFD = null
				WHERE ID_VC = _ID_Devolucion;
			END IF;
		ELSIF _EntCFD <> '00'
		THEN
			UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
			SET TFD = 3
			WHERE ID_VC = _ID_Devolucion;
		END IF;
		-- FIN CFD

		--Procede a la cancelacion de la CXC o PAGOS
		IF _Condicion = 0
		THEN
			FOR _REC_TMP_PAGOS IN  (select ID_Mov from  tbl_ventas_devoluciones_pagos where ID_Devolucion = _ID_Devolucion)
			LOOP
				SELECT * INTO _errpart, _resultpart, _id_bancaj FROM sp_bancos_movs_cancelar(_REC_TMP_PAGOS.ID_Mov) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
					EXIT;
				END IF;
			END LOOP;
		ELSIF _Condicion = 1
		THEN
			SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_client_cxc_cancelar(_id_cxc) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			END IF;
		ELSE --Ningun pago... id_cxc es en realidad la poliza del documento por cobrar
			--Procede a cancelar la poliza
			IF _ID_CXC is not null
			THEN
				SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_cont_polizas_cancelar(_ID_CXC, _Fecha) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
				END IF;
			END IF;
		END IF;
		
		-- procede a cancelar los detalles del movimiento al almacen
		IF _err = 0 AND _ID_Movimiento is not null
		THEN
			SELECT * INTO _errpart, _resultpart, _id_polcost FROM sp_invserv_alm_movs_cancelar( _id_movimiento ) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			END IF;
		END IF;

	END IF;		

	RETURN QUERY SELECT _err, _result, _ID_Devolucion;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_ventas_devoluciones_cancelar(integer, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_compras_devoluciones_cancelar(
    _id_devolucion integer,
    _id_entidadcompra smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_Numero int; _Fecha timestamp; _ID_Pol int; _ID_PolCost int; _err int; _result varchar(255); _errpart int; 
	_resultpart varchar(255); _Ref varchar(25); _ID_CXP int; _ID_Movimiento int; _ID_Bodega smallint;
 	_ID_BanCaj int; _Condicion smallint; _mes smallint; _ano smallint; 
 	_ID_CFD int; _TFD smallint;
 	_REC_TMP_PAGOS RECORD; 
BEGIN
	_err := 0;
	_result := 'La Devolución se ha cancelado satisfactoriamente';
	_Fecha := (select Fecha from TBL_COMPRAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion);
	_Numero := (select Numero from TBL_COMPRAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion);
	_ID_Movimiento := (select ID_PolCost from TBL_COMPRAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion); -- el id del movimiento al almacen
	_ID_CXP := (select ID_Pol from TBL_COMPRAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion); 
	_ID_Bodega := (select ID_Bodega from TBL_COMPRAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion);
	_Condicion := (select Condicion from TBL_COMPRAS_DEVOLUCIONES_CAB where ID_VC = _ID_Devolucion);
	
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	
	_ID_CFD := (select ID_CFD from TBL_COMPRAS_DEVOLUCIONES_CAB WHERE ID_VC = _ID_Devolucion);
	_TFD := (select TFD from TBL_COMPRAS_DEVOLUCIONES_CAB WHERE ID_VC = _ID_Devolucion);

	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_COMPRAS_DEVOLUCIONES_CAB
		SET Status = 'C'
		WHERE ID_VC = _ID_Devolucion;
		
		--procede a cancelar el CFD
		IF _ID_CFD is not null
		THEN
			IF (select FSI_Tipo from TBL_CFDCOMP where ID_CFD = _ID_CFD) = 'DSC' and (select FSI_ID from TBL_CFDCOMP where ID_CFD = _ID_CFD) = _ID_Devolucion
			THEN 
				UPDATE TBL_CFDCOMP
				SET FSI_Tipo = 'ENT', FSI_ID = _ID_EntidadCompra
				WHERE ID_CFD = _ID_CFD;
			END IF;
			
			UPDATE TBL_COMPRAS_DEVOLUCIONES_CAB
			SET ID_CFD = null
			WHERE ID_VC = _ID_Devolucion;
		END IF;

		--Procede a la cancelacion de la CXP o PAGOS
		IF _Condicion = 0
		THEN
			FOR _REC_TMP_PAGOS IN  (select ID_Mov from  tbl_compras_devoluciones_pagos where ID_Devolucion = _ID_Devolucion)
			LOOP
				SELECT * INTO _errpart, _resultpart, _id_bancaj FROM sp_bancos_movs_cancelar(_REC_TMP_PAGOS.ID_Mov) as ( err integer, res varchar, clave integer );
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _resultpart;
					EXIT;
				END IF;
			END LOOP;
	
		ELSE
			SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_provee_cxp_cancelar(_id_cxp) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			END IF;

		END IF;
		
		-- procede a cancelar los detalles del movimiento al almacen
		IF _err = 0 AND _ID_Movimiento is not null
		THEN
			SELECT * INTO _errpart, _resultpart, _id_polcost FROM sp_invserv_alm_movs_cancelar( _id_movimiento ) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			END IF;
		END IF;

	END IF;		

	RETURN QUERY SELECT _err, _result, _ID_Devolucion;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_devoluciones_cancelar(integer, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
insert into tbl_formatos
values('FSI-NNOM','Recibos de nomina CFDI','NOM_NOMINA');

insert into tbl_formatos_det
values('FSI-NNOM',1,'FSI_CAB','',null,null,null,259.50,'S','000000',null,null);
insert into tbl_formatos_det
values('FSI-NNOM',2,'FSI_VENTANA','',null,null,800.00,600.00,'S','000000',null,null);
insert into tbl_formatos_det
values('FSI-NNOM',3,'FSI_DET','18',null,95.00,null,2.50,'S','000000',null,null);
insert into tbl_formatos_det
values('FSI-NNOM',4,'FSI_IMPTIT','font-family: Arial, Helvetica, sans-serif; font-size: 16pt; font-style: italic; font-weight: bold;',null,null,null,null,'S','000000',null,null);
insert into tbl_formatos_det
values('FSI-NNOM',5,'FSI_IMPETQ','font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: bold;',null,null,null,null,'S','000000',null,null);
insert into tbl_formatos_det
values('FSI-NNOM',6,'FSI_IMPCAB','font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: normal;',null,null,null,null,'S','000000',null,null);
insert into tbl_formatos_det
values('FSI-NNOM',7,'FSI_IMPDET','font-family: Arial, Helvetica, sans-serif; font-size: 7pt; font-style: normal; font-weight: normal;',null,null,null,null,'S','000000',null,null);
insert into tbl_formatos_det
values('FSI-NNOM',8,'FSI_TITULO','RECIBO DE NOMINA',0.00,0.00,95.00,10.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',9,'FSI_ETIQUETA','Derección',0.00,25.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',10,'FSI_ETIQUETA','NoExt/NoInt',40.00,25.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',11,'FSI_ETIQUETA','Colonia',0.00,30.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',12,'FSI_ETIQUETA','Localidad',40.00,30.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',13,'FSI_ETIQUETA','Municipio',0.00,35.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',14,'FSI_ETIQUETA','Estado',40.00,35.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',15,'FSI_ETIQUETA','C.P.',0.00,40.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',16,'FSI_ETIQUETA','Pais: México',40.00,40.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',17,'FSI_TITULO','Nombre de Empresa',0.00,10.00,95.00,7.50,'C','000000','left','bottom');
insert into tbl_formatos_det
values('FSI-NNOM',18,'FSI_TITULO','RFC: XXXX######XXX',0.00,17.50,95.00,7.50,'C','000000','left','bottom');
insert into tbl_formatos_det
values('FSI-NNOM',19,'FSI_ETIQUETA','FOLIO FISCAL',95.00,0.00,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',20,'TFD_UUID','general',95.00,2.50,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',21,'FSI_ETIQUETA','SERIE CERTIFICADO SAT',95.00,5.00,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',22,'TFD_NoCertificadoSAT','general',95.00,7.50,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',23,'FSI_ETIQUETA','FECHA Y HORA DE CERTIFICACION',95.00,10.00,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',24,'TFD_FechaTimbre','dd/MMM/yyyy hh:mm:ss',95.00,12.50,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',25,'FSI_ETIQUETA','FECHA DE ELABORACION',95.00,15.00,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',26,'CFD_Fecha','dd/MMM/yyyy hh:mm:ss',95.00,17.50,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',27,'FSI_ETIQUETA','DATOS DEL RECEPTOR',0.00,50.00,95.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',28,'FSI_ETIQUETA','RFC:',0.00,55.00,10.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',29,'RFC','general',10.00,55.00,30.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',30,'Nombre','general',0.00,60.00,95.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',31,'Calle','general',0.00,65.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',32,'NoExt','general',40.00,65.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',33,'NoInt','general',60.00,65.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',34,'Colonia','general',0.00,70.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',35,'Localidad','general',40.00,70.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',36,'Municipio','general',0.00,75.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',37,'Estado','general',40.00,75.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',38,'CP','general',0.00,80.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',39,'Pais','general',40.00,80.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',40,'FSI_ETIQUETA','EMITIDO EN:',105.00,50.00,85.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',41,'CFD_Nombre','general',105.00,55.00,85.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',42,'CFD_Calle','general',105.00,60.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',43,'CFD_NoExt','general',145.00,60.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',44,'CFD_NoInt','general',165.00,60.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',45,'CFD_Colonia','general',105.00,65.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',46,'CFD_Localidad','general',145.00,65.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',47,'CFD_Municipio','general',105.00,70.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',48,'CFD_Estado','general',145.00,70.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',49,'CFD_CP','general',105.00,75.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',50,'CFD_Pais','general',145.00,75.00,40.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',51,'FSI_ETIQUETA','DOC INT:',105.00,80.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',52,'Numero',' |0',125.00,80.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',53,'FSI_ETIQUETA','FSI_QRCODE',0.00,145.00,55.00,55.00,'C','000000','left','top');
insert into tbl_formatos_det
values('FSI-NNOM',54,'FSI_ETIQUETA','ESTE DOCUMENTO ES UNA REPRESENTACION IMPRESA DE UN CFDI',0.00,245.00,190.00,5.00,'C','000000','center','middle');
insert into tbl_formatos_det
values('FSI-NNOM',55,'TFD_CadenaOriginal','general',0.00,235.00,190.00,10.00,'C','000000','left','top');
insert into tbl_formatos_det
values('FSI-NNOM',56,'FSI_ETIQUETA','CADENA ORIGINAL DEL COMPLEMENTO DE CERTIFICACION DEL SAT',0.00,230.00,190.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',57,'TFD_SelloSAT','general',0.00,220.00,190.00,10.00,'C','000000','left','top');
insert into tbl_formatos_det
values('FSI-NNOM',58,'FSI_ETIQUETA','SELLO DEL SAT',0.00,215.00,190.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',59,'CFD_Sello','general',0.00,205.00,190.00,10.00,'C','000000','left','top');
insert into tbl_formatos_det
values('FSI-NNOM',60,'FSI_ETIQUETA','SELLO DEL CFDI',0.00,200.00,190.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',61,'FSI_ETIQUETA','CVE',0.00,90.00,17.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',62,'FSI_ETIQUETA','DESCRIPCION',17.00,90.00,113.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',63,'FSI_ETIQUETA','PERCEPCIONES',130.00,90.00,30.00,5.00,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',64,'FSI_ETIQUETA','DEDUCCIONES',160.00,90.00,30.00,5.00,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',65,'Total','LETRA',55.00,160.00,85.00,10.00,'C','000000','left','top');
insert into tbl_formatos_det
values('FSI-NNOM',66,'FSI_ETIQUETA','PAGO EN UNA SOLA EXHIBICION',55.00,155.00,85.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',67,'FSI_ETIQUETA','SUB TOTAL',140.00,150.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',68,'FSI_ETIQUETA','ISR',140.00,160.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',69,'FSI_ETIQUETA','TOTAL',140.00,165.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',70,'FSI_ETIQUETA','REF:',145.00,80.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',71,'Referencia','general',165.00,80.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',72,'FSI_ETIQUETA','REGIMEN FISCAL: Regimen General De Ley',95.00,30.00,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',73,'FSI_ETIQUETA','LUGAR DE EXPEDICION',95.00,20.00,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',74,'CFD_LugarExpedicion','general',95.00,22.50,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',75,'FSI_ETIQUETA','METODO DE PAGO',95.00,25.00,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',76,'CFD_MetodoDePago','general',95.00,27.50,95.00,2.50,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',77,'subtotal',',|.|2|0',160.00,150.00,30.00,5.00,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',78,'FSI_ETIQUETA','DESCUENTO',140.00,155.00,20.00,5.00,'C','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',79,'descuento',',|.|2|0',160.00,155.00,30.00,5.00,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',80,'total',',|.|2|0',160.00,165.00,30.00,5.00,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',81,'isr',',|.|2|0',160.00,160.00,30.00,5.00,'C','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',82,'id_movimiento',' |0',0.00,0.00,17.00,5.00,'D','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',83,'deduccion',',|.|2|1',160.00,0.00,30.00,5.00,'D','000000','right','middle');
insert into tbl_formatos_det
values('FSI-NNOM',84,'descripcion','general',17.00,0.00,113.00,5.00,'D','000000','left','middle');
insert into tbl_formatos_det
values('FSI-NNOM',85,'percepcion',',|.|2|1',130.00,0.00,30.00,5.00,'D','000000','right','middle');

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_calculo_nomina_generar(_id_nomina integer)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(4000); _errpart int; _resultpart varchar(255); _numpol int; _bancajmov int; _clase varchar(50);
		_banresult varchar(255); _Tipo smallint; _Numero_Nomina smallint; _Ano smallint; _Fecha timestamp; _Mes smallint; 
					_ID_Sucursal smallint; _ContCuenTipo smallint; _ContCuenClave smallint; _FijaCuenTipo smallint; _FijaCuenClave smallint; 
					_BanTipo smallint; _BanClave smallint; _BanFijo bit; _Concepto varchar(80); _id_clasificacion varchar(10); _Ref varchar(25); 
					_contTotal smallint; _contNum smallint; _contTotalDep smallint; _contNumDep smallint; _contPart smallint; _CC character(19);
	-- segundo declare
	_ID_Movimiento smallint; _ID_Departamento char(4); _Departamento varchar(40); _ID_Cuenta char(19); _Gravado numeric(19,4); _Exento numeric(19,4); _Deduccion numeric(19,4); _TotalBAN numeric(19,4);
	-- contabilidad electronica
	_CE boolean; _CuentaOrigen varchar(50); _BancoOrigen character(3); _REC_RECIBOS RECORD; _UUID character(36); _id_pol integer; _id_poliza integer; _id_part smallint; _id_satMP character(2);
	 --iteracion
	_REC_MOVIMIENTOS RECORD; _REC_DEPARTAMENTO RECORD; _REC_MASEMP RECORD; 
		
BEGIN
	_err := 0;
	_result := 'La nómina ha quedado generada y el pago se ha aplicado correctamente';
	-- Define el tipo de nomina para saber si existe nomina de vales
	_Tipo := (select Tipo from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
	_Numero_Nomina := (select Numero_Nomina from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
	_Ano := (select Ano from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
	_Fecha := (select Fecha_Hasta from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
	_ID_Sucursal := (select ID_Sucursal from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
	_Mes := date_part('month',_Fecha);
	_Ano := date_part('year',_Fecha);
	_id_clasificacion := (select ID_Clasificacion from TBL_COMPANIAS where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal);
	_Concepto := 'Pago nómina ' || (select Descripcion from TBL_COMPANIAS where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal) || ' ' || cast(_Numero_Nomina as varchar) || ' - ' || cast(_Ano as varchar) || ' Tipo ' || cast(_Tipo as varchar);  
	_ContCuenTipo := (select ContCuenTipo from TBL_COMPANIAS where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal);
	_ContCuenClave := (select ContCuenClave from TBL_COMPANIAS where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal);
	_FijaCuenTipo := (select FijaCuenTipo from TBL_COMPANIAS where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal);
	_FijaCuenClave := (select FijaCuenClave from TBL_COMPANIAS where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal);
	
	_Ref := 'NNOM|' || cast(_ID_Nomina as varchar) || '|' || cast(_ID_Sucursal as varchar) || '||';
			
	IF(_Tipo = 3 or _Tipo = 4) -- Significa nomina especial
	THEN
		_CE := false;
		_BanTipo := (select Tipo from TBL_BANCOS_CUENTAS where Tipo = _FijaCuenTipo and Clave = _FijaCuenClave);
		_BanClave := (select Clave from TBL_BANCOS_CUENTAS where Tipo = _FijaCuenTipo and Clave = _FijaCuenClave);
		_BanFijo := (select Fijo from TBL_BANCOS_CUENTAS where Tipo = _FijaCuenTipo and Clave = _FijaCuenClave);
		_CC := (select CC from TBL_BANCOS_CUENTAS where Tipo = _FijaCuenTipo and Clave = _FijaCuenClave);
		
		IF _BanFijo = '0' -- el banco o caja no es fijo, sino contable... Error porque intentamos pagar nomina especial
		THEN
			_err := 3;
			_result := 'ERROR: No se puede pagar una nómina especial con banco o cajas contables'; 
		END IF;
	ELSE
		_CE := true;
		_BanTipo := (select Tipo from TBL_BANCOS_CUENTAS where Tipo = _ContCuenTipo and Clave = _ContCuenClave);
		_BanClave := (select Clave from TBL_BANCOS_CUENTAS where Tipo = _ContCuenTipo and Clave = _ContCuenClave);
		_BanFijo := (select Fijo from TBL_BANCOS_CUENTAS where Tipo = _ContCuenTipo and Clave = _ContCuenClave);
		_CC := (select CC from TBL_BANCOS_CUENTAS where Tipo = _ContCuenTipo and Clave = _ContCuenClave); -- la cuenta contable del banco
		
		IF _BanFijo = '1' -- el banco o caja no es contable sino fija... Error porque intentamos pagar nomina NO  especial
		THEN
			_err := 3;
			_result := 'ERROR: No se puede pagar una nómina contable con banco o cajas fijas'; 
		ELSE
			IF _BanTipo = 0 -- Se paga con bancos
			THEN
				FOR _REC_MASEMP IN ( SELECT ID_Empleado, Cuenta_Bancaria, ID_SatBanco FROM TBL_NOM_MASEMP WHERE ID_Empleado
												IN (SELECT ID_Empleado FROM TBL_NOM_CALCULO_NOMINA_ESP WHERE ID_Nomina = _ID_Nomina) ) 
				LOOP -- por los distintos empleados
					IF _REC_MASEMP.Cuenta_Bancaria = '' OR _REC_MASEMP.ID_SatBanco = '000'
					THEN
						_err := 3;
						_result := 'ERROR: No existe soporte para contabilidad electrónica para las transferencias al empleado. Clave: ' || _REC_MASEMP.ID_Empleado; 
						EXIT;
					END IF; 
				END LOOP;
			END IF; -- No es transferencia, se paga en efectivo
		END IF;
	END IF;

	IF (select count(*) from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina) < 1 -- Si no existe la nomina revisa:
	THEN
		_err := 3;
		_result := 'ERROR: Esta nómina no existe. No se puede generar'; 
	END IF;
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
		OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF(_BanTipo is null or _BanClave is null)
	THEN
		_err := 3;
		_result := 'ERROR: No se han enlazado bancos o cajas para generar este tipo de nómina.'; 
	ELSIF (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = _BanTipo and Clave = _BanClave) <> 1
	THEN
		_err := 3;
		_result := 'ERROR: El banco o caja enlazado tiene una moneda extranjera. No se puede generar la nómina'; 
	END IF;

	IF _err = 0
	THEN
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE2 (
			Part smallint NOT NULL ,
			Cuenta char(19) NOT NULL ,
			Concepto varchar(80) NOT NULL ,
			Parcial numeric(19,4) NOT NULL ,
			Moneda smallint NOT NULL ,
			TC numeric(19,4) NOT NULL ,
			Debe numeric(19,4) NOT NULL ,
			Haber numeric(19,4) NOT NULL 
		);

		_contPart := 0;
		
		FOR _REC_MOVIMIENTOS IN ( SELECT DISTINCT ID_Movimiento FROM TBL_NOM_CALCULO_NOMINA_DET	WHERE ID_Nomina = _ID_Nomina ) 
		LOOP -- por los distintos movimientos en esta nómina
			_ID_Movimiento := _REC_MOVIMIENTOS.ID_Movimiento;
			
			FOR _REC_DEPARTAMENTO IN ( 	SELECT DISTINCT ID_Departamento 
											FROM TBL_NOM_MASEMP
											WHERE ID_Empleado IN      ( 	SELECT DISTINCT ID_Empleado 
																		FROM TBL_NOM_CALCULO_NOMINA_DET
																		WHERE ID_Nomina = _ID_Nomina and ID_Movimiento = _ID_Movimiento  )		) 
			LOOP --por los distintos departamentos
				_ID_Departamento := _REC_DEPARTAMENTO.ID_Departamento;
				_ID_Cuenta := (	select Cuenta from TBL_NOM_MOVIMIENTOS_NOMINA_DET where ID_Movimiento = _ID_Movimiento and ID_Departamento = _ID_Departamento );
				_Departamento := (  select Nombre from TBL_NOM_DEPARTAMENTOS where ID_Departamento = _ID_Departamento );
				IF(_ID_Cuenta is null)
				THEN
					_err := 3;
					_result := 'ERROR: No están enlazadas todas las cuentas contables a los departamentos del movimiento ' || cast(_ID_Movimiento as varchar) || '. No se puede Generar<BR>Debes enlazar primero las cuentas a los departamentos en el catálogo de movimientos';
					EXIT;
				END IF;
				-- Asigna cantidades de la nomina
				_contPart := _contPart + 1;				
				_Gravado := ( 	select sum(n.Gravado) from TBL_NOM_CALCULO_NOMINA_DET n inner join TBL_NOM_MASEMP m on n.ID_Empleado = m.ID_Empleado
													where n.ID_Nomina = _ID_Nomina and n.ID_Movimiento = _ID_Movimiento and m.ID_Departamento = _ID_Departamento );
				_Exento = ( 	select sum(n.Exento) from TBL_NOM_CALCULO_NOMINA_DET n inner join TBL_NOM_MASEMP m on n.ID_Empleado = m.ID_Empleado
													where n.ID_Nomina = _ID_Nomina and n.ID_Movimiento = _ID_Movimiento and m.ID_Departamento = _ID_Departamento );
				_Deduccion = ( 	select sum(n.Deduccion) from TBL_NOM_CALCULO_NOMINA_DET n inner join TBL_NOM_MASEMP m on n.ID_Empleado = m.ID_Empleado
													where n.ID_Nomina = _ID_Nomina and n.ID_Movimiento = _ID_Movimiento and m.ID_Departamento = _ID_Departamento );
				-- Aqui va agregando la poliza
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE2
				VALUES(_contPart, _ID_Cuenta, _Departamento, _Gravado + _Exento - _Deduccion, 1, 1.0, _Gravado + _Exento, -_Deduccion);
				
			END LOOP; --For empleados

			IF _err > 0
			THEN
				EXIT;
			END IF;	
			
		END LOOP; --For movimientos

		_TotalBAN := ( select SUM(Debe) - SUM(Haber) from _TMP_CONT_POLIZAS_DETALLE2 );
		--RAISE NOTICE 'TOTAL DEBE: %',  ( select SUM(Debe)  from _TMP_CONT_POLIZAS_DETALLE2 );
		--RAISE NOTICE 'TOTAL HABER: %',  ( select SUM(Haber)  from _TMP_CONT_POLIZAS_DETALLE2 );
		if _err = 0
		then
			if _TotalBAN is null or _TotalBAN <= 0.0
			then 
				_err = 3;
				_result := 'ERROR: Al parecer esta nómina no contiene ningun recibo o el total del pago de la nómina es un importe de cero o un importe negativo. No se puede generar el pago de esta nómina'; 
			end if;
		end if; 
		  
		IF _err = 0 and _CE = false
		THEN
			-- Agrega ahora el movimiento a la caja o banco fijo
			CREATE LOCAL TEMPORARY TABLE _TMP_BANCOS_MOVIMIENTOS_DETALLE (
				Part serial NOT NULL ,
				Cuenta char(19) NOT NULL ,
				Concepto varchar(80) NOT NULL ,
				Parcial numeric(19,4) NOT NULL ,
				Moneda smallint NOT NULL ,
				TC numeric(19,4) NOT NULL ,
				Cantidad numeric(19,4) NOT NULL
			);

			--Agrega los movimientos a la tabla temporal
			INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE(Cuenta, Concepto, Parcial, Moneda, TC, Cantidad)
			SELECT Cuenta, Concepto, SUM(Parcial), Moneda, TC, SUM(Debe) - SUM(Haber)
			FROM _TMP_CONT_POLIZAS_DETALLE2
			GROUP BY Cuenta, Concepto, Moneda, TC;

		    --SELECT * INTO _errpart, _resultpart, _bancajmov FROM  sp_bancos_movs_agregar( _BanCaj, _ID_BanCaj, _Fecha, _Obs, _Beneficiario, 0.00, _TotalBAN, _tipomov, 'T', _IdMon, _tcCXP, _Ref, _Doc, _id_clasificacion, null, _ID_SatBanco, _RFC, _ID_SatMetodosPago, _BancoExt, _CuentaBanco, _Cheque) as ( err integer, res varchar, clave integer);
			SELECT * INTO _errpart, _banresult, _bancajmov FROM  sp_bancos_movs_agregar( _BanTipo, _BanClave, _Fecha, _Concepto, '', 0.00, _TotalBAN, 'RET', 'T', '1', 1.0, _Ref, '', _id_clasificacion, null, '000', '', '99','','','' ) as ( err integer, res varchar, clave integer);
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _banresult;
			ELSE
				UPDATE  TBL_NOM_CALCULO_NOMINA
				SET Status = 'P', FormaPago = (case when _BanTipo = 0 then 'B' else 'C' end), 
							ID_Mov = _bancajmov
				WHERE ID_Nomina = _ID_Nomina;
			END IF;

			-- Fin del movimiento bancario	
			DROP TABLE _TMP_BANCOS_MOVIMIENTOS_DETALLE;
						
		ELSIF _err = 0 and _CE = true
		THEN
			_CuentaOrigen := (select Descripcion from TBL_BANCOS_CUENTAS  where Tipo = _BanTipo and Clave = _BanClave);
			_BancoOrigen := (select ID_SatBanco from TBL_BANCOS_CUENTAS  where Tipo = _BanTipo and Clave = _BanClave);
				
			CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
				Part serial NOT NULL ,
				Cuenta char(19) NOT NULL ,
				Concepto varchar(80) NOT NULL ,
				Parcial numeric(19,4) NOT NULL ,
				Moneda smallint NOT NULL ,
				TC numeric(19,4) NOT NULL ,
				Debe numeric(19,4) NOT NULL ,
				Haber numeric(19,4) NOT NULL
			);
			CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS ( 
				id_part smallint NOT NULL,
				ctaori character varying(50) NOT NULL,
				bancoori character(3) NOT NULL,
				monto numeric(19,4) NOT NULL,
				ctadest character varying(50) NOT NULL,
				bancodest character(3) NOT NULL,
				fecha timestamp without time zone NOT NULL,
				benef character varying(300) NOT NULL,
				rfc character varying(13) NOT NULL,
				bancooriext character varying(150) NOT NULL,
				bancodestext character varying(150) NOT NULL,
				moneda character(3) NOT NULL,
				tipcamb numeric(19,5) NOT NULL
			);
			CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO (
				id_part smallint NOT NULL,
				metpagopol character(2) NOT NULL,
				fecha timestamp without time zone NOT NULL,
				benef character varying(300) NOT NULL,
				rfc character varying(13) NOT NULL,
				monto numeric(19,4) NOT NULL,
				moneda character(3) NOT NULL,
				tipcamb numeric(19,5) NOT NULL
			);

			INSERT INTO _TMP_CONT_POLIZAS_DETALLE( Cuenta, Concepto, Parcial, Moneda, TC, Debe, Haber )
			SELECT Cuenta, Concepto, SUM(Parcial), Moneda, TC, SUM(Debe), SUM(Haber)
			FROM _TMP_CONT_POLIZAS_DETALLE2
			GROUP BY Cuenta, Concepto, Moneda, TC;
			
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE
			VALUES(default, _CC, _Concepto, _TotalBan, '1', '1.0', 0.0, _TotalBan)
			RETURNING currval(pg_get_serial_sequence('_TMP_CONT_POLIZAS_DETALLE', 'part')) INTO _contPart;

			FOR _REC_RECIBOS IN ( 	select esp.ID_Nomina, esp.Recibo, esp.ID_Empleado, e.Nombre || ' ' || e.Apellido_Paterno || ' ' || e.Apellido_Materno as Nombre, e.Cuenta_Bancaria, e.ID_SatBanco, 
										e.RFC_Letras || e.RFC_Fecha || e.RFC_Homoclave as RFC, esp.Gravado + esp.Exento + esp.Deduccion as Neto 
									from tbl_nom_calculo_nomina_esp esp inner join tbl_nom_masemp e on esp.ID_Empleado = e.ID_Empleado
									where esp.ID_Nomina = _ID_Nomina ) 
			LOOP -- por los distintos recibos
				raise notice 'ID_Empleado: % Neto: %', _REC_RECIBOS.ID_Empleado, _REC_RECIBOS.Neto;
				IF _BanTipo = '0' --Se paga con entidad bancaria
				THEN
					_id_satMP = '03';
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS
					VALUES(_contPart, _CuentaOrigen, _BancoOrigen, _REC_RECIBOS.Neto, _REC_RECIBOS.Cuenta_Bancaria, _REC_RECIBOS.ID_SatBanco, _Fecha, _REC_RECIBOS.Nombre, _REC_RECIBOS.RFC,'','','MXN',1.0);
				ELSE
					_id_satMP = '01';
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO
					VALUES(_contPart, '01', _Fecha, _REC_RECIBOS.Nombre, _REC_RECIBOS.RFC, _REC_RECIBOS.Neto, 'MXN', 1.0);
				END IF;
			END LOOP;

			-- Agrega ahora la poliza principal
			SELECT * INTO _errpart, _resultpart, _numpol 
			FROM sp_cont_polizas_agregar('EG', _Fecha, _Concepto,'0', '', _TotalBan, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE -- Genera el movimiento bancario
			   --SELECT * INTO _errpart, _resultpart, _bancajmov FROM  sp_bancos_movs_agregar( _BanCaj, _ID_BanCaj, _Fecha, _Obs, _Beneficiario, 0.00, _TotalBAN, _tipomov, 'T', _IdMon, _tcCXP, _Ref, _Doc, _id_clasificacion, null, _ID_SatBanco, _RFC, _ID_SatMetodosPago, _BancoExt, _CuentaBanco, _Cheque) as ( err integer, res varchar, clave integer);
				SELECT * INTO _errpart, _banresult, _bancajmov FROM  sp_bancos_movs_agregar( _BanTipo, _BanClave, _Fecha, _Concepto, '', 0.00, _TotalBAN, 'RET', 'T', '1', 1.0, _Ref, '', _id_clasificacion, _numpol, '000', '',_id_satMP,'','','' ) as ( err integer, res varchar, clave integer);
				IF _errpart <> 0
				THEN
					_err := 3;
					_result := _banresult;
				ELSE
					IF _BanTipo = 0
					THEN
						_clase := 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_BanClave as varchar) || '||;';
					ELSE
						_clase := 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_BanClave as varchar) || '||;';
					END IF;

					UPDATE  TBL_NOM_CALCULO_NOMINA
					SET Status = 'P', FormaPago = (case when _BanTipo = 0 then 'B' else 'C' end), 
							ID_Mov = _bancajmov
					WHERE ID_Nomina = _ID_Nomina;

					UPDATE TBL_CONT_POLIZAS
					SET Ref = _clase
					WHERE ID = _numpol;
				END IF;
			END IF;			
					
			DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
			DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS;
			DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO;
		END IF;
		
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE2;

		--Ahora genera contabilidad electronica de los recibos que tienen CFDI externos enlazados
		IF(_err = 0 and _Tipo <> 3 and _Tipo <> 4) -- Significa nominaque no es especial
		THEN
			_id_pol := (select id_mov from tbl_nom_calculo_nomina where id_nomina = _id_nomina ); --Extrae el movimiento bancario
			_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol); --Extrae la poliza del movimiento
			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
			--Ahora enlaza a la contabilidad electrónica
			IF _id_poliza IS NOT NULL and _id_part IS NOT NULL
			THEN
				--Si existe la partida ingresa registro de comprobante ....
				FOR _REC_RECIBOS IN ( 	select esp.ID_Nomina, esp.ID_Empleado, esp.ID_CFD, esp.TFD, e.RFC_Letras || e.RFC_Fecha || e.RFC_Homoclave as RFC, esp.Gravado + esp.Exento + esp.Deduccion as Total
										from tbl_nom_calculo_nomina_esp esp inner join tbl_nom_masemp e on esp.ID_Empleado = e.ID_Empleado
										where esp.ID_Nomina = _ID_Nomina ) 
				LOOP -- por los distintos recibos
					--raise notice 'CFD % TFD %', _REC_RECIBOS.ID_CFD, _REC_RECIBOS.TFD;
					IF _REC_RECIBOS.TFD = 3
					THEN
						_UUID := (SELECT UUID FROM TBL_CFDNOM WHERE ID_CFD = _REC_RECIBOS.ID_CFD LIMIT 1);
						--raise notice 'UUID %', (SELECT UUID FROM TBL_CFDNOM WHERE ID_CFD = _REC_RECIBOS.ID_CFD LIMIT 1);
						INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
						VALUES(default, _id_poliza, _id_part, _uuid, _REC_RECIBOS.Total, _REC_RECIBOS.RFC);
					END IF;
				END LOOP;
				
				
			END IF;
		
		END IF;
	
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_nomina;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_calculo_nomina_generar(integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cfd_enlazar(
    _fsi_tipo character,
    _fsi_id integer,
    _uuid character)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_CFD int; _id_pol integer; _id_poliza integer; _id_part smallint; _total numeric(19,4); _id_provee integer; _condicion smallint; _cc character(19); _rfc varchar(15);
	_id_moneda smallint; _tc numeric(19,4);
BEGIN	
 	_err := 0;
	_result := 'El comprobante fiscal digital s enlazó correctamente';
	
	IF _uuid <> ''
	THEN
		IF (select count(*) from TBL_CFDCOMP where UUID = _uuid) > 0
		THEN
			_ID_CFD := (select ID_CFD from TBL_CFDCOMP where UUID = _uuid) ;
		ELSE
			_err := 3;
			_result := 'ERROR: No se puede enlazar el CFDI al registro, porque no existe el UUID proporcionado';
		END IF;
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_CFDCOMP
		SET FSI_Tipo = _FSI_Tipo, FSI_ID = _FSI_ID
		WHERE ID_CFD = _ID_CFD;

		IF _FSI_Tipo = 'FAC'
		THEN
			UPDATE TBL_COMPRAS_FACTURAS_CAB
			SET ID_CFD = _ID_CFD, TFD = 3
			WHERE ID_VC = _FSI_ID;

			_total := (select total from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			--_id_provee := (select id_clipro from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			--_rfc := (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee);
			_id_moneda :=  (select moneda from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			_tc :=  (select tc from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_facturas_pagos where id_factura = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMCONT');
			ELSIF _condicion = 1 --Credito
			THEN
				_id_pol := (select id_pol from tbl_compras_facturas_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
			ELSE -- Ningun método de pago
				_id_poliza := (select id_pol from tbl_compras_facturas_cab where id_vc = _FSI_ID);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
			--raise notice 'CXP % TOT % PRO % CON % POL % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _rfc;
		ELSIF _FSI_Tipo = 'REC'
		THEN
			UPDATE TBL_COMPRAS_RECEPCIONES_CAB
			SET ID_CFD = _ID_CFD, TFD = 3
			WHERE ID_VC = _FSI_ID;
		ELSIF _FSI_Tipo = 'GAS'
		THEN
			UPDATE TBL_COMPRAS_GASTOS_CAB
			SET ID_CFD = _ID_CFD, TFD = 3
			WHERE ID_VC = _FSI_ID;
			
			_total := (select total from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			--_id_provee := (select id_clipro from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			--_rfc := (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee);
			_id_moneda :=  (select moneda from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			_tc :=  (select tc from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_gastos_pagos where id_gasto = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
			ELSIF _condicion = 1 --Credito
			THEN
				_id_pol := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
			ELSE
				_id_poliza := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			END IF;
			-- En gastos, el cfdi se enlaza a la primera partida de la poliza porque no existe base 
			-- de cuenta contable como Compras de Credito o de contado, que si existen en facturas de materias primas
			IF _id_poliza IS NOT NULL
			THEN
				_id_part := 1;
			END IF;
			raise notice 'PP % TOT % PRO % CON % POL % PART % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _id_part, _rfc;
		ELSE -- IF _FSI_Tipo = 'DSV'
			UPDATE TBL_COMPRAS_DEVOLUCIONES_CAB
			SET ID_CFD = _ID_CFD, TFD = 3
			WHERE ID_VC = _FSI_ID;

			_total := (select total from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			--_id_provee := (select id_clipro from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			--_rfc := (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee);
			_id_moneda :=  (select moneda from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			_tc :=  (select tc from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_devoluciones_pagos where id_devolucion = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSCCONT');
			ELSIF _condicion = 1 --Credito
			THEN
				_id_pol := (select id_pol from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
				_cc := (select cc from TBL_PROVEE_CXP_CONCEPTOS where ID_Concepto = (select id_concepto from TBL_PROVEE_CXP where id_cp = _id_pol) );
			ELSE
				_id_poliza := (select id_pol from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSC');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
		END IF;

		--Ahora enlaza a la contabilidad electrónica en caso de  factura o gasto
		IF _id_poliza IS NOT NULL and _id_part IS NOT NULL
		THEN
			--Si existe la partida ingresa registro de comprobante .... Puede no ingresar en el caso que se haya cambiado la variable despues de ingresar la compra o gasto
			INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_COMPROBANTES( id_pol, id_part, uuid_cfdi, monto, rfc, id_tipo, moneda, tipcamb, cfd_cbb_serie, cfd_cbb_numfol, numfactext, taxid )
			SELECT _id_poliza, _id_part, _uuid, Total, RFC, 'CompNal',  (select id_satmoneda from tbl_cont_monedas where clave = _id_moneda), _tc, '', 0, '', ''
			FROM TBL_CFDCOMP 
			WHERE ID_CFD = _ID_CFD;
		END IF;
		
	END IF;

	RETURN QUERY SELECT _err, _result, _fsi_id;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cfd_enlazar(character, integer, character)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cfd_enlazar_uuids(
    _fsi_tipo character,
    _fsi_id integer,
    _uuid character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_CFD int; _id_pol integer; _id_poliza integer; _id_part smallint; _total numeric(19,4); _id_provee integer; _condicion smallint; _cc character(19); _rfc varchar(15); 
	_ContUUIDs smallint; _NumUUIDs smallint; _uuidx character(36);
	_id_moneda smallint; _tc numeric(19,4);
BEGIN	
 	_err := 0;
	_result := 'El comprobante fiscal digital se enlazó correctamente';
	
	IF _uuid <> ''
	THEN
		_NumUUIDs := (char_length(_uuid) / 36);
		_ContUUIDs := 0;
		WHILE _ContUUIDs < _NumUUIDs
		LOOP
			_uuidx := substring(_uuid, ((_ContUUIDs * 36) + 1), 36);
			IF (select count(*) from TBL_CFDCOMP where UUID = _uuidx) = 0
			THEN
				_err := 3;
				_result := 'ERROR: No se puede enlazar el CFDI al registro, porque no existe el UUID proporcionado ' + _uuidx;
				EXIT;
			END IF;
			_ContUUIDs := _ContUUIDs + 1;
		END LOOP;
	END IF;
	
	IF _err = 0
	THEN
		_NumUUIDs := (char_length(_uuid) / 36);
		_ContUUIDs := 0;
		WHILE _ContUUIDs < _NumUUIDs
		LOOP
			_uuidx := substring(_uuid, ((_ContUUIDs * 36) + 1), 36);

			UPDATE TBL_CFDCOMP
			SET FSI_Tipo = _FSI_Tipo, FSI_ID = _FSI_ID
			WHERE UUID = _uuidx;

			IF _FSI_Tipo = 'GAS'
			THEN
				insert into TBL_COMPRAS_GASTOS_CFD
				select  _FSI_ID, ID_CFD from TBL_CFDCOMP where UUID = _uuidx;
			END IF;
			
			_ContUUIDs := _ContUUIDs + 1;
		END LOOP;

		IF _FSI_Tipo = 'GAS'
		THEN
			UPDATE TBL_COMPRAS_GASTOS_CAB
			SET TFD = 3
			WHERE ID_VC = _FSI_ID;
			
			_total := (select total from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			--_id_provee := (select id_clipro from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			--_rfc := (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee);
			_id_moneda :=  (select moneda from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			_tc :=  (select tc from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_gastos_pagos where id_gasto = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
			ELSIF _condicion = 1 --Credito
			THEN
				_id_pol := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
			ELSE
				_id_poliza := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			END IF;
			-- En gastos, el cfdi se enlaza a la primera partida de la poliza porque no existe base 
			-- de cuenta contable como Compras de Credito o de contado, que si existen en facturas de materias primas
			IF _id_poliza IS NOT NULL
			THEN
				_id_part := 1;
			END IF;
			raise notice 'PP % TOT % PRO % CON % POL % PART % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _id_part, _rfc;
		END IF;

		--Ahora enlaza a la contabilidad electrónica en caso de  factura o gasto
		IF _id_poliza IS NOT NULL and _id_part IS NOT NULL
		THEN
			--Si existe la partida ingresa registro de comprobante .... 
			_NumUUIDs := (char_length(_uuid) / 36);
			_ContUUIDs := 0;
			WHILE _ContUUIDs < _NumUUIDs
			LOOP
				_uuidx := substring(_uuid, ((_ContUUIDs * 36) + 1), 36);
				INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_COMPROBANTES( id_pol, id_part, uuid_cfdi, monto, rfc, id_tipo, moneda, tipcamb, cfd_cbb_serie, cfd_cbb_numfol, numfactext, taxid )
				SELECT _id_poliza, _id_part, _uuidx, Total, RFC, 'CompNal',  (select id_satmoneda from tbl_cont_monedas where clave = _id_moneda), _tc, '', 0, '', ''
				FROM TBL_CFDCOMP 
				WHERE UUID = _uuidx;
				_ContUUIDs := _ContUUIDs + 1;
			END LOOP;
		END IF;
		
	END IF;

	RETURN QUERY SELECT _err, _result, _fsi_id;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cfd_enlazar_uuids(character, integer, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cfd_enlazarcbbext(
    _fsi_tipo character,
    _fsi_id integer,
    _uuid character)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_CFD int; _id_pol integer; _id_poliza integer; _id_part smallint; _total numeric(19,4); _id_provee integer; _condicion smallint; _cc character(19); _rfc varchar(15);
	_TipoComprobante varchar(10); _taxid varchar(30); _id_moneda smallint; _CBBEXT smallint;
BEGIN	
 	_err := 0;
	_result := 'El comprobante fiscal digital se enlazó correctamente';
	
	IF _uuid <> ''
	THEN
		IF (select count(*) from TBL_CFDCOMPOTR where UUID = _uuid) > 0
		THEN
			_ID_CFD := (select ID_CFD from TBL_CFDCOMPOTR where UUID = _uuid) ;
		ELSE
			_err := 3;
			_result := 'ERROR: No se puede enlazar el CBB o Factura Extranjera al registro, porque no existe el Archivo proporcionado';
		END IF;
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_CFDCOMPOTR
		SET FSI_Tipo = _FSI_Tipo, FSI_ID = _FSI_ID
		WHERE ID_CFD = _ID_CFD;

		_TipoComprobante := (select CASE WHEN numfactext = '' THEN 'CompNalOtr' ELSE 'CompExt' END from tbl_cfdcompotr where id_cfd = _ID_CFD );
		_CBBEXT := (select CASE WHEN numfactext = '' THEN 4 ELSE 5 END from tbl_cfdcompotr where id_cfd = _ID_CFD );
		
		IF _FSI_Tipo = 'FAC'
		THEN
			UPDATE TBL_COMPRAS_FACTURAS_CAB
			SET ID_CFD = _ID_CFD, TFD = _CBBEXT
			WHERE ID_VC = _FSI_ID;

			--_total := (select total from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			_id_provee := (select id_clipro from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			if _id_provee = 0 
			then
				_rfc := 'XAXX010101000';
				_taxid := 'XEXX010101000';
			else
				_rfc := case when _TipoComprobante = 'CompNalOtr' then (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee) else '' end;
				_taxid := case when _TipoComprobante = 'CompExt' then (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee) else '' end;
			end if;
			_id_moneda :=  (select moneda from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_facturas_pagos where id_factura = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMCONT');
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_compras_facturas_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
			ELSE
				_id_poliza := (select id_pol from tbl_compras_facturas_cab where id_vc = _FSI_ID);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
			--raise notice 'CXP % TOT % PRO % CON % POL % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _rfc;
		ELSIF _FSI_Tipo = 'REC'
		THEN
			UPDATE TBL_COMPRAS_RECEPCIONES_CAB
			SET ID_CFD = _ID_CFD, TFD = _CBBEXT
			WHERE ID_VC = _FSI_ID;
		ELSIF _FSI_Tipo = 'GAS'
		THEN
			UPDATE TBL_COMPRAS_GASTOS_CAB
			SET ID_CFD = _ID_CFD, TFD = _CBBEXT
			WHERE ID_VC = _FSI_ID;
			
			--_total := (select total from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			_id_provee := (select id_clipro from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			if _id_provee = 0 
			then
				_rfc := 'XAXX010101000';
				_taxid := 'XEXX010101000';
			else
				_rfc := case when _TipoComprobante = 'CompNalOtr' then (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee) else '' end;
				_taxid := case when _TipoComprobante = 'CompExt' then (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee) else '' end;
			end if;
			_id_moneda :=  (select moneda from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_gastos_pagos where id_gasto = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
			ELSE
				_id_poliza := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			END IF;
			-- En gastos, el cfdi se enlaza a la primera partida de la poliza porque no existe base 
			-- de cuenta contable como Compras de Credito o de contado, que si existen en facturas de materias primas
			IF _id_poliza IS NOT NULL
			THEN
				_id_part := 1;
			END IF;
			raise notice 'PP % TOT % PRO % CON % POL % PART % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _id_part, _rfc;
		ELSE -- IF _FSI_Tipo = 'DSV'
			UPDATE TBL_COMPRAS_DEVOLUCIONES_CAB
			SET ID_CFD = _ID_CFD, TFD = _CBBEXT
			WHERE ID_VC = _FSI_ID;

			--_total := (select total from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			_id_provee := (select id_clipro from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			if _id_provee = 0 
			then
				_rfc := 'XAXX010101000';
				_taxid := 'XEXX010101000';
			else
				_rfc := case when _TipoComprobante = 'CompNalOtr' then (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee) else '' end;
				_taxid := case when _TipoComprobante = 'CompExt' then (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee) else '' end;
			end if;
			_id_moneda :=  (select moneda from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_devoluciones_pagos where id_devolucion = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSCCONT');
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
				_cc := (select cc from TBL_PROVEE_CXP_CONCEPTOS where ID_Concepto = (select id_concepto from TBL_PROVEE_CXP where id_cp = _id_pol) );
			ELSE
				_id_poliza := (select id_pol from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSC');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
		END IF;

		--Ahora enlaza a la contabilidad electrónica en caso de  factura o gasto
		IF _id_poliza IS NOT NULL and _id_part IS NOT NULL
		THEN
			--Si existe la partida ingresa registro de comprobante .... Puede no ingresar en el caso que se haya cambiado la variable despues de ingresar la compra o gasto
			INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_COMPROBANTES( id_pol, id_part, uuid_cfdi, monto, rfc, id_tipo, moneda, tipcamb, cfd_cbb_serie, cfd_cbb_numfol, numfactext, taxid )
			SELECT _id_poliza, _id_part, _uuid, Total, _RFC, _TipoComprobante, (select id_satmoneda from tbl_cont_monedas where clave = _id_moneda), TC, CFD_CBB_Serie, CFD_CBB_NumFol, NumFactExt, _TaxID 
			FROM TBL_CFDCOMPOTR
			WHERE ID_CFD = _ID_CFD;
		END IF;
		
	END IF;

	RETURN QUERY SELECT _err, _result, _fsi_id;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cfd_enlazarcbbext(character, integer, character)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cfd_enlazarcbbext_uuids(
    _fsi_tipo character,
    _fsi_id integer,
    _uuid character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_CFD int; _id_pol integer; _id_poliza integer; _id_part smallint; _total numeric(19,4); _id_provee integer; _condicion smallint; _cc character(19); _rfc varchar(15); 
	_ContUUIDs smallint; _NumUUIDs smallint; _uuidx character(36);
	_TipoComprobante varchar(10); _id_moneda smallint; 
BEGIN	
 	_err := 0;
	_result := 'El comprobante fiscal digital se enlazó correctamente';
	
	IF _uuid <> ''
	THEN
		_NumUUIDs := (char_length(_uuid) / 36);
		_ContUUIDs := 0;
		WHILE _ContUUIDs < _NumUUIDs
		LOOP
			_uuidx := substring(_uuid, ((_ContUUIDs * 36) + 1), 36);
			IF (select count(*) from TBL_CFDCOMPOTR where UUID = _uuidx) = 0
			THEN
				_err := 3;
				_result := 'ERROR: No se puede enlazar el CBB o Factura extranjera al registro, porque no existe el UUID proporcionado ' + _uuidx;
				EXIT;
			END IF;
			_ContUUIDs := _ContUUIDs + 1;
		END LOOP;
	END IF;
	
	IF _err = 0
	THEN
		_NumUUIDs := (char_length(_uuid) / 36);
		_ContUUIDs := 0;
		WHILE _ContUUIDs < _NumUUIDs
		LOOP
			_uuidx := substring(_uuid, ((_ContUUIDs * 36) + 1), 36);

			UPDATE TBL_CFDCOMPOTR
			SET FSI_Tipo = _FSI_Tipo, FSI_ID = _FSI_ID
			WHERE UUID = _uuidx;

			IF _FSI_Tipo = 'GAS'
			THEN
				insert into TBL_COMPRAS_GASTOS_CFD
				select  _FSI_ID, ID_CFD from TBL_CFDCOMPOTR where UUID = _uuidx;
			END IF;
			
			_ContUUIDs := _ContUUIDs + 1;
		END LOOP;

		IF _FSI_Tipo = 'GAS'
		THEN
			UPDATE TBL_COMPRAS_GASTOS_CAB
			SET TFD = 6
			WHERE ID_VC = _FSI_ID;
			
			_total := (select total from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			_id_provee := (select id_clipro from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			if _id_provee = 0 
			then
				_rfc := 'XAXX010101000';
			else
				_rfc := (select rfc from tbl_provee_provee where id_tipo = 'PR' and id_clave = _id_provee);
			end if;
			_id_moneda :=  (select moneda from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_gastos_pagos where id_gasto = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
			ELSE
				_id_poliza := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			END IF;
			-- En gastos, el cfdi se enlaza a la primera partida de la poliza porque no existe base 
			-- de cuenta contable como Compras de Credito o de contado, que si existen en facturas de materias primas
			IF _id_poliza IS NOT NULL
			THEN
				_id_part := 1;
			END IF;
			--raise notice 'PP % TOT % PRO % CON % POL % PART % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _id_part, _rfc;
		END IF;

		--Ahora enlaza a la contabilidad electrónica en caso de  factura o gasto
		IF _id_poliza IS NOT NULL and _id_part IS NOT NULL
		THEN
			--Si existe la partida ingresa registro de comprobante .... 
			_NumUUIDs := (char_length(_uuid) / 36);
			_ContUUIDs := 0;
			WHILE _ContUUIDs < _NumUUIDs
			LOOP
				_uuidx := substring(_uuid, ((_ContUUIDs * 36) + 1), 36);
				_TipoComprobante := (select CASE WHEN numfactext = '' THEN 'CompNalOtr' ELSE 'CompExt' END from tbl_cfdcompotr where uuid = _uuidx );

				INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_COMPROBANTES( id_pol, id_part, uuid_cfdi, monto, rfc, id_tipo, moneda, tipcamb, cfd_cbb_serie, cfd_cbb_numfol, numfactext, taxid )
				SELECT _id_poliza, _id_part, _uuidx, Total, _RFC, _TipoComprobante, (select id_satmoneda from tbl_cont_monedas where clave = o.ID_Moneda), TC, CFD_CBB_Serie, CFD_CBB_NumFol, NumFactExt, ''
				FROM TBL_CFDCOMPOTR o
				WHERE UUID = _uuidx;
				_ContUUIDs := _ContUUIDs + 1;
			END LOOP;
		END IF;
		
	END IF;

	RETURN QUERY SELECT _err, _result, _fsi_id;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cfd_enlazarcbbext_uuids(character, integer, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cfd_enlazarventa(
    _fsi_tipo character,
    _fsi_id integer,
    _uuid character)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_CFD int; _id_pol integer; _id_poliza integer; _id_part smallint; _total numeric(19,4); _id_client integer; _condicion smallint; _cc character(19); _rfc varchar(15);
	_id_moneda smallint; _tc numeric(19,4); _moneda_sat character(3);
BEGIN	
 	_err := 0;
	_result := 'El comprobante fiscal digital se enlazó correctamente';
	
	IF _uuid <> ''
	THEN
		IF (select count(*) from TBL_CFDVEN where UUID = _uuid) > 0
		THEN
			_ID_CFD := (select ID_CFD from TBL_CFDVEN where UUID = _uuid) ;
		ELSE
			_err := 3;
			_result := 'ERROR: No se puede enlazar el CFDI al registro, porque no existe el UUID proporcionado';
		END IF;
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_CFDVEN
		SET FSI_Tipo = _FSI_Tipo, FSI_ID = _FSI_ID
		WHERE ID_CFD = _ID_CFD;

		UPDATE TBL_CFD
		SET FSI_Tipo = _FSI_Tipo, FSI_ID = _FSI_ID
		WHERE ID_CFD = _ID_CFD;

		IF _FSI_Tipo = 'FAC'
		THEN
			UPDATE TBL_VENTAS_FACTURAS_CAB
			SET ID_CFD = _ID_CFD, TFD = 3
			WHERE ID_VC = _FSI_ID;

			_total := (select total from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
			_id_client := (select id_clipro from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
			_rfc := (select rfc from tbl_client_client where id_tipo = 'CL' and id_clave = _id_client);
			_id_moneda :=  (select moneda from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
			_tc :=  (select tc from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_ventas_facturas_pagos where id_factura = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VENCONT');
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_CLIENT_CXC where id_cp = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VEN');
			ELSE
				_id_poliza := (select id_pol from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VEN');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
			--raise notice 'CXP % TOT % PRO % CON % POL % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _rfc;
		ELSIF _FSI_Tipo = 'REM'
		THEN
			UPDATE TBL_VENTAS_REMISIONES_CAB
			SET ID_CFD = _ID_CFD, TFD = 3
			WHERE ID_VC = _FSI_ID;
		ELSE -- IF _FSI_Tipo = 'DSV'
			UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
			SET ID_CFD = _ID_CFD, TFD = 3
			WHERE ID_VC = _FSI_ID;

			_total := (select total from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
			_id_client := (select id_clipro from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
			_rfc := (select rfc from tbl_client_client where id_tipo = 'CL' and id_clave = _id_client);
			_id_moneda :=  (select moneda from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
			_tc :=  (select tc from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_ventas_devoluciones_pagos where id_devolucion = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSVCONT');
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_CLIENT_CXC where id_cp = _id_pol);
				_cc := (select cc from TBL_CLIENT_CXC_CONCEPTOS where ID_Concepto = (select id_concepto from TBL_CLIENT_CXC where id_cp = _id_pol) );
			ELSE
				_id_poliza := (select id_pol from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSV');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
		END IF;

		--Ahora enlaza a la contabilidad electrónica en caso de  factura o gasto
		IF _id_poliza IS NOT NULL and _id_part IS NOT NULL
		THEN
			_moneda_sat := (select id_satmoneda from tbl_cont_monedas where clave = _id_moneda);
			--Si existe la partida ingresa registro de comprobante .... Puede no ingresar en el caso que se haya cambiado la variable despues de ingresar la compra o gasto
			INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
			VALUES(default, _id_poliza, _id_part, _uuid, _Total, _RFC, 'CompNal',  _moneda_sat, _tc, '', 0, '', '');
		END IF;
		
	END IF;

	RETURN QUERY SELECT _err, _result, _fsi_id;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cfd_enlazarventa(character, integer, character)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cfd_desenlazar_documento(
    _tipo character varying,
    _id_cfd integer)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _fsi_tipo character(3); _fsi_id integer; _fsi_esp1 character varying(20); _id_pol integer; _id_poliza integer; _id_part smallint; _condicion smallint; _cc character(19); _fsi_ent smallint;
	_tipobanco smallint; _clavebanco smallint;
BEGIN	
 	_err := 0;
	_result := 'El comprobante fiscal digital se desenlazó correctamente';
	
	IF _tipo = 'COMPRAS'
	THEN
		_fsi_tipo = (select FSI_Tipo from TBL_CFDCOMP where ID_CFD = _id_cfd);
		_fsi_id = (select FSI_ID from TBL_CFDCOMP where ID_CFD = _id_cfd);
	ELSIF _tipo = 'OTROS'
	THEN
		_fsi_tipo = (select FSI_Tipo from TBL_CFDCOMPOTR where ID_CFD = _id_cfd);
		_fsi_id = (select FSI_ID from TBL_CFDCOMPOTR where ID_CFD = _id_cfd);
	ELSIF _tipo = 'VENTAS'
	THEN
		_fsi_tipo = (select FSI_Tipo from TBL_CFDVEN where ID_CFD = _id_cfd);
		_fsi_id = (select FSI_ID from TBL_CFDVEN where ID_CFD = _id_cfd);
	ELSIF _tipo = 'NOMINA'
	THEN
		_fsi_tipo = (select FSI_Tipo from TBL_CFDNOM where ID_CFD = _id_cfd);
		_fsi_id = (select FSI_ID from TBL_CFDNOM where ID_CFD = _id_cfd);
		_fsi_esp1 = (select FSI_Esp1 from TBL_CFDNOM where ID_CFD = _id_cfd);
	ELSE
		_err := 3;
		_result := 'ERROR: Desconocido... No existe el tipo de documento: ' || _Tipo;
	END IF;

	IF _err = 0 AND (_Tipo = 'COMPRAS' OR _Tipo = 'OTROS')
	THEN
		IF _FSI_Tipo = 'FAC'
		THEN
			UPDATE TBL_COMPRAS_FACTURAS_CAB
			SET ID_CFD = null, TFD = null
			WHERE ID_VC = _FSI_ID;

			_fsi_ent := (select id_entidad from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_facturas_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_facturas_pagos where id_factura = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMCONT');
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_compras_facturas_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
			ELSE
				_id_poliza := (select id_pol from tbl_compras_facturas_cab where id_vc = _FSI_ID);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
			--raise notice 'CXP % TOT % PRO % CON % POL % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _rfc;
		ELSIF _FSI_Tipo = 'REC'
		THEN
			UPDATE TBL_COMPRAS_RECEPCIONES_CAB
			SET ID_CFD = null, TFD = null
			WHERE ID_VC = _FSI_ID;

			_fsi_ent := (select id_entidad from tbl_compras_recepciones_cab where id_vc = _FSI_ID);

		ELSIF _FSI_Tipo = 'GAS'
		THEN
			UPDATE TBL_COMPRAS_GASTOS_CAB
			SET ID_CFD = null, TFD = null
			WHERE ID_VC = _FSI_ID;
			
			_fsi_ent := (select id_entidad from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_gastos_pagos where id_gasto = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
			ELSE
				_id_poliza := (select id_pol from tbl_compras_gastos_cab where id_vc = _FSI_ID);
			END IF;
			-- En gastos, el cfdi se enlaza a la primera partida de la poliza porque no existe base 
			-- de cuenta contable como Compras de Credito o de contado, que si existen en facturas de materias primas
			IF _id_poliza IS NOT NULL
			THEN
				_id_part := 1;
			END IF;

			DELETE FROM tbl_compras_gastos_cfd
			WHERE id_gasto = _FSI_ID;
			--raise notice 'PP % TOT % PRO % CON % POL % PART % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _id_part, _rfc;
		ELSE -- IF _FSI_Tipo = 'DSC'
			UPDATE TBL_COMPRAS_DEVOLUCIONES_CAB
			SET ID_CFD = null, TFD = null
			WHERE ID_VC = _FSI_ID;

			_fsi_ent := (select id_entidad from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_compras_devoluciones_pagos where id_devolucion = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSCCONT');
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_PROVEE_CXP where id_cp = _id_pol);
				_cc := (select cc from TBL_PROVEE_CXP_CONCEPTOS where ID_Concepto = (select id_concepto from TBL_PROVEE_CXP where id_cp = _id_pol) );
			ELSE
				_id_poliza := (select id_pol from tbl_compras_devoluciones_cab where id_vc = _FSI_ID);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSC');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
		END IF;

		IF _Tipo = 'COMPRAS'
		THEN
			UPDATE TBL_CFDCOMP
			SET FSI_Tipo = 'ENT', FSI_ID = _FSI_Ent
			WHERE FSI_Tipo = _fsi_tipo AND FSI_ID = _fsi_id;
		ELSE
			UPDATE TBL_CFDCOMPOTR
			SET FSI_Tipo = 'ENT', FSI_ID = _FSI_Ent
			WHERE FSI_Tipo = _fsi_tipo AND FSI_ID = _fsi_id;
		END IF;
	ELSIF _err = 0 AND _Tipo = 'VENTAS'
	THEN
		IF _FSI_Tipo = 'FAC'
		THEN
			UPDATE TBL_VENTAS_FACTURAS_CAB
			SET ID_CFD = null, TFD = null
			WHERE ID_VC = _FSI_ID;

			_fsi_ent := (select id_entidad from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_ventas_facturas_pagos where id_factura = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VENCONT');
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_CLIENT_CXC where id_cp = _id_pol);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VEN');
			ELSE
				_id_poliza := (select id_pol from tbl_ventas_facturas_cab where id_vc = _FSI_ID);
				_cc :=  (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VEN');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
			--raise notice 'CXP % TOT % PRO % CON % POL % RFC %',_id_pol, _total, _id_provee, _condicion, _id_poliza, _rfc;
		ELSIF _FSI_Tipo = 'REM'
		THEN
			UPDATE TBL_VENTAS_REMISIONES_CAB
			SET ID_CFD = null, TFD = null
			WHERE ID_VC = _FSI_ID;

			_fsi_ent := (select id_entidad from tbl_ventas_remisiones_cab where id_vc = _FSI_ID);
			
		ELSE -- IF _FSI_Tipo = 'DSV'
			UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
			SET ID_CFD = null, TFD = null
			WHERE ID_VC = _FSI_ID;

			_fsi_ent := (select id_entidad from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
			_condicion := (select condicion from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
			
			IF _condicion = 0 --Contado
			THEN
				_id_pol := (select id_mov from tbl_ventas_devoluciones_pagos where id_devolucion = _FSI_ID limit 1 );
				_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSVCONT');
			ELSIF _condicion = 1
			THEN
				_id_pol := (select id_pol from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
				_id_poliza := (select id_pol from TBL_CLIENT_CXC where id_cp = _id_pol);
				_cc := (select cc from TBL_CLIENT_CXC_CONCEPTOS where ID_Concepto = (select id_concepto from TBL_CLIENT_CXC where id_cp = _id_pol) );
			ELSE
				_id_poliza := (select id_pol from tbl_ventas_devoluciones_cab where id_vc = _FSI_ID);
				_cc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSV');
			END IF;

			IF _id_poliza IS NOT NULL
			THEN
				_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
			END IF;
		END IF;

		UPDATE TBL_CFDVEN
		SET FSI_Tipo = 'ENT', FSI_ID = _FSI_Ent
		WHERE FSI_Tipo = _fsi_tipo AND FSI_ID = _fsi_id;

		UPDATE TBL_CFD
		SET FSI_Tipo = 'ENT', FSI_ID = _FSI_Ent
		WHERE FSI_Tipo = _fsi_tipo AND FSI_ID = _fsi_id;
	ELSIF _err = 0 AND _Tipo = 'NOMINA'
	THEN
		_fsi_ent := (select id_sucursal from tbl_nom_calculo_nomina where id_nomina = _FSI_ID );
		_id_pol := (select id_mov from tbl_nom_calculo_nomina where id_nomina = _FSI_ID ); --Extrae el movimiento bancario
		_id_poliza := (select pol_id from tbl_bancos_movimientos where id = _id_pol); --Extrae la poliza del movimiento
		_tipobanco := (select tipo from tbl_bancos_movimientos where id = _id_pol); -- el tipo de banco
		_clavebanco := (select clave from tbl_bancos_movimientos where id = _id_pol); -- la clave del banco
		_cc := (select cc from tbl_bancos_cuentas where tipo = _tipobanco and clave = _clavebanco); -- la cuenta contable del banco
	
		IF _id_poliza IS NOT NULL
		THEN
			_id_part := (select Part from TBL_CONT_POLIZAS_DETALLE where ID = _id_poliza and Cuenta = _cc limit 1);
		END IF;

		UPDATE TBL_CFDNOM
		SET FSI_Tipo = 'ENT', FSI_ID = _FSI_Ent, FSI_Esp1 = ''
		WHERE FSI_Tipo = _fsi_tipo AND FSI_ID = _fsi_id;

		UPDATE TBL_CFD
		SET FSI_Tipo = 'ENT', FSI_ID = _FSI_Ent, FSI_Esp1 = ''
		WHERE FSI_Tipo = _fsi_tipo AND FSI_ID = _fsi_id;

		UPDATE TBL_NOM_CALCULO_NOMINA_ESP
		SET ID_CFD = null, TFD = null
		WHERE ID_Nomina = _FSI_ID;

	END IF;
	
	--Ahora desenlaza de la contabilidad electrónica
	IF _id_poliza IS NOT NULL and _id_part IS NOT NULL
	THEN
		--Si existe la partida elimina registro de comprobante .... Puede no ingresar en el caso que se haya cambiado la variable despues de ingresar el comprobante
		DELETE FROM TBL_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
		WHERE id_pol = _id_poliza AND id_part = _id_part;
	END IF;

	RETURN QUERY SELECT _err, _result, (_tipo || '/' || _fsi_tipo || ':' || _id_cfd::varchar)::varchar;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cfd_desenlazar_documento(character varying, integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cont_polizas_cierre_generar(_ano smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_result varchar(255); _err int;
BEGIN
	_err := 0;
	_result := 'La poliza de cierre anual se ha generado totalmente';
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = 13 and Ano = _ano and Cerrado = '1') > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = 13 and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;
	
	IF(select count(*) from TBL_CONT_POLIZAS_DETALLE_CA where Mes = 13 and Ano = _Ano) > 0
	THEN
		_err := 3;
		_result := 'ERROR: Ya existen partidas en esta poliza de cierre anual. No se puede generar';
	END IF;

	IF _err = 0
	THEN
		INSERT INTO TBL_CONT_POLIZAS_DETALLE_CA
		SELECT 13, _Ano, d.Cuenta, 
			case when d.SaldoFinal < 0 then abs(d.SaldoFinal) else 0 end, 
			case when d.SaldoFinal < 0 then 0 else d.SaldoFinal end
		FROM TBL_CONT_CATALOGO_DETALLE d INNER JOIN TBL_CONT_CATALOGO c ON
			d.Cuenta = c.Cuenta INNER JOIN TBL_CONT_RUBROS r ON 
			substring(d.Cuenta,1,4) BETWEEN r.CuentaInicial AND r.CuentaFinal
		WHERE d.Mes = 12 and d.Ano = _Ano and d.SaldoFinal <> 0 and c.Acum = '0' and
					(r.Tipo = 'RI' or r.Tipo = 'RC' or r.Tipo = 'RG' or r.Tipo = 'RO');
	END IF;
		
	RETURN QUERY SELECT _err, _result, _Ano;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cont_polizas_cierre_generar(smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_cont_resultados(
    _mes smallint,
    _mes2 smallint,
    _ano smallint,
    _detallado bit)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_TotalVentas numeric(19,4);_TotalCostos numeric(19,4);_TotalGastos numeric(19,4);
					_TotalOtros numeric(19,4); _ResultadoXD numeric(19,4);

BEGIN

	CREATE LOCAL TEMPORARY TABLE _TMP_RESULTADOS (
		Part serial NOT NULL ,
		Clave varchar(50) NOT NULL ,
		Concepto varchar (400) NOT NULL ,
		Parcial numeric(19,4) NOT NULL ,
		Total numeric(19,4) NOT NULL ,
		Suma numeric(19,4) NOT NULL ,
		AC char (1) NOT NULL
	);
	_TotalVentas := ( select sum(Saldo) from view_cont_estado_resultados
				where Tipo = 'RI' and	Mes between _Mes and _Mes2 and Ano = _Ano );
	_TotalCostos := ( select sum(Saldo) from view_cont_estado_resultados
				where Tipo = 'RC' and	Mes between _Mes and _Mes2 and Ano = _Ano );
	_TotalGastos := ( select sum(Saldo) from view_cont_estado_resultados
				where Tipo = 'RG' and	Mes between _Mes and _Mes2 and Ano = _Ano );
	_TotalOtros := ( select sum(Saldo) from view_cont_estado_resultados
				where Tipo = 'RO' and	Mes between _Mes and _Mes2 and Ano = _Ano );
	_ResultadoXD := (_TotalVentas - _TotalCostos - _TotalGastos + _TotalOtros); 

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>VENTAS_INGRESOS</b>', 0,0,0, '';
	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------', 0, 0, coalesce(sum(Saldo),0), '' 
	from view_cont_estado_resultados 
	where Tipo = 'RI' and Saldo <> 0.0000 and Mes between _Mes and _Mes2 and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' 
		from view_cont_estado_resultados
		where Tipo = 'RI' and Saldo <> 0.0000 and  
		Mes between _Mes and _Mes2 and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0, 
						case when Acum = '1' then '+' else ' ' end
		from view_cont_estado_resultados_det
		where Tipo = 'RI' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes between _Mes and _Mes2 and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta; 
	end if;

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>COSTO_DE_VENTAS</b>', 0,0,0, '';
	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------', 0, 0, coalesce(sum(Saldo),0), '' 
	from view_cont_estado_resultados 
	where Tipo = 'RC' and Saldo <> 0.0000 and Mes between _Mes and _Mes2 and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_estado_resultados
		where Tipo = 'RC' and Saldo <> 0.0000 and  
		Mes between _Mes and _Mes2 and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0, 
						case when Acum = '1' then '+' else ' ' end
		from view_cont_estado_resultados_det
		where Tipo = 'RC' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes between _Mes and _Mes2 and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta; 
	end if;

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '','<b>UTILIDAD O PERDIDA BRUTA:</b>', 0, 0, coalesce((_TotalVentas - _TotalCostos),0), '';

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>GASTOS_DE_OPERACION</b>', 0,0,0, '';
	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------', 0, 0, coalesce(sum(Saldo),0), '' 
	from view_cont_estado_resultados 
	where Tipo = 'RG' and Saldo <> 0.0000 and Mes between _Mes and _Mes2 and Ano = _Ano; 
	if _Detallado = '0'
	then
		insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_estado_resultados
		where Tipo = 'RG' and Saldo <> 0.0000 and  
		Mes between _Mes and _Mes2 and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0, 
						case when Acum = '1' then '+' else ' ' end
		from view_cont_estado_resultados_det
		where Tipo = 'RG' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes between _Mes and _Mes2 and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta; 
	end if;

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '', '<b>UTILIDAD O PERDIDA OPERACIONAL:</b>', 0, 0, coalesce((_TotalVentas - _TotalCostos - _TotalGastos),0), '';

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>OTROS_GASTOS_Y_PRODUCTOS</b>', 0,0,0, '';
	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------', 0, 0, coalesce(sum(Saldo),0), ''
	from view_cont_estado_resultados 
	where Tipo = 'RO' and Saldo <> 0.0000 and Mes between _Mes and _Mes2 and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_estado_resultados
		where Tipo = 'RO' and Saldo <> 0.0000 and  
		Mes between _Mes and _Mes2 and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0, 
						case when Acum = '1' then '+' else ' ' end
		from view_cont_estado_resultados_det
		where Tipo = 'RO' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes between _Mes and _Mes2 and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta; 
	end if;

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '','<b>UTILIDAD O PERDIDA NETA:</b>', 0, 0, coalesce((_TotalVentas - _TotalCostos - _TotalGastos + _TotalOtros),0), '';

	insert into _TMP_RESULTADOS(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	RETURN QUERY
	select Clave, Concepto, Parcial, Total, Suma, AC from _TMP_RESULTADOS
	order by part ASC;

	DROP TABLE _TMP_RESULTADOS;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_cont_resultados(smallint, smallint, smallint, bit)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_cont_resultados_acum(
    _ano smallint,
    _comparativo character varying,
    _ultimos smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE
	_cont smallint; _TotalVentas numeric(19,4); _TotalCostos numeric(19,4); _TotalGastos numeric(19,4);
					_TotalOtros numeric(19,4); _ResultadoXD numeric(19,4);
BEGIN 
	CREATE LOCAL TEMPORARY TABLE _TMP_RESULTADOS_GEN (
			Part serial NOT NULL ,
			Periodo varchar (20) NOT NULL ,
			Ingresos numeric(19,4) NOT NULL ,
			Costos numeric(19,4) NOT NULL ,
			Gastos numeric(19,4) NOT NULL ,
			Otros numeric(19,4) NOT NULL ,
			Resultado numeric(19,4) NOT NULL 
	);

	CREATE LOCAL TEMPORARY TABLE _TMP_RESULTADOS (
			Part serial NOT NULL ,
			Periodo varchar (20) NOT NULL ,
			Total numeric(19,4) NOT NULL 
	);

	if _Ultimos = '1' -- se trata de comparativo mensual
	then
		_cont := 1;
		while _cont <= 12
		loop
			_TotalVentas := coalesce(( select sum(Saldo) from view_cont_estado_resultados
													where Tipo = 'RI' and	Mes = _cont and Ano = _Ano ),0);
			_TotalCostos := coalesce(( select sum(Saldo) from view_cont_estado_resultados
													where Tipo = 'RC' and	Mes = _cont and Ano = _Ano ),0);
			_TotalGastos := coalesce(( select sum(Saldo) from view_cont_estado_resultados
													where Tipo = 'RG' and	Mes = _cont and Ano = _Ano ),0);
			_TotalOtros :=	 coalesce(( select sum(Saldo) from view_cont_estado_resultados
													where Tipo = 'RO' and	Mes = _cont and Ano = _Ano ),0);
			_ResultadoXD := (_TotalVentas - _TotalCostos - _TotalGastos + _TotalOtros);
		
			 
			if _Comparativo = 'GENERAL'
			then
				INSERT INTO _TMP_RESULTADOS_GEN (Periodo,Ingresos,Costos,Gastos,Otros,Resultado)
				SELECT cast(_cont as varchar), _TotalVentas, _TotalCostos, _TotalGastos, _TotalOtros, _ResultadoXD;
			else
				if _Comparativo = 'RI'
				then
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _TotalVentas; 
				elsif _Comparativo = 'RC'
				then
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _TotalCostos; 
				elsif _Comparativo = 'RG'
				then
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _TotalGastos; 
				elsif _Comparativo = 'RO'
				then
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _TotalOtros; 
				else
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _ResultadoXD;
				end if;
			end if;

			_cont := _cont + 1;
		end loop;

	else -- De lo contrario es un comparativo anual

		_cont := _Ano - _Ultimos + 1;

		while _cont <= _Ano
		loop
			_TotalVentas = coalesce(( select sum(Saldo) from view_cont_estado_resultados
													where Tipo = 'RI' and	Mes between 1 and 12 and Ano = _cont ),0);
			_TotalCostos = coalesce(( select sum(Saldo) from view_cont_estado_resultados
													where Tipo = 'RC' and	Mes between 1 and 12 and Ano = _cont ),0);
			_TotalGastos = coalesce(( select sum(Saldo) from view_cont_estado_resultados
													where Tipo = 'RG' and	Mes between 1 and 12 and Ano = _cont ),0);
			_TotalOtros =	 coalesce(( select sum(Saldo) from view_cont_estado_resultados
													where Tipo = 'RO' and	Mes between 1 and 12 and Ano = _cont ),0);
			_ResultadoXD = (_TotalVentas - _TotalCostos - _TotalGastos + _TotalOtros);

			if _Comparativo = 'GENERAL'
			then
				INSERT INTO _TMP_RESULTADOS_GEN (Periodo,Ingresos,Costos,Gastos,Otros,Resultado)
				SELECT cast(_cont as varchar), _TotalVentas, _TotalCostos, _TotalGastos, _TotalOtros, _ResultadoXD;
			else
				if(_Comparativo = 'RI')
				then
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _TotalVentas;
				elsif(_Comparativo = 'RC')
				then
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _TotalCostos;
				elsif(_Comparativo = 'RG')
				then        	
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _TotalGastos;
				elsif(_Comparativo = 'RO')
				then
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _TotalOtros;
				else
					INSERT INTO _TMP_RESULTADOS (Periodo,Total)
					SELECT cast(_cont as varchar), _ResultadoXD;
				end if;
			end if;

			_cont := _cont + 1;
		end loop;

	end if;

	if _Comparativo = 'GENERAL'
	then
		return query
		select Periodo, Ingresos, Costos, Gastos, Otros, Resultado
		from _TMP_RESULTADOS_GEN
		order by part ASC;
	else
		return query
		select Periodo, Total
		from _TMP_RESULTADOS
		order by part ASC;
	end if;

	DROP TABLE _TMP_RESULTADOS;
	DROP TABLE _TMP_RESULTADOS_GEN;
	
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_cont_resultados_acum(smallint, character varying, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cont_estado_resultados AS 
 SELECT d.mes, d.ano, r.clave, r.tipo, r.nombre AS rubro, d.cuenta, c.nombre, 
        CASE
            WHEN getlevelaccount(d.cuenta) = 1 THEN 0.0
            WHEN (d.saldofinal - d.saldoinicial) < 0::numeric AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RO'::bpchar) THEN abs(d.saldofinal - d.saldoinicial)
            WHEN (d.saldofinal - d.saldoinicial) > 0::numeric AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RO'::bpchar) THEN - (d.saldofinal - d.saldoinicial)
            ELSE d.saldofinal - d.saldoinicial
        END AS parcial, 
        CASE
            WHEN getlevelaccount(d.cuenta) <> 1 THEN 0.0
            WHEN (d.saldofinal - d.saldoinicial) < 0::numeric AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RO'::bpchar) THEN abs(d.saldofinal - d.saldoinicial)
            WHEN (d.saldofinal - d.saldoinicial) > 0::numeric AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RO'::bpchar) THEN - (d.saldofinal - d.saldoinicial)
            ELSE d.saldofinal - d.saldoinicial
        END AS saldo
   FROM tbl_cont_catalogo_detalle d
   JOIN tbl_cont_catalogo c ON d.cuenta = c.cuenta
   JOIN tbl_cont_rubros r ON "substring"(d.cuenta::text, 1, 4) >= r.cuentainicial::text AND "substring"(d.cuenta::text, 1, 4) <= r.cuentafinal::text
  WHERE c.acum = B'1'::"bit" AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RC'::bpchar OR r.tipo = 'RG'::bpchar OR r.tipo = 'RO'::bpchar);

ALTER TABLE view_cont_estado_resultados
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cont_estado_resultados_det AS 
 SELECT d.mes, d.ano, r.tipo, getlevelaccount(d.cuenta) AS nivel, c.acum, d.cuenta, c.nombre, 
        CASE
            WHEN getlevelaccount(d.cuenta) = 1 THEN 0.0
            WHEN (d.saldofinal - d.saldoinicial) < 0::numeric AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RO'::bpchar) THEN abs(d.saldofinal - d.saldoinicial)
            WHEN (d.saldofinal - d.saldoinicial) > 0::numeric AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RO'::bpchar) THEN - (d.saldofinal - d.saldoinicial)
            ELSE d.saldofinal - d.saldoinicial
        END AS parcial, 
        CASE
            WHEN getlevelaccount(d.cuenta) <> 1 THEN 0.0
            WHEN (d.saldofinal - d.saldoinicial) < 0::numeric AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RO'::bpchar) THEN abs(d.saldofinal - d.saldoinicial)
            WHEN (d.saldofinal - d.saldoinicial) > 0::numeric AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RO'::bpchar) THEN - (d.saldofinal - d.saldoinicial)
            ELSE d.saldofinal - d.saldoinicial
        END AS saldo
   FROM tbl_cont_catalogo_detalle d
   JOIN tbl_cont_catalogo c ON d.cuenta = c.cuenta
   JOIN tbl_cont_rubros r ON "substring"(d.cuenta::text, 1, 4) >= r.cuentainicial::text AND "substring"(d.cuenta::text, 1, 4) <= r.cuentafinal::text
  WHERE r.tipo = 'RI'::bpchar OR r.tipo = 'RC'::bpchar OR r.tipo = 'RG'::bpchar OR r.tipo = 'RO'::bpchar;

ALTER TABLE view_cont_estado_resultados_det
  OWNER TO [[owner]];

--@FIN_BLOQUE
INSERT INTO tbl_variables(id_variable, descripcion, ventero, vdecimal, vfecha, valfanumerico, desistema, modulo)
VALUES('CC_IEPSCPN','CC|-|-|-|-', null, null, null, '', '1','COMP');

--@FIN_BLOQUE
INSERT INTO tbl_variables(id_variable, descripcion, ventero, vdecimal, vfecha, valfanumerico, desistema, modulo)
VALUES('CC_IEPSVPN','CC|-|-|-|-', null, null, null, '', '1','VEN');

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_provee_cxp_aplanticipo(
    _id_entidad smallint,
    _id_cxp integer,
    _fecha timestamp without time zone,
    _obs character varying,
    _moneda smallint,
    _tc numeric,
    _total numeric,
    _cantidad numeric,
    _id_ant integer)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _errpart int; _resultpart varchar(255); _numpol int; 
	_ID_Concepto smallint; _ID_ConceptoANT smallint; _MonedaCXP smallint; _MonedaANT smallint;
	_tcCXP numeric(19,4); _tcANT numeric(19,4); _pagoCXP numeric(19,4); _saldoANT numeric(19,4); _CC_Con char(19); _CC char(19);
	_ConceptoCon varchar(80); _DocAmparado varchar(80); _id_cxpgen int; _id_cxpant int; _Ref varchar(25);
	_RefCXP varchar(25); _RefANT varchar(25); _mes smallint; _ano smallint; _CC_DCAF char(19); _CC_DCEC char(19);
	_ID_TipoProvee char(2); _ID_ClaveProvee int; _ID_TipoProveeAnt char(2); _ID_ClaveProveeAnt int; _id_clasificacion varchar(10);
	--Contabilidad electronica
	_CC_COMP char(19); _CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _polcxp int; _ivapnCXP boolean; _iepspnCXP boolean; _contPart smallint; _ajusteIVA numeric(19,4); _ajusteIEPS numeric(19,4);
	_compraSUBTOTAL numeric(19,4); _compraIVA numeric(19,4); _compraIEPS numeric(19,4); _compraTOTAL numeric(19,4);  _TipoDocCXP char(4); _ClaveDocCXP int; _totalPol numeric(19,4);
BEGIN
	_err := 0;
	_result := (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');
	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_ID_TipoProvee := (select ID_TipoCliPro from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_ID_ClaveProvee := (select ID_ClaveCliPro from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_ID_TipoProveeAnt := (select ID_TipoCliPro from TBL_PROVEE_CXP where ID_CP = _ID_ANT);
	_ID_ClaveProveeAnt := (select ID_ClaveCliPro from TBL_PROVEE_CXP where ID_CP = _ID_ANT);
	_ID_Concepto := (select ID_Concepto from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_ID_ConceptoANT := (select ID_Concepto from TBL_PROVEE_CXP where ID_CP = _ID_ANT);
	_CC :=  (select ID_CC from TBL_PROVEE_PROVEE where ID_Tipo = _ID_TipoProvee and ID_Clave =  _ID_ClaveProvee );
	_DocAmparado := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'ETQ')  || cast(_ID_ANT as varchar);
	_tcCXP := (select TC from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_tcANT := (select TC from TBL_PROVEE_CXP where ID_CP = _ID_ANT);
	_pagoCXP := round((_Total * _tcCXP),2);
	_saldoANT := (select Saldo from TBL_PROVEE_CXP where ID_CP = _ID_ANT);
	_MonedaCXP := (select Moneda from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_MonedaANT := (select Moneda from TBL_PROVEE_CXP where ID_CP = _ID_ANT);
	_ConceptoCon := (select Descripcion from VIEW_PROVEE_CXP_CONCEPTOS where ID_Concepto = '1'); -- Concepto de Anticipo

	_polcxp := (select ID_Pol from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_CC_COMP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAAC');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAACPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSC');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSCPN');
	_TipoDocCXP := (select ID_TipoDocOrig from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_ClaveDocCXP := (select ID_ClaveDocOrig from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_id_clasificacion := (select ID_Clasificacion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_Entidad);

	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;
	
	IF  (select Status from TBL_PROVEE_CXP where ID_CP = _ID_ANT) = 'C'
	THEN
		_err := 3;
		_result := 'ERROR: No se puede aplicar un anticipo cancelado';	
	END IF;

	IF _CC_COMP = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta para compras a crédito no existe o no se ha enlazado';	
	END IF;
	
	IF _CC_IVA = '' OR _CC_IVAPN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva acreditable efectivamente pagado o la de iva acreditable pendiente de pagar no existe o no se ha enlazado';
	ELSE
		--Cuando exista version de contablilidad electrónica en una cueta por pagar, extrae el iva pendiente de la póliza. Esto indica un ajuste, de lo contrario no ajusta nada
		-- Por ejemplo cuando la cuenta por pagar era anterior a la contabilidad electronica, no hace ajuste de IVAs
		IF (select count(*) from TBL_CONT_POLIZAS_DETALLE where ID = _polcxp and Cuenta = _CC_IVAPN) > 0
		THEN
			IF _TipoDocCXP = 'CFAC'
			THEN
				_compraSUBTOTAL := (select SubTotal from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP); 
				_compraIVA := (select IVA from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP);
				_compraTOTAL := (select Total from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP);
				_ivapnCXP := true;
			ELSIF _TipoDocCXP = 'CGAS'
			THEN
				_compraSUBTOTAL := (select SubTotal from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP); 
				_compraIVA := (select IVA from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP);
				_compraTOTAL := (select Total from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP);
				_ivapnCXP := true;
			ELSE
				_ivapnCXP := false;
			END IF;
		ELSE
			_ivapnCXP := false;
		END IF;
	END IF;

	IF _CC_IEPS = '' OR _CC_IEPSPN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente pagado o la de IEPS pendiente de pagar no existe o no se ha enlazado';
	ELSE
		--Cuando exista version de contablilidad electrónica en una cueta por pagar, extrae el ieps pendiente de la póliza. Esto indica un ajuste, de lo contrario no ajusta nada
		-- Por ejemplo cuando la cuenta por pagar era anterior a la contabilidad electronica, no hace ajuste de IEPSes
		IF (select count(*) from TBL_CONT_POLIZAS_DETALLE where ID = _polcxp and Cuenta = _CC_IEPSPN) > 0
		THEN
			IF _TipoDocCXP = 'CFAC'
			THEN
				_compraSUBTOTAL := (select SubTotal from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP); 
				_compraIEPS := (select IEPS from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP);
				_compraTOTAL := (select Total from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP);
				_iepspnCXP := true;
			ELSIF _TipoDocCXP = 'CGAS'
			THEN
				_compraSUBTOTAL := (select SubTotal from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP); 
				_compraIEPS := (select IEPS from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP);
				_compraTOTAL := (select Total from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP);
				_iepspnCXP := true;
			ELSE
				_iepspnCXP := false;
			END IF;
		ELSE
			_iepspnCXP := false;
		END IF;
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;

	IF _ID_TipoProvee <> _ID_TipoProveeANT or _ID_ClaveProvee <> _ID_ClaveProveeANT or _ID_ConceptoANT <> '1' or _ID_Concepto = '1'
	THEN
		_err := 3;
		_result := (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');  --'ERROR: El proveee del anticipo es distinto al de la cuenta por cobrar, o en su defecto se quiere aplicar el anticipo a una cuenta que representa un anticipo';
	END IF;

	IF _Moneda <> _MonedaCXP or _Moneda <> _MonedaANT or _TC <> _tcANT
	THEN
		_err := 3;
		_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las monedas de la cuenta y el anticipo son distintas, o el tipo de cambio del anticipo no es el mismo que el de esta aplicación. No se puede aplicar el anticipo';
	END IF;

	IF _saldoANT < _Total
	THEN
		_err := 3;
		_result :=  (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El anticipo que se quiere aplicar es mayor al saldo de la cuenta del propio anticipo';
	END IF;

	IF _err = 0
	THEN	
		--Primero genera el descuento por aplicacion
		_RefANT := 'CCXP|' || cast(_ID_ANT as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';

		INSERT INTO TBL_PROVEE_CXP
		VALUES(default, _ID_Entidad, 'DPA', _Fecha, _ID_TipoProvee, _ID_ClaveProvee, '101', _Obs,/*id_tipodocorig*/ null,/*id_clavedocorig*/ null,_Moneda,_TC,_Total,0.00,_Fecha,/*_RefANT*/ null,'G',null,_ID_ANT,null)
		RETURNING currval(pg_get_serial_sequence('TBL_PROVEE_CXP', 'id_cp')) INTO _id_cxpant;
		--VALUES(@ID_ANT,@numeroANT,@Fecha,101,@Moneda,@TC,@Total,'A','G',@ID_CXP,-1,@numero,@Obs)

		--Ahora genera la aplicacion
		_RefCXP := 'CCXP|' || cast(_ID_CXP as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';

		INSERT INTO TBL_PROVEE_CXP
		VALUES(default, _ID_Entidad, 'APL', _Fecha, _ID_TipoProvee, _ID_ClaveProvee, '101', _Obs,'CCXP',_id_cxpant,_Moneda,_TC,_Total,( _pagoCXP - _Cantidad ),_Fecha,/*_RefCXP*/ null,'G',null,_ID_CXP,null)
		RETURNING currval(pg_get_serial_sequence('TBL_PROVEE_CXP', 'id_cp')) INTO _id_cxpgen;
			
		_Ref := 'CCXP|' || cast(_ID_CXPGEN as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';

		IF _Cantidad <> _pagoCXP OR _ivapnCXP = true OR _iepspnCXP = true -- SI HAY DIFERENCIA CAMBIARIA o IVAs y/o IEPSes DE CONTABILIDAD ELECTRONICA POR AJUSTAR
		THEN
			
			-- Primero registra y crea la tabla temporal de detalle de la poliza
			CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
				Part smallint NOT NULL ,
				Cuenta char(19) NOT NULL ,
				Concepto varchar(80) NOT NULL ,
				Parcial numeric(19,4) NOT NULL ,
				Moneda smallint NOT NULL ,
				TC numeric(19,4) NOT NULL ,
				Debe numeric(19,4) NOT NULL ,
				Haber numeric(19,4) NOT NULL
			);
			-- Fin de la tabla temporal
			_contPart := 0;
			_totalPol := 0.0;
			IF _ivapnCXP = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
			THEN
				--Calcula el iva de lo que se esta aplicando con una regla de tres
				_contPart := _contPart + 1;
				_ajusteIVA := round( (((_compraIVA * _tcCXP) / (_compraTOTAL * _tcCXP)) * (_Total * _tcCXP)), 2);
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_IVA, 'Ajuste de los IVAs', _ajusteIVA, '1', '1.0', _ajusteIVA, 0.0); 
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_IVAPN, 'Ajuste de los IVAs', _ajusteIVA, '1', '1.0', 0.0, _ajusteIVA); -- se insertara en el proveee la cantidad que se le debe segun su CXP
				_totalPol := _totalPol + _ajusteIVA;
			END IF;
			IF _iepspnCXP = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
			THEN
				--Calcula el ieps de lo que se esta aplicando con una regla de tres
				_contPart := _contPart + 1;
				_ajusteIEPS := round( (((_compraIEPS * _tcCXP) / (_compraTOTAL * _tcCXP)) * (_Total * _tcCXP)), 2);
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_IEPS, 'Ajuste de los IEPSes', _ajusteIEPS, '1', '1.0', _ajusteIEPS, 0.0); 
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_IEPSPN, 'Ajuste de los IEPSes', _ajusteIEPS, '1', '1.0', 0.0, _ajusteIEPS); -- se insertara en el proveee la cantidad que se le debe segun su CXP
				_totalPol := _totalPol + _ajusteIEPS;
			END IF;
			
			IF _Cantidad < _pagoCXP -- si la cantidad del pago es menor a la conversion en la deuda de la cxp significaganancia cambiaria
			THEN
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_DCAF, /*Ganancia*/(select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'ETQ') , (_PagoCXP - _Cantidad),'1',1.0000, 0.0, (_PagoCXP - _Cantidad)); 

				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC, _DocAmparado, (_PagoCXP - _Cantidad),'1',1.0000, (_PagoCXP - _Cantidad), 0.0);
							
				_totalPol := _totalPol + (_PagoCXP - _Cantidad);
			ELSIF _Cantidad > _pagoCXP -- de lo contrario es una perdida cambiaria
			THEN
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC, _DocAmparado, (_Cantidad - _PagoCXP),'1',1.0000, 0.0, (_Cantidad - _PagoCXP));

				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_DCEC, /*Perdida*/(select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'ETQ') , (_Cantidad - _PagoCXP),'1',1.0000, (_Cantidad - _PagoCXP), 0.0); 
								
				_totalPol := _totalPol + (_Cantidad - _PagoCXP);
      			END IF;

      			SELECT * INTO _errpart, _resultpart, _numpol FROM sp_cont_polizas_agregar('DR', _Fecha, _Obs, '0', _Ref, _totalPol, _id_clasificacion ) as ( err integer, res varchar, clave integer );

			--Fin de la agregacion
		
			-- Agrega ahora la poliza
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				UPDATE TBL_PROVEE_CXP
				SET ID_Pol = _numpol
				WHERE ID_CP = _id_cxpgen;
			END IF;

			DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
		END IF;

		IF _err = 0
		THEN
			--procede a actualizar el saldo y el status pagado de la cxp
			UPDATE  TBL_PROVEE_CXP
			SET Saldo = Saldo - _Total
			WHERE ID_CP = _ID_CXP;
			
			UPDATE  TBL_PROVEE_CXP
			SET Saldo = Saldo - _Total
			WHERE ID_CP = _ID_ANT;
	
		END IF;
	
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_cxpgen;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_provee_cxp_aplanticipo(smallint, integer, timestamp without time zone, character varying, smallint, numeric, numeric, numeric, integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_provee_cxp_pagar(
    _id_entidad smallint,
    _id_cxp integer,
    _fecha timestamp without time zone,
    _doc character varying,
    _moneda smallint,
    _tc numeric,
    _total numeric,
    _id_formapago smallint,
    _id_bancaj smallint,
    _cantidad numeric,
    _obs character varying,
    _docamparado character varying,
    _essaldo bit,
    _id_concepto smallint,
    _regpol boolean,
    _id_tipodocorig character varying,
    _id_clavedocorig integer,
    _claseref character varying,
    _tipomov character,
    _id_satbanco character,
    _id_satmetodospago character,
    _bancoext character varying,
    _cuentabanco character varying,
    _cheque character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_ID_CXPGEN int; _bancajmov int; _err int; _result varchar(255); _errpart int; _resultpart varchar(255); 
	_banresult varchar(255); _tcCXP numeric(19,4); _pagoCXP numeric(19,4); _CC_Con char(19); _CC char(19);
	_CC_DCAF char(19); _CC_DCEC char(19); _FormaPago char(1);
	_numpol int; _ID_TipoProvee char(2); _ID_ClaveProvee int; _ConceptoCon varchar(80);
	_Ref varchar(25); _RefCXP varchar(25); _mes smallint; _ano smallint; _Beneficiario varchar(80); _IdMon smallint; 
	_tcBAN numeric(19,4); _TotalBAN numeric(19,4); _BanCaj smallint; _id_clasificacion varchar(10);
	_pagada smallint; _tot numeric(19,4); _sald numeric(19,4); 
	_RFC varchar(15);
	--Contabilidad electronica
	_CC_COMP char(19); _CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _polcxp int; _ivapnCXP boolean; _iepspnCXP boolean; _contPart smallint; _ajusteIVA numeric(19,4); _ajusteIEPS numeric(19,4);
	_compraSUBTOTAL numeric(19,4); _compraIVA numeric(19,4); _compraIEPS numeric(19,4); _compraTOTAL numeric(19,4);  _TipoDocCXP char(4); _ClaveDocCXP int;
BEGIN
	_err := 0;
	_result := (case when _EsSaldo = '0' then (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK')
			else (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK') end);
	_ID_TipoProvee := (select ID_TipoCliPro from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_ID_ClaveProvee := (select ID_ClaveCliPro from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_Beneficiario := (select Nombre from TBL_PROVEE_PROVEE where ID_Tipo = _ID_TipoProvee and ID_Clave = _ID_ClaveProvee);
	_CC :=  (select ID_CC from TBL_PROVEE_PROVEE where ID_Tipo = _ID_TipoProvee and ID_Clave =  _ID_ClaveProvee );
	_RFC := ( select RFC from TBL_PROVEE_PROVEE where ID_Tipo = _ID_TipoProvee and ID_Clave = _ID_ClaveProvee);
	
	_FormaPago := (case when _EsSaldo = '1' then 'S' else (select case when _ID_FormaPago = 1 Then 'C' else 'B' end) end);
	_BanCaj := _ID_FormaPago; -- 1 cajas 0 bancos
	_DocAmparado := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'ETQ') || _DocAmparado;

	_polcxp := (select ID_Pol from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_CC_COMP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAAC');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAACPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSC');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSCPN');
	_TipoDocCXP := (select ID_TipoDocOrig from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_ClaveDocCXP := (select ID_ClaveDocOrig from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	
	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_tcCXP := (select TC from TBL_PROVEE_CXP where ID_CP = _ID_CXP);
	_pagoCXP := round((_total * _tcCXP),2);
	_bancajmov := null;
	_numpol := null;
	_CC_Con := (select CC from TBL_PROVEE_CXP_CONCEPTOS where ID_Concepto = _ID_Concepto);
	_ConceptoCon := (select Descripcion from VIEW_PROVEE_CXP_CONCEPTOS where ID_Concepto = _ID_Concepto) || ' - ' || _DocAmparado;

	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	--_PAGSAL := (case when _EsSaldo = '1' then 'SAL' else 'PAG' end);
	_id_clasificacion := (select ID_Clasificacion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_Entidad);

	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _CC_COMP = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta para compras a crédito no existe o no se ha enlazado';	
	END IF;
	
	IF _CC_IVA = '' OR _CC_IVAPN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva acreditable efectivamente pagado o la de iva acreditable pendiente de pagar, no existe o no se ha enlazado';
	ELSE
		--Cuando exista version de contablilidad electrónica en una cueta por pagar, extrae el iva pendiente de la póliza. Esto indica un ajuste, de lo contrario no ajusta nada
		-- Por ejemplo cuando la cuenta por pagar era anterior a la contabilidad electronica, no hace ajuste de IVAs
		IF (select count(*) from TBL_CONT_POLIZAS_DETALLE where ID = _polcxp and Cuenta = _CC_IVAPN) > 0
		THEN
			IF _TipoDocCXP = 'CFAC'
			THEN
				_compraSUBTOTAL := (select SubTotal from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP); 
				_compraIVA := (select IVA from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP);
				_compraTOTAL := (select Total from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP);
				_ivapnCXP := true;
			ELSIF _TipoDocCXP = 'CGAS'
			THEN
				_compraSUBTOTAL := (select SubTotal from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP); 
				_compraIVA := (select IVA from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP);
				_compraTOTAL := (select Total from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP);
				_ivapnCXP := true;
			ELSE
				_ivapnCXP := false;
			END IF;
		ELSE
			_ivapnCXP := false;
		END IF;
	END IF;

	IF _CC_IEPS = '' OR _CC_IEPSPN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente pagado o la de IEPS pendiente de pagar no existe o no se ha enlazado';
	ELSE
		--Cuando exista version de contablilidad electrónica en una cueta por pagar, extrae el ieps pendiente de la póliza. Esto indica un ajuste, de lo contrario no ajusta nada
		-- Por ejemplo cuando la cuenta por pagar era anterior a la contabilidad electronica, no hace ajuste de IVAs
		IF (select count(*) from TBL_CONT_POLIZAS_DETALLE where ID = _polcxp and Cuenta = _CC_IEPSPN) > 0
		THEN
			IF _TipoDocCXP = 'CFAC'
			THEN
				_compraSUBTOTAL := (select SubTotal from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP); 
				_compraIEPS := (select IEPS from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP);
				_compraTOTAL := (select Total from tbl_compras_facturas_cab where id_vc = _ClaveDocCXP);
				_iepspnCXP := true;
			ELSIF _TipoDocCXP = 'CGAS'
			THEN
				_compraSUBTOTAL := (select SubTotal from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP); 
				_compraIEPS := (select IEPS from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP);
				_compraTOTAL := (select Total from tbl_compras_gastos_cab where id_vc = _ClaveDocCXP);
				_iepspnCXP := true;
			ELSE
				_iepspnCXP := false;
			END IF;
		ELSE
			_iepspnCXP := false;
		END IF;
	END IF;
	
	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;
	
	IF _EsSaldo = '0' -- Si es pago en bancos, solo pueden pagarse cuentas en moneda extranjera con esa moneda o pesos, y si es en pesos solo con bancos en pesos
	THEN
		_IdMon := (case when _FormaPago = 'B' then ( select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = '0' and Clave = _ID_BanCaj ) 
							else ( select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = '1' and Clave = _ID_BanCaj ) end);
		_TotalBAN := (case when _IdMon = '1' then _Cantidad else _Total end);
		_tcBAN := (case when _IdMon = '1' then 1.0 else _TC end);
		IF _IdMon <> 1 and _IdMon <> _Moneda
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
				m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --ERROR: Solo se puede pagar con bancos de moneda extranjera, cuentas de la misma moneda extranjera';
		END IF;	
	END IF;
	
	IF _err = 0
	THEN
		_RefCXP := 'CCXP|' || cast(_ID_CXP as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';

		IF _EsSaldo = '0' -- El pago se hace en efectivo o cheque
		THEN
			IF _IdMon = 1 -- Se paga con cuenta en pesos banco nacional
			THEN
				INSERT INTO TBL_PROVEE_CXP
				VALUES(default, _ID_Entidad, 'PAG', _Fecha, _ID_TipoProvee, _ID_ClaveProvee, _ID_Concepto, _Obs, _id_tipodocorig, _id_clavedocorig,_Moneda,_TC,_Total,0.00,_Fecha, _claseref, 'G',null,_ID_CXP,null)
				RETURNING currval(pg_get_serial_sequence('TBL_PROVEE_CXP', 'id_cp')) INTO _id_cxpgen;
			ELSE
				INSERT INTO TBL_PROVEE_CXP
				VALUES(default, _ID_Entidad, 'PAG', _Fecha, _ID_TipoProvee, _ID_ClaveProvee, _ID_Concepto, _Obs, _id_tipodocorig, _id_clavedocorig,_Moneda,_tcCXP,_Total,0.00,_Fecha,_claseref,'G',null,_ID_CXP,null)
				RETURNING currval(pg_get_serial_sequence('TBL_PROVEE_CXP', 'id_cp')) INTO _id_cxpgen;
			END IF;

			_Ref := 'CCXP|' || cast(_ID_CXPGEN as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';
			
			CREATE LOCAL TEMPORARY TABLE _TMP_BANCOS_MOVIMIENTOS_DETALLE (
				Part smallint NOT NULL ,
				Cuenta char(19) NOT NULL ,
				Concepto varchar(80) NOT NULL ,
				Parcial numeric(19,4) NOT NULL ,
				Moneda smallint NOT NULL ,
				TC numeric(19,4) NOT NULL ,
				Cantidad numeric(19,4) NOT NULL
			);
		
			--RAISE NOTICE 'Cantidad %, _pagoCXP %', _Cantidad, _pagoCXP;
			IF _IdMon = 1 -- Se paga con cuenta en pesos banco nacional
			THEN
				_contPart := 1;
				INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
				VALUES(_contPart, _CC, (_DocAmparado || ' ' || _Doc), _Total, _Moneda, _tcCXP, _pagoCXP); -- se insertara en el proveee la cantidad que se le debe segun su CXP

				IF _ivapnCXP = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
				THEN
					--Calcula el iva de lo que se esta pagando con una regla de tres
					_contPart := _contPart + 1;
					_ajusteIVA := round( (((_compraIVA * _tcCXP) / (_compraTOTAL * _tcCXP)) * (_Total * _tcCXP)), 2);
					--round(     ((((_Total * _tcCXP) - (_compraIVA * _tcCXP)) *   (_compraIVA * _tcCXP)) / (_compraSUBTOTAL * _tcCXP))   , 2);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IVA, 'Ajuste de los IVAs', _ajusteIVA, '1', '1.0', _ajusteIVA); 
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IVAPN, 'Ajuste de los IVAs', _ajusteIVA, '1', '1.0', -_ajusteIVA); 
				END IF;

				IF _iepspnCXP = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
				THEN
					--Calcula el ieps de lo que se esta pagando con una regla de tres
					_contPart := _contPart + 1;
					_ajusteIEPS := round( (((_compraIEPS * _tcCXP) / (_compraTOTAL * _tcCXP)) * (_Total * _tcCXP)), 2);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IEPS, 'Ajuste de los IEPSes', _ajusteIEPS, '1', '1.0', _ajusteIEPS); 
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IEPSPN, 'Ajuste de los IEPSes', _ajusteIEPS, '1', '1.0', -_ajusteIEPS); 
				END IF;
				
				IF(_Cantidad < _pagoCXP) -- si la cantidad del pago es menor a la conversion en la deuda de la cxp signifiaganancia cambiaria
				THEN
					_contPart := _contPart + 1;
					--RAISE NOTICE 'perdida Parcial %, Ultimo %', (_PagoCXP - _Cantidad), -(_PagoCXP - _Cantidad);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_DCAF,/*Ganancia*/ (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), (_PagoCXP - _Cantidad),'1',1.0000,-(_PagoCXP - _Cantidad)); 
				ELSIF(_Cantidad > _pagoCXP) -- de lo contrario es una ganancia cambiaria
				THEN
					_contPart := _contPart + 1;
					--RAISE NOTICE 'ganancia Parcial %, Ultimo %', (_Cantidad - _PagoCXP), (_Cantidad - _PagoCXP);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_DCEC, /*Perdida*/(select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), (_Cantidad - _PagoCXP),'1',1.0000,(_Cantidad - _PagoCXP));  					
				END IF;

				SELECT * INTO _errpart, _resultpart, _bancajmov FROM  sp_bancos_movs_agregar( _BanCaj, _ID_BanCaj, _Fecha, _Obs, _Beneficiario, 0.00, _TotalBAN, _tipomov, 'T', _IdMon, _tcBAN, _Ref, _Doc, _id_clasificacion, null, _ID_SatBanco, _RFC, _ID_SatMetodosPago, _BancoExt, _CuentaBanco, _Cheque ) as ( err integer, res varchar, clave integer);
			     
			ELSE
				_contPart := 1;
				INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
				VALUES( _contPart, _CC, (_DocAmparado || ' ' || _Doc), _Total, '1', _tcCXP, _Total); -- se insertara en el proveee la cantidad que se le debe segun su CXP
	
				IF _ivapnCXP = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
				THEN
					--Calcula el iva de lo que se esta pagando con una regla de tres
					_contPart := _contPart + 1;
					_ajusteIVA := round( ((_compraIVA / _compraTOTAL) * _Total), 2);
					--round(     (( (_Total - _compraIVA) *  _compraIVA) / _compraSUBTOTAL)   , 2);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IVA, 'Ajuste de los IVAs', _ajusteIVA, '1', _tcCXP, _ajusteIVA); -- se insertara en el proveee la cantidad que se le debe segun su CXP
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IVAPN, 'Ajuste de los IVAs', _ajusteIVA, '1', _tcCXP, -_ajusteIVA); -- se insertara en el proveee la cantidad que se le debe segun su CXP
				END IF;

				IF _iepspnCXP = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
				THEN
					--Calcula el ieps de lo que se esta pagando con una regla de tres
					_contPart := _contPart + 1;
					_ajusteIEPS := round( ((_compraIEPS / _compraTOTAL) * _Total), 2);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IEPS, 'Ajuste de los IEPSes', _ajusteIEPS, '1', _tcCXP, _ajusteIEPS); -- se insertara en el proveee la cantidad que se le debe segun su CXP
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IEPSPN, 'Ajuste de los IEPSes', _ajusteIEPS, '1', _tcCXP, -_ajusteIEPS); -- se insertara en el proveee la cantidad que se le debe segun su CXP
				END IF;

				SELECT * INTO _errpart, _resultpart, _bancajmov FROM  sp_bancos_movs_agregar( _BanCaj, _ID_BanCaj, _Fecha, _Obs, _Beneficiario, 0.00, _TotalBAN, _tipomov, 'T', _IdMon, _tcCXP, _Ref, _Doc, _id_clasificacion, null, _ID_SatBanco, _RFC, _ID_SatMetodosPago, _BancoExt, _CuentaBanco, _Cheque) as ( err integer, res varchar, clave integer);
				
			END IF;
			
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				-- Procede a hacer update al anticipo con el ID del movimiento de banco o caja 
				UPDATE  TBL_PROVEE_CXP
				SET ID_PagoBanCaj = _bancajmov
				WHERE ID_CP = _ID_CXPGEN;
			END IF;
			
			DROP TABLE _TMP_BANCOS_MOVIMIENTOS_DETALLE;
		ELSE -- ES SALDO
			INSERT INTO TBL_PROVEE_CXP
			VALUES(default, _ID_Entidad, 'SAL', _Fecha, _ID_TipoProvee, _ID_ClaveProvee, _ID_Concepto, _Obs, _id_tipodocorig, _id_clavedocorig,_Moneda,_TC,_Total,0.00,_Fecha,_claseref,'G',null,_ID_CXP,null)
			RETURNING currval(pg_get_serial_sequence('TBL_PROVEE_CXP', 'id_cp')) INTO _id_cxpgen;

			_Ref := 'CCXP|' || cast(_ID_CXPGEN as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';
			
			IF _CC_Con is not null AND _regpol = true --Si la cuenta del concepto es real y manda registro de poliza..... registra la poliza
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				-- Fin de la tabla temporal
				-- Inserta el cargo de la cuenta del concepto
				_contPart := 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_Con, (_DocAmparado || ' ' || _Doc), _Total, _Moneda, _TC, 0.0, _Cantidad);

				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC, _ConceptoCon, _Total, _Moneda, _tcCXP, _pagoCXP, 0.0); -- se insertara en el proveee la cantidad que se le debe segun su CXP

				IF(_Cantidad < _pagoCXP) -- si la cantidad del pago es menor a la conversion en la deuda de la cxp significaganancia cambiaria
				THEN
					--RAISE NOTICE 'cantMenor Parcial %, Ultimo %', (_PagoCXP - _Cantidad), -(_PagoCXP - _Cantidad);
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					VALUES('3', _CC_DCAF, /*Ganancia*/(select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), (_PagoCXP - _Cantidad),'1',1.0000, 0.0, (_PagoCXP - _Cantidad)); 
				ELSIF(_Cantidad > _pagoCXP) -- de lo contrario es una perdida cambiaria
				THEN
					--RAISE NOTICE 'cantMayor Parcial %, Ultimo %', (_Cantidad - _PagoCXP), (_Cantidad - _PagoCXP);
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					VALUES('3', _CC_DCEC, /*Perdida*/(select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), (_Cantidad - _PagoCXP),'1',1.0000, (_Cantidad - _PagoCXP), 0.0);  					
				END IF;
			
				SELECT * INTO _errpart, _resultpart, _numpol FROM sp_cont_polizas_agregar('DR', _Fecha, _Obs,'0', _Ref, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				-- Agrega ahora la poliza
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					UPDATE  TBL_PROVEE_CXP
					SET ID_Pol = _numpol
					WHERE ID_CP = _ID_CXPGEN;
				END IF;

				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
			END IF;
		END IF;

		-- procede a registrar la poliza de este movimiento
		IF _err = 0
		THEN
			--procede a actualizar el saldo y el status pagado de la cxp
			UPDATE  TBL_PROVEE_CXP
			SET Saldo = Saldo - _Total
			WHERE ID_CP = _ID_CXP;
			
			UPDATE TBL_PROVEE_SALDOS_MONEDAS
			SET Saldo = Saldo - _Total
			WHERE ID_Moneda = _Moneda AND ID_Tipo = _ID_TipoProvee AND ID_Clave =  _ID_ClaveProvee;
		END IF;
	END IF;

	RETURN QUERY SELECT _err, _result, _id_cxpgen;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_provee_cxp_pagar(smallint, integer, timestamp without time zone, character varying, smallint, numeric, numeric, smallint, smallint, numeric, character varying, character varying, bit, smallint, boolean, character varying, integer, character varying, character, character, character, character varying, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_client_cxc_aplanticipo(
    _id_entidad smallint,
    _id_cxc integer,
    _fecha timestamp without time zone,
    _obs character varying,
    _moneda smallint,
    _tc numeric,
    _total numeric,
    _cantidad numeric,
    _id_ant integer)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _errpart int; _resultpart varchar(255); _numpol int; 
	_ID_Concepto smallint; _ID_ConceptoANT smallint; _MonedaCXC smallint; _MonedaANT smallint;
	_tcCXC numeric(19,4); _tcANT numeric(19,4); _pagoCXC numeric(19,4); _saldoANT numeric(19,4); _CC_Con char(19); _CC char(19);
	_ConceptoCon varchar(80); _DocAmparado varchar(80); _id_cxcgen int; _id_cxcant int; _Ref varchar(25);
	_RefCXC varchar(25); _RefANT varchar(25); _mes smallint; _ano smallint; _CC_DCAF char(19); _CC_DCEC char(19);
	_ID_TipoClient char(2); _ID_ClaveClient int; _ID_TipoClientAnt char(2); _ID_ClaveClientAnt int; _id_clasificacion varchar(10);
	--contabilidad electronica
	_CC_VEN char(19); _CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _polcxc int; _ivapnCXC boolean; _iepspnCXC boolean; _contPart smallint; _ajusteIVA numeric(19,4); _ajusteIEPS numeric(19,4);
	_ventaSUBTOTAL numeric(19,4); _ventaIVA numeric(19,4); _ventaIEPS numeric(19,4); _ventaTOTAL numeric(19,4);  _TipoDocCXC char(4); _ClaveDocCXC int;  _totalPol numeric(19,4);
BEGIN
	_err := 0;
	_result := (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');
	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_ID_TipoClient := (select ID_TipoCliPro from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_ID_ClaveClient := (select ID_ClaveCliPro from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_ID_TipoClientAnt := (select ID_TipoCliPro from TBL_CLIENT_CXC where ID_CP = _ID_ANT);
	_ID_ClaveClientAnt := (select ID_ClaveCliPro from TBL_CLIENT_CXC where ID_CP = _ID_ANT);
	_ID_Concepto := (select ID_Concepto from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_ID_ConceptoANT := (select ID_Concepto from TBL_CLIENT_CXC where ID_CP = _ID_ANT);
	_CC :=  (select ID_CC from TBL_CLIENT_CLIENT where ID_Tipo = _ID_TipoClient and ID_Clave =  _ID_ClaveClient );
	_DocAmparado := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'ETQ')  || cast(_ID_ANT as varchar);
	_tcCXC := (select TC from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_tcANT := (select TC from TBL_CLIENT_CXC where ID_CP = _ID_ANT);
	_pagoCXC := round((_Total * _tcCXC),2);
	_saldoANT := (select Saldo from TBL_CLIENT_CXC where ID_CP = _ID_ANT);
	_MonedaCXC := (select Moneda from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_MonedaANT := (select Moneda from TBL_CLIENT_CXC where ID_CP = _ID_ANT);
	_ConceptoCon := (select Descripcion from VIEW_CLIENT_CXC_CONCEPTOS where ID_Concepto = '1'); -- Concepto de Anticipo

	_polcxc := (select ID_Pol from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_CC_VEN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VEN');
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPP');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPPPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSV');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSVPN');
	_TipoDocCXC := (select ID_TipoDocOrig from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_ClaveDocCXC := (select ID_ClaveDocOrig from TBL_CLIENT_CXC where ID_CP = _ID_CXC);

	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_id_clasificacion := (select ID_Clasificacion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_Entidad);

	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF  (select Status from TBL_CLIENT_CXC where ID_CP = _ID_ANT) = 'C'
	THEN
		_err := 3;
		_result := 'ERROR: No se puede aplicar un anticipo cancelado';	
	END IF;
	
	IF _CC_VEN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta para ventas a crédito no existe o no se ha enlazado';	
	END IF;
	
	IF _CC_IVA = '' OR _CC_IVAPN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva por pagar efectivamente cobrado o la de iva por pagar pendiente de cobro, no existe o no se ha enlazado';
	ELSE
		--Cuando exista version de contablilidad electrónica en una cueta por cobrar, extrae el iva pendiente de la póliza. Esto indica un ajuste, de lo contrario no ajusta nada
		-- Por ejemplo cuando la cuenta por pagar era anterior a la contabilidad electronica, no hace ajuste de IVAs
		IF (select count(*) from TBL_CONT_POLIZAS_DETALLE where ID = _polcxc and Cuenta = _CC_IVAPN) > 0
		THEN
			IF _TipoDocCXC = 'VFAC'
			THEN
				_ventaSUBTOTAL := (select SubTotal from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC); 
				_ventaIVA := (select IVA from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC);
				_ventaTOTAL := (select Total from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC);
				_ivapnCXC := true;
			ELSE
				_ivapnCXC := false;
			END IF;
		ELSE
			_ivapnCXC := false;
		END IF;
	END IF;

	IF _CC_IEPS = '' OR _CC_IEPSPN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente cobrado o la de IEPS pendiente de cobrar no existe o no se ha enlazado';
	ELSE
		--Cuando exista version de contablilidad electrónica en una cueta por cobrar, extrae el ieps pendiente de la póliza. Esto indica un ajuste, de lo contrario no ajusta nada
		-- Por ejemplo cuando la cuenta por cobrar era anterior a la contabilidad electronica, no hace ajuste de IEPSes
		IF (select count(*) from TBL_CONT_POLIZAS_DETALLE where ID = _polcxc and Cuenta = _CC_IEPSPN) > 0
		THEN
			IF _TipoDocCXC = 'VFAC'
			THEN
				_ventaSUBTOTAL := (select SubTotal from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC); 
				_ventaIEPS := (select IEPS from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC);
				_ventaTOTAL := (select Total from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC);
				_iepspnCXC := true;
			ELSE
				_iepspnCXC := false;
			END IF;
		ELSE
			_iepspnCXC := false;
		END IF;
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;

	IF _ID_TipoClient <> _ID_TipoClientANT or _ID_ClaveClient <> _ID_ClaveClientANT or _ID_ConceptoANT <> '1' or _ID_Concepto = '1'
	THEN
		_err := 3;
		_result := (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');  --'ERROR: El cliente del anticipo es distinto al de la cuenta por cobrar, o en su defecto se quiere aplicar el anticipo a una cuenta que representa un anticipo';
	END IF;

	IF _Moneda <> _MonedaCXC or _Moneda <> _MonedaANT or _TC <> _tcANT
	THEN
		_err := 3;
		_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las monedas de la cuenta y el anticipo son distintas, o el tipo de cambio del anticipo no es el mismo que el de esta aplicación. No se puede aplicar el anticipo';
	END IF;

	IF _saldoANT < _Total
	THEN
		_err := 3;
		_result :=  (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El anticipo que se quiere aplicar es mayor al saldo de la cuenta del propio anticipo';
	END IF;

	IF _err = 0
	THEN	
		--Primero genera el descuento por aplicacion
		_RefANT := 'VCXC|' || cast(_ID_ANT as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';

		INSERT INTO TBL_CLIENT_CXC
		VALUES(default, _ID_Entidad, 'DPA', _Fecha, _ID_TipoClient, _ID_ClaveClient, '101', _Obs,/*id_tipodocorig*/ null,/*id_clavedocorig*/ null,_Moneda,_TC,_Total,0.00,_Fecha,/*_RefANT*/ null,'G',null,_ID_ANT,null)
		RETURNING currval(pg_get_serial_sequence('TBL_CLIENT_CXC', 'id_cp')) INTO _id_cxcant;
		--VALUES(@ID_ANT,@numeroANT,@Fecha,101,@Moneda,@TC,@Total,'A','G',@ID_CXC,-1,@numero,@Obs)

		--Ahora genera la aplicacion
		_RefCXC := 'VCXC|' || cast(_ID_CXC as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';

		INSERT INTO TBL_CLIENT_CXC
		VALUES(default, _ID_Entidad, 'APL', _Fecha, _ID_TipoClient, _ID_ClaveClient, '101', _Obs,'VCXC',_id_cxcant,_Moneda,_TC,_Total,( _pagoCXC - _Cantidad ),_Fecha,/*_RefCXC*/ null,'G',null,_ID_CXC,null)
		RETURNING currval(pg_get_serial_sequence('TBL_CLIENT_CXC', 'id_cp')) INTO _id_cxcgen;
			
		_Ref := 'VCXC|' || cast(_ID_CXCGEN as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';

		IF _Cantidad <> _pagoCXC  OR _ivapnCXC = true OR _iepspnCXC = true -- SI HAY DIFERENCIA CAMBIARIA o IVAS y/o IEPSes DE CONTABILIDAD ELECTRONICA POR AJUSTAR
		THEN
			-- Primero registra y crea la tabla temporal de detalle de la poliza
			CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
				Part smallint NOT NULL ,
				Cuenta char(19) NOT NULL ,
				Concepto varchar(80) NOT NULL ,
				Parcial numeric(19,4) NOT NULL ,
				Moneda smallint NOT NULL ,
				TC numeric(19,4) NOT NULL ,
				Debe numeric(19,4) NOT NULL ,
				Haber numeric(19,4) NOT NULL
			);
			-- Fin de la tabla temporal
			_contPart := 0;
			_totalPol := 0.0;
			IF _ivapnCXC = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
			THEN
				--Calcula el iva de lo que se esta aplicando con una regla de tres
				_contPart := _contPart + 1;
				_ajusteIVA := round( (((_ventaIVA * _tcCXC) / (_ventaTOTAL * _tcCXC)) * (_Total * _tcCXC)), 2);
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_IVAPN, 'Ajuste de los IVAs', _ajusteIVA, '1', '1.0', _ajusteIVA, 0.0); 
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_IVA, 'Ajuste de los IVAs', _ajusteIVA, '1', '1.0', 0.0, _ajusteIVA); 
				_totalPol := _totalPol + _ajusteIVA;
			END IF;
	
			IF _iepspnCXC = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
			THEN
				--Calcula el ieps de lo que se esta aplicando con una regla de tres
				_contPart := _contPart + 1;
				_ajusteIEPS := round( (((_ventaIEPS * _tcCXC) / (_ventaTOTAL * _tcCXC)) * (_Total * _tcCXC)), 2);
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_IEPSPN, 'Ajuste de los IEPSes', _ajusteIEPS, '1', '1.0', _ajusteIEPS, 0.0); 
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_IEPS, 'Ajuste de los IEPSes', _ajusteIEPS, '1', '1.0', 0.0, _ajusteIEPS); 
				_totalPol := _totalPol + _ajusteIEPS;
			END IF;
			
			IF _Cantidad < _pagoCXC -- si la cantidad del pago es menor a la conversion en la deuda de la cxp significa perdida cambiaria
			THEN
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_DCEC, /*Perdida*/(select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'ETQ') , (_PagoCXC - _Cantidad),'1',1.0000, (_PagoCXC - _Cantidad), 0.00); 

				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC, _DocAmparado, (_PagoCXC - _Cantidad),'1',1.0000, 0.0, (_PagoCXC - _Cantidad));

				_totalPol := _totalPol + (_PagoCXC - _Cantidad);			
			ELSIF _Cantidad > _pagoCXC -- de lo contrario es una ganancia cambiaria
			THEN
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC, _DocAmparado, (_Cantidad - _PagoCXC),'1',1.0000, (_Cantidad - _PagoCXC), 0.00);

				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES(_contPart, _CC_DCAF, /*Ganancia*/(select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'ETQ') , (_Cantidad - _PagoCXC),'1',1.0000, 0.00, (_Cantidad - _PagoCXC)); 

				_totalPol := _totalPol + (_Cantidad - _PagoCXC);				
			END IF;

      			SELECT * INTO _errpart, _resultpart, _numpol FROM sp_cont_polizas_agregar('DR', _Fecha, _Obs, '0', _Ref, _totalPol, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      			
			--Fin de la agregacion
			
			-- Agrega ahora la poliza
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				UPDATE TBL_CLIENT_CXC
				SET ID_Pol = _numpol
				WHERE ID_CP = _id_cxcgen;
			END IF;

			DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
		END IF;

		-- procede a registrar la poliza de este movimiento
		IF _err = 0
		THEN
			--procede a actualizar el saldo y el status pagado de la cxc
			UPDATE  TBL_CLIENT_CXC
			SET Saldo = Saldo - _Total
			WHERE ID_CP = _ID_CXC;
			
			UPDATE  TBL_CLIENT_CXC
			SET Saldo = Saldo - _Total
			WHERE ID_CP = _ID_ANT;
	
		END IF;
	
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_cxcgen;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_client_cxc_aplanticipo(smallint, integer, timestamp without time zone, character varying, smallint, numeric, numeric, numeric, integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_client_cxc_pagar(
    _id_entidad smallint,
    _id_cxc integer,
    _fecha timestamp without time zone,
    _doc character varying,
    _moneda smallint,
    _tc numeric,
    _total numeric,
    _id_formapago smallint,
    _id_bancaj smallint,
    _cantidad numeric,
    _obs character varying,
    _docamparado character varying,
    _essaldo bit,
    _id_concepto smallint,
    _regpol boolean,
    _id_tipodocorig character varying,
    _id_clavedocorig integer,
    _claseref character varying,
    _tipomov character,
    _id_satbanco character,
    _id_satmetodospago character,
    _bancoext character varying,
    _cuentabanco character varying,
    _cheque character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_ID_CXCGEN int; _bancajmov int; _err int; _result varchar(255); _errpart int; _resultpart varchar(255); 
	_banresult varchar(255); _tcCXC numeric(19,4); _pagoCXC numeric(19,4); _CC_Con char(19); _CC char(19);
	_CC_DCAF char(19); _CC_DCEC char(19); _FormaPago char(1);  
	_numpol int; _ID_TipoClient char(2); _ID_ClaveClient int; _ConceptoCon varchar(80);
	_Ref varchar(25); _RefCXC varchar(25); _mes smallint; _ano smallint; _Beneficiario varchar(80); _IdMon smallint; 
	_tcBAN numeric(19,4); _TotalBAN numeric(19,4); _BanCaj smallint; _id_clasificacion varchar(10);
	_pagada smallint; _tot numeric(19,4); _sald numeric(19,4); 
	--contabilidad electronica
	_CC_VEN char(19); _CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _polcxc int; _ivapnCXC boolean; _iepspnCXC boolean; _contPart smallint; _ajusteIVA numeric(19,4); _ajusteIEPS numeric(19,4);
	_ventaSUBTOTAL numeric(19,4); _ventaIVA numeric(19,4); _ventaIEPS numeric(19,4); _ventaTOTAL numeric(19,4);  _TipoDocCXC char(4); _ClaveDocCXC int;
	_RFC varchar(15);
BEGIN
	_err := 0;
	_result := (case when _EsSaldo = '0' then (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK')
			else (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK') end);
	_ID_TipoClient := (select ID_TipoCliPro from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_ID_ClaveClient := (select ID_ClaveCliPro from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_CC :=  (select ID_CC from TBL_CLIENT_CLIENT where ID_Tipo = _ID_TipoClient and ID_Clave =  _ID_ClaveClient );
	_FormaPago := (case when _EsSaldo = '1' then 'S' else (select case when _ID_FormaPago = 1 Then 'C' else 'B' end) end);
	_BanCaj := _ID_FormaPago; -- 1 cajas 0 bancos
	_Beneficiario := ( select VAlfanumerico from TBL_VARIABLES where ID_Variable = 'EMPRESA');
	_RFC := ( select RFC from TBL_CLIENT_CLIENT where ID_Tipo = _ID_TipoClient and ID_Clave =  _ID_ClaveClient);
	_DocAmparado := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'ETQ') || _DocAmparado;

	_polcxc := (select ID_Pol from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_CC_VEN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VEN');
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPP');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPPPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSV');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSVPN');
	_TipoDocCXC := (select ID_TipoDocOrig from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_ClaveDocCXC := (select ID_ClaveDocOrig from TBL_CLIENT_CXC where ID_CP = _ID_CXC);

	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_tcCXC := (select TC from TBL_CLIENT_CXC where ID_CP = _ID_CXC);
	_pagoCXC := round((_total * _tcCXC),2);
	_bancajmov := null;
	_numpol := null;
	_CC_Con := (select CC from TBL_CLIENT_CXC_CONCEPTOS where ID_Concepto = _ID_Concepto);
	_ConceptoCon := (select Descripcion from VIEW_CLIENT_CXC_CONCEPTOS where ID_Concepto = _ID_Concepto) || ' - ' || _DocAmparado;

	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	--_PAGSAL := (case when _EsSaldo = '1' then 'SAL' else 'PAG' end);
	_id_clasificacion := (select ID_Clasificacion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_Entidad);

	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _CC_VEN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta para ventas a crédito no existe o no se ha enlazado';	
	END IF;
	
	IF _CC_IVA = '' OR _CC_IVAPN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva por pagar efectivamente cobrado o la de iva por pagar pendiente de cobro, no existe o no se ha enlazado';
	ELSE
		--Cuando exista version de contablilidad electrónica en una cueta por cobrar, extrae el iva pendiente de la póliza. Esto indica un ajuste, de lo contrario no ajusta nada
		-- Por ejemplo cuando la cuenta por pagar era anterior a la contabilidad electronica, no hace ajuste de IVAs
		IF (select count(*) from TBL_CONT_POLIZAS_DETALLE where ID = _polcxc and Cuenta = _CC_IVAPN) > 0
		THEN
			IF _TipoDocCXC = 'VFAC'
			THEN
				_ventaSUBTOTAL := (select SubTotal from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC); 
				_ventaIVA := (select IVA from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC);
				_ventaTOTAL := (select Total from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC);
				_ivapnCXC := true;
			ELSE
				_ivapnCXC := false;
			END IF;
		ELSE
			_ivapnCXC := false;
		END IF;
	END IF;

	IF _CC_IEPS = '' OR _CC_IEPSPN = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente cobrado o la de IEPS pendiente de cobrar no existe o no se ha enlazado';
	ELSE
		--Cuando exista version de contablilidad electrónica en una cueta por cobrar, extrae el ieps pendiente de la póliza. Esto indica un ajuste, de lo contrario no ajusta nada
		-- Por ejemplo cuando la cuenta por cobrar era anterior a la contabilidad electronica, no hace ajuste de IEPSes
		IF (select count(*) from TBL_CONT_POLIZAS_DETALLE where ID = _polcxc and Cuenta = _CC_IEPSPN) > 0
		THEN
			IF _TipoDocCXC = 'VFAC'
			THEN
				_ventaSUBTOTAL := (select SubTotal from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC); 
				_ventaIEPS := (select IEPS from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC);
				_ventaTOTAL := (select Total from tbl_ventas_facturas_cab where id_vc = _ClaveDocCXC);
				_iepspnCXC := true;
			ELSE
				_iepspnCXC := false;
			END IF;
		ELSE
			_iepspnCXC := false;
		END IF;
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;
	
	IF _EsSaldo = '0' -- Si es pago en bancos, solo pueden pagarse cuentas en moneda extranjera con esa moneda o pesos, y si es en pesos solo con bancos en pesos
	THEN
		_IdMon := (case when _FormaPago = 'B' then ( select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = '0' and Clave = _ID_BanCaj ) 
							else ( select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = '1' and Clave = _ID_BanCaj ) end);
		_TotalBAN := (case when _IdMon = '1' then _Cantidad else _Total end);
		_tcBAN := (case when _IdMon = '1' then 1.0 else _TC end);
		IF _IdMon <> 1 and _IdMon <> _Moneda
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
				m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --ERROR: Solo se puede pagar con bancos de moneda extranjera, cuentas de la misma moneda extranjera';
		END IF;	
	END IF;
	
	IF _err = 0
	THEN
		_RefCXC := 'VCXC|' || cast(_ID_CXC as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';

		IF _EsSaldo = '0' -- El pago se hace en efectivo o cheque
		THEN
			IF _IdMon = 1 -- Se paga con cuenta en pesos banco nacional
			THEN
				INSERT INTO TBL_CLIENT_CXC
				VALUES(default, _ID_Entidad, 'PAG', _Fecha, _ID_TipoClient, _ID_ClaveClient, _ID_Concepto, _Obs, _id_tipodocorig, _id_clavedocorig,_Moneda,_TC,_Total,0.00,_Fecha, _claseref, 'G',null,_ID_CXC,null)
				RETURNING currval(pg_get_serial_sequence('TBL_CLIENT_CXC', 'id_cp')) INTO _id_cxcgen;
			ELSE
				INSERT INTO TBL_CLIENT_CXC
				VALUES(default, _ID_Entidad, 'PAG', _Fecha, _ID_TipoClient, _ID_ClaveClient, _ID_Concepto, _Obs, _id_tipodocorig, _id_clavedocorig,_Moneda,_tcCXC,_Total,0.00,_Fecha,_claseref,'G',null,_ID_CXC,null)
				RETURNING currval(pg_get_serial_sequence('TBL_CLIENT_CXC', 'id_cp')) INTO _id_cxcgen;
			END IF;

			_Ref := 'VCXC|' || cast(_ID_CXCGEN as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';
			
			CREATE LOCAL TEMPORARY TABLE _TMP_BANCOS_MOVIMIENTOS_DETALLE (
				Part smallint NOT NULL ,
				Cuenta char(19) NOT NULL ,
				Concepto varchar(80) NOT NULL ,
				Parcial numeric(19,4) NOT NULL ,
				Moneda smallint NOT NULL ,
				TC numeric(19,4) NOT NULL ,
				Cantidad numeric(19,4) NOT NULL
			);

			--RAISE NOTICE 'Cantidad %, _pagoCXC %', _Cantidad, _pagoCXC;

			IF _IdMon = 1 -- Se paga con cuenta en pesos banco nacional
			THEN
				_contPart := 1;
				INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
				VALUES( _contPart, _CC, (_DocAmparado || ' ' || _Doc), _Total, _Moneda, _tcCXC, _pagoCXC); -- se insertara en el cliente la cantidad que se le debe segun su CXP

				IF _ivapnCXC = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
				THEN
					--Calcula el iva de lo que se esta pagando con una regla de tres
					_contPart := _contPart + 1;
					_ajusteIVA := round( (((_ventaIVA * _tcCXC) / (_ventaTOTAL * _tcCXC)) * (_Total * _tcCXC)), 2);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IVAPN, 'Ajuste de los IVAs', _ajusteIVA, '1', '1.0', -_ajusteIVA); 
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IVA, 'Ajuste de los IVAs', _ajusteIVA, '1', '1.0', _ajusteIVA); 
				END IF;
	
				IF _iepspnCXC = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
				THEN
					--Calcula el ieps de lo que se esta pagando con una regla de tres
					_contPart := _contPart + 1;
					_ajusteIEPS := round( (((_ventaIEPS * _tcCXC) / (_ventaTOTAL * _tcCXC)) * (_Total * _tcCXC)), 2);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IEPSPN, 'Ajuste de los IEPSes', _ajusteIEPS, '1', '1.0', -_ajusteIEPS); 
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IEPS, 'Ajuste de los IEPSes', _ajusteIEPS, '1', '1.0', _ajusteIEPS); 
				END IF;
				
				IF(_Cantidad < _pagoCXC) -- si la cantidad del pago es menor a la conversion en la deuda de la cxc significa perdida cambiaria
				THEN
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES( _contPart, _CC_DCEC,/*pERDIDA*/ (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), (_PagoCXC - _Cantidad),'1',1.0000,-(_PagoCXC - _Cantidad)); 
				ELSIF(_Cantidad > _pagoCXC) -- de lo contrario es una ganancia cambiaria
				THEN
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES( _contPart, _CC_DCAF, /*Ganancia*/(select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), (_Cantidad - _PagoCXC),'1',1.0000,(_Cantidad - _PagoCXC));  					
				END IF;

				SELECT * INTO _errpart, _resultpart, _bancajmov FROM  sp_bancos_movs_agregar( _BanCaj, _ID_BanCaj, _Fecha, _Obs, _Beneficiario, _TotalBAN, 0.00, _tipomov, 'T', _IdMon, _tcBAN, _Ref, _Doc, _id_clasificacion, null, _ID_SatBanco, _RFC, _ID_SatMetodosPago, _BancoExt, _CuentaBanco, _Cheque ) as ( err integer, res varchar, clave integer);
		    
			ELSE
				_contPart := 1;
				INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
				VALUES( _contPart, _CC, (_DocAmparado || ' ' || _Doc), _Total, '1', _tcCXC, _Total); -- se insertara en el cliente la cantidad que se le debe segun su CXP

				IF _ivapnCXC = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
				THEN
					--Calcula el iva de lo que se esta pagando con una regla de tres
					_contPart := _contPart + 1;
					_ajusteIVA := round( ((_ventaIVA / _ventaTOTAL) * _Total), 2);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IVAPN, 'Ajuste de los IVAs', _ajusteIVA, '1', _tcCXC, -_ajusteIVA); 
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IVA, 'Ajuste de los IVAs', _ajusteIVA, '1', _tcCXC, _ajusteIVA); 
				END IF;

				IF _iepspnCXC = true -- sera verdadero cuando sea entidad contable y versión contabilidad electronica 
				THEN
					--Calcula el ieps de lo que se esta pagando con una regla de tres
					_contPart := _contPart + 1;
					_ajusteIEPS := round( ((_ventaIEPS / _ventaTOTAL) * _Total), 2);
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IEPSPN, 'Ajuste de los IEPSes', _ajusteIEPS, '1', _tcCXC, -_ajusteIEPS); 
					_contPart := _contPart + 1;
					INSERT INTO _TMP_BANCOS_MOVIMIENTOS_DETALLE
					VALUES(_contPart, _CC_IEPS, 'Ajuste de los IEPSes', _ajusteIEPS, '1', _tcCXC, _ajusteIEPS); 
				END IF;

				SELECT * INTO _errpart, _resultpart, _bancajmov FROM  sp_bancos_movs_agregar( _BanCaj, _ID_BanCaj, _Fecha, _Obs, _Beneficiario, _TotalBAN, 0.00, _tipomov, 'T', _IdMon, _tcCXC, _Ref, _Doc, _id_clasificacion, null, _ID_SatBanco, _RFC, _ID_SatMetodosPago, _BancoExt, _CuentaBanco, _Cheque ) as ( err integer, res varchar, clave integer);
		    
			END IF;
			
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				-- Procede a hacer update al anticipo con el ID del movimiento de banco o caja 
				UPDATE  TBL_CLIENT_CXC
				SET ID_PagoBanCaj = _bancajmov
				WHERE ID_CP = _ID_CXCGEN;
			END IF;
			
			DROP TABLE _TMP_BANCOS_MOVIMIENTOS_DETALLE;
		ELSE -- ES SALDO
			INSERT INTO TBL_CLIENT_CXC
			VALUES(default, _ID_Entidad, 'SAL', _Fecha, _ID_TipoClient, _ID_ClaveClient, _ID_Concepto, _Obs, _id_tipodocorig, _id_clavedocorig,_Moneda,_TC,_Total,0.00,_Fecha,_claseref,'G',null,_ID_CXC,null)
			RETURNING currval(pg_get_serial_sequence('TBL_CLIENT_CXC', 'id_cp')) INTO _id_cxcgen;

			_Ref := 'VCXC|' || cast(_ID_CXCGEN as varchar) || '|' || cast(_ID_Entidad as varchar) || '||';
			
			IF _CC_Con is not null AND _regpol = true --Si la cuenta del concepto es real y manda registro de poliza..... registra la poliza
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				-- Fin de la tabla temporal
				-- Inserta el cargo de la cuenta del concepto
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES('1', _CC_Con, (_DocAmparado || ' ' || _Doc), _Total, _Moneda, _TC, _Cantidad, 0.0);
				
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				VALUES('2', _CC, _ConceptoCon, _Total, _Moneda, _tcCXC, 0.0, _pagoCXC); -- se insertara en el cliente la cantidad que se le debe segun su CXP

				IF(_Cantidad < _pagoCXC) -- si la cantidad del pago es menor a la conversion en la deuda de la cxc significa perdida cambiaria
				THEN
					--RAISE NOTICE 'cantMenor Parcial %, Ultimo %', (_PagoCXC - _Cantidad), -(_PagoCXC - _Cantidad);
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					VALUES('3', _CC_DCEC, /*Perdida*/(select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), (_PagoCXC - _Cantidad),'1',1.0000,(_PagoCXC - _Cantidad), 0.0); 
				ELSIF(_Cantidad > _pagoCXC) -- de lo contrario es una ganancia cambiaria
				THEN
					--RAISE NOTICE 'cantMayor Parcial %, Ultimo %', (_Cantidad - _PagoCXC), (_Cantidad - _PagoCXC);
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					VALUES('3', _CC_DCAF, /*Ganancia*/(select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and m.sub::text = 'BD' and m.elm::text = 'ETQ'), (_Cantidad - _PagoCXC),'1',1.0000, 0.0, (_Cantidad - _PagoCXC));  					
				END IF;
			
				SELECT * INTO _errpart, _resultpart, _numpol FROM sp_cont_polizas_agregar('DR', _Fecha, _Obs,'0', _Ref, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				-- Agrega ahora la poliza
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					UPDATE  TBL_CLIENT_CXC
					SET ID_Pol = _numpol
					WHERE ID_CP = _ID_CXCGEN;
				END IF;

				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
			END IF;
		END IF;

		-- procede a registrar la poliza de este movimiento
		IF _err = 0
		THEN
			--procede a actualizar el saldo y el status pagado de la cxc
			UPDATE  TBL_CLIENT_CXC
			SET Saldo = Saldo - _Total
			WHERE ID_CP = _ID_CXC;
			
			UPDATE TBL_CLIENT_SALDOS_MONEDAS
			SET Saldo = Saldo - _Total
			WHERE ID_Moneda = _Moneda AND ID_Tipo = _ID_TipoClient AND ID_Clave =  _ID_ClaveClient;
		END IF;
	END IF;

	RETURN QUERY SELECT _err, _result, _id_cxcgen;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_client_cxc_pagar(smallint, integer, timestamp without time zone, character varying, smallint, numeric, numeric, smallint, smallint, numeric, character varying, character varying, bit, smallint, boolean, character varying, integer, character varying, character, character, character, character varying, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
--////////////////////////////////////////////////////////////////////////////////////////////////////////
--Hasta aqui ya esta actualizado en todas las bases locales
--Ahora lo siguiente es exclusivamente para la Ayuda del sistema.... Actualizar en demás locales y 
--verificar actualizados de TAJA y TAJAB
--////////////////////////////////////////////////////////////////////////////////////////////////////////

CREATE TABLE tbl_reports_help
(
  id_report smallint NOT NULL,
  help text NOT NULL,
  CONSTRAINT pk_tbl_reports_help PRIMARY KEY (id_report),
  CONSTRAINT fk_tbl_reports_help_tbl_reports FOREIGN KEY (id_report)
      REFERENCES tbl_reports (id_report) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE tbl_reports_help
  OWNER TO [[owner]];

--@FIN_BLOQUE
INSERT INTO tbl_reports_help(id_report, help)
SELECT id_report, 'Documentación del reporte "' || description || '"'
FROM tbl_reports;

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cont_balanza_general AS 
 SELECT cd.mes, cd.ano, r.tipo, getlevelaccount(c.cuenta) AS nivel, c.cuenta, c.acum AS ac, c.nombre, cd.retiros AS cargos, cd.abonos, 
        CASE
            WHEN cd.retiros > cd.abonos THEN cd.retiros - cd.abonos
            ELSE 0.0
        END AS deudor, 
        CASE
            WHEN cd.abonos > cd.retiros THEN cd.abonos - cd.retiros
            ELSE 0.0
        END AS acreedor, 
        CASE
            WHEN cd.retiros > cd.abonos AND (r.tipo = 'AC'::bpchar OR r.tipo = 'AF'::bpchar OR r.tipo = 'AD'::bpchar OR r.tipo = 'PC'::bpchar OR r.tipo = 'PL'::bpchar OR r.tipo = 'PD'::bpchar OR r.tipo = 'CC'::bpchar) THEN cd.retiros - cd.abonos
            ELSE 0.0
        END AS activo, 
        CASE
            WHEN cd.abonos > cd.retiros AND (r.tipo = 'AC'::bpchar OR r.tipo = 'AF'::bpchar OR r.tipo = 'AD'::bpchar OR r.tipo = 'PC'::bpchar OR r.tipo = 'PL'::bpchar OR r.tipo = 'PD'::bpchar OR r.tipo = 'CC'::bpchar) THEN cd.abonos - cd.retiros
            ELSE 0.0
        END AS pasivo, 
        CASE
            WHEN cd.retiros > cd.abonos AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RC'::bpchar OR r.tipo = 'RG'::bpchar OR r.tipo = 'RO'::bpchar OR r.tipo = 'IP'::bpchar) THEN cd.retiros - cd.abonos
            ELSE 0.0
        END AS perdida, 
        CASE
            WHEN cd.abonos > cd.retiros AND (r.tipo = 'RI'::bpchar OR r.tipo = 'RC'::bpchar OR r.tipo = 'RG'::bpchar OR r.tipo = 'RO'::bpchar OR r.tipo = 'IP'::bpchar) THEN cd.abonos - cd.retiros
            ELSE 0.0
        END AS ganancia
   FROM tbl_cont_catalogo c
   JOIN tbl_cont_catalogo_detalle cd ON c.cuenta = cd.cuenta
   JOIN tbl_cont_rubros r ON c.cuenta::text >= "substring"(r.cuentainicial::text, 1, 4) AND c.cuenta::text <= ("substring"(r.cuentafinal::text, 1, 4) || '999999999999999'::text);

ALTER TABLE view_cont_balanza_general
  OWNER TO [[owner]];
 
--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_cont_balanza_general(
    _mes smallint,
    _ano smallint,
    _totales bit,
    _cols integer,
    _niveldetalle smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE
	_TotalActivo numeric(19,4); _TotalPasivo numeric(19,4); _TotalPerdida numeric(19,4); _TotalGanancia numeric(19,4); _Resultado numeric(19,4);
BEGIN
	IF _Cols = 8
	THEN
		IF _Totales = '0' --Son las cuentas como tal y no los acumulados
		THEN
			CREATE LOCAL TEMPORARY TABLE _TMP_BALANZA_GENERAL (
				Part serial NOT NULL ,
				Clave varchar(50) NOT NULL ,
				Nombre varchar (400) NOT NULL ,
				Cargos numeric(19,4) NOT NULL ,
				Abonos numeric(19,4) NOT NULL ,
				Deudor numeric(19,4) NOT NULL ,
				Acreedor numeric(19,4) NOT NULL ,
				Activo numeric(19,4) NOT NULL ,
				Pasivo numeric(19,4) NOT NULL ,
				Perdida numeric(19,4) NOT NULL ,
				Ganancia numeric(19,4) NOT NULL,
				AC varchar(2) NOT NULL
			);

			IF _NivelDetalle = 1 --Solo cuentas de mayor
			THEN
				insert into _TMP_BALANZA_GENERAL(Clave,Nombre,Cargos,Abonos,Deudor,Acreedor,Activo,Pasivo,Perdida,Ganancia,AC)
				select substring(Cuenta,1,4), Nombre, Cargos, Abonos, Deudor, Acreedor, Activo, Pasivo, Perdida, Ganancia, '++' 
				from view_cont_balanza_general
				where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '1' and Nivel = '1'
				order by cuenta;
			ELSIF _Niveldetalle = 2 --Todo, tanto aumulativas como de detalle
			THEN
				insert into _TMP_BALANZA_GENERAL(Clave,Nombre,Cargos,Abonos,Deudor,Acreedor,Activo,Pasivo,Perdida,Ganancia,AC)
				select case 	when Nivel = 1 then substring(Cuenta,1,4) 
										when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
										when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
										when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
										when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
										else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
							case 	when Nivel = 1 then Nombre 
										when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
										when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
										when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
										when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
										else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
							Cargos,Abonos,Deudor,Acreedor,Activo,Pasivo,Perdida,Ganancia,case when AC = '1' then case when Nivel = 1 then '++' else '+' end else ' ' end
				from view_cont_balanza_general
				where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano
				order by cuenta;
			ELSE -- Solo cuentas de detalle
				insert into _TMP_BALANZA_GENERAL(Clave,Nombre,Cargos,Abonos,Deudor,Acreedor,Activo,Pasivo,Perdida,Ganancia,AC)
				select CASE
							WHEN Nivel = 1 THEN substring(cuenta, 1, 4)
							WHEN Nivel = 2 THEN substring(cuenta, 1, 4) || '-' || substring(cuenta, 5, 3)
							WHEN Nivel = 3 THEN substring(cuenta, 1, 4) || '-' || substring(cuenta, 5, 3) || '-' || substring(cuenta, 8, 3)
							WHEN Nivel = 4 THEN substring(cuenta, 1, 4) || '-' || substring(cuenta, 5, 3) || '-' || substring(cuenta, 8, 3) || '-' || substring(cuenta, 11, 3)
							WHEN Nivel = 5 THEN substring(cuenta, 1, 4) || '-' || substring(cuenta, 5, 3) || '-' || substring(cuenta, 8, 3) || '-' || substring(cuenta, 11, 3) || '-' || substring(cuenta, 14, 3)
							WHEN Nivel = 6 THEN substring(cuenta, 1, 4) || '-' || substring(cuenta, 5, 3) || '-' || substring(cuenta, 8, 3) || '-' || substring(cuenta, 11, 3) || '-' || substring(cuenta, 14, 3) || '-' || substring(cuenta, 17, 3)
						ELSE cuenta END AS Cuenta, Nombre, Cargos, Abonos, Deudor, Acreedor, Activo, Pasivo, Perdida, Ganancia, ' ' 
				from view_cont_balanza_general
				where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '0'
				order by cuenta;
			END IF;

			RETURN QUERY
			select clave, nombre, cargos, abonos, deudor, acreedor, activo, pasivo, perdida, ganancia, ac
			from _TMP_BALANZA_GENERAL
			order by part ASC;
			
			DROP TABLE _TMP_BALANZA_GENERAL;
		ELSE --Acumulados
			CREATE LOCAL TEMPORARY TABLE _TMP_BALANZA_GENERAL (
				Totales varchar (400) NOT NULL ,
				Cargos numeric(19,4) NOT NULL ,
				Abonos numeric(19,4) NOT NULL ,
				Deudor numeric(19,4) NOT NULL ,
				Acreedor numeric(19,4) NOT NULL ,
				Activo numeric(19,4) NOT NULL ,
				Pasivo numeric(19,4) NOT NULL ,
				Perdida numeric(19,4) NOT NULL ,
				Ganancia numeric(19,4) NOT NULL ,
				AC varchar(2) NOT NULL
			);

			_Resultado = (select resultado from tbl_cont_resultados where Mes = _Mes and Ano = _Ano);

			_TotalActivo = (select sum(Activo) from view_cont_balanza_general where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '0');
			_TotalPasivo =  (select sum(Pasivo) from view_cont_balanza_general where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '0');
			_TotalPerdida = (select sum(Perdida) from view_cont_balanza_general where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '0');
			_TotalGanancia = (select sum(Ganancia) from view_cont_balanza_general where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '0');
			
			insert into _TMP_BALANZA_GENERAL(Totales,Cargos,Abonos,Deudor,Acreedor,Activo,Pasivo,Perdida,Ganancia,AC)
			select 'Suma:' as Totales, sum(Cargos), sum(Abonos), sum(Deudor), sum(Acreedor), _TotalActivo, _TotalPasivo, _TotalPerdida, _TotalGanancia, ' ' 
			from view_cont_balanza_general
			where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '0';
			insert into _TMP_BALANZA_GENERAL(Totales,Cargos,Abonos,Deudor,Acreedor,Activo,Pasivo,Perdida,Ganancia,AC)
			select 'Resultado:' as Totales, 0, 0, 0, 0, 
				case when _Resultado < 0 then abs(_Resultado) else 0 end, 
				case when _Resultado > 0 then _Resultado else 0 end, 
				case when _Resultado > 0 then _Resultado else 0 end, 
				case when _Resultado < 0 then abs(_Resultado) else 0 end, ' ';
			insert into _TMP_BALANZA_GENERAL(Totales,Cargos,Abonos,Deudor,Acreedor,Activo,Pasivo,Perdida,Ganancia,AC)
			select 'Total:' as Totales, 0, 0, 0, 0, 
				case when _Resultado < 0 then _TotalActivo - abs(_Resultado) else _TotalActivo end, 
				case when _Resultado > 0 then _TotalPasivo - _Resultado else _TotalPasivo end, 
				case when _Resultado > 0 then _TotalPerdida - _Resultado else _TotalPerdida end, 
				case when _Resultado < 0 then _TotalGanancia - abs(_Resultado) else _TotalGanancia end, ' ';
						
			RETURN QUERY
			select totales, cargos, abonos, deudor, acreedor, activo, pasivo, perdida, ganancia,ac
			from _TMP_BALANZA_GENERAL;
			
			DROP TABLE _TMP_BALANZA_GENERAL;

		END IF;
	ELSE --Balanza normal
		IF _Totales = '0' --Son las cuentas como tal y no los acumulados
		THEN
			CREATE LOCAL TEMPORARY TABLE _TMP_BALANZA_GENERAL (
				Part serial NOT NULL ,
				Clave varchar(50) NOT NULL ,
				Nombre varchar (400) NOT NULL ,
				Cargos numeric(19,4) NOT NULL ,
				Abonos numeric(19,4) NOT NULL ,
				Deudor numeric(19,4) NOT NULL ,
				Acreedor numeric(19,4) NOT NULL ,
				AC varchar(2) NOT NULL
			);

			IF _NivelDetalle = 1 --Solo cuentas de mayor
			THEN
				insert into _TMP_BALANZA_GENERAL(Clave,Nombre,Cargos,Abonos,Deudor,Acreedor,AC)
				select substring(Cuenta,1,4), Nombre, Cargos, Abonos, Deudor, Acreedor, '++' 
				from view_cont_balanza_general
				where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '1' and Nivel = '1'
				order by cuenta;
			ELSIF _Niveldetalle = 2 --Todas, tanto acumulativas como de detalle
			THEN
				insert into _TMP_BALANZA_GENERAL(Clave,Nombre,Cargos,Abonos,Deudor,Acreedor,AC)
				select case 	when Nivel = 1 then substring(Cuenta,1,4) 
										when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
										when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
										when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
										when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
										else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
							case 	when Nivel = 1 then Nombre 
										when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
										when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
										when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
										when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
										else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
							Cargos,Abonos,Deudor,Acreedor,case when AC = '1' then case when Nivel = 1 then '++' else '+' end else ' ' end
				from view_cont_balanza_general
				where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano
				order by cuenta;
			ELSE --Solo cuentas de detalle
				insert into _TMP_BALANZA_GENERAL(Clave,Nombre,Cargos,Abonos,Deudor,Acreedor,AC)
				select Cuenta , Nombre, Cargos,Abonos,Deudor,Acreedor, ' '
				from view_cont_balanza_general
				where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '0'
				order by cuenta;
			END IF;

			RETURN QUERY
			select clave, nombre, cargos, abonos, deudor, acreedor, ac
			from _TMP_BALANZA_GENERAL
			order by part ASC;
			
			DROP TABLE _TMP_BALANZA_GENERAL;
		ELSE --Acumulados
			CREATE LOCAL TEMPORARY TABLE _TMP_BALANZA_GENERAL (
				Totales varchar (400) NOT NULL ,
				Cargos numeric(19,4) NOT NULL ,
				Abonos numeric(19,4) NOT NULL ,
				Deudor numeric(19,4) NOT NULL ,
				Acreedor numeric(19,4) NOT NULL ,
				AC varchar(2) NOT NULL
			);

			insert into _TMP_BALANZA_GENERAL(Totales,Cargos,Abonos,Deudor,Acreedor,AC)
			select 'Total:' as Totales, sum(Cargos), sum(Abonos), sum(Deudor), sum(Acreedor), ' ' 
			from view_cont_balanza_general
			where (Cargos <> 0 or Abonos <> 0) and Mes = _Mes and Ano = _Ano and AC = '0';
			
			RETURN QUERY
			select totales, cargos, abonos, deudor, acreedor, ac
			from _TMP_BALANZA_GENERAL;
			
			DROP TABLE _TMP_BALANZA_GENERAL;

		END IF;
	END IF;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_cont_balanza_general(smallint, smallint, bit, integer, smallint)
  OWNER TO [[owner]];


--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_fechadiaria(
    _fecha timestamp without time zone,
    _fecha2 timestamp without time zone)
  RETURNS SETOF record AS
$BODY$
DECLARE
	_dias smallint; _dia smallint; 
BEGIN
	create local temporary table _tmp_fechadiaria (
		fecha timestamp not null
	);
	
	_dias := getfechadiff('day',_fecha,_fecha2);
	
	IF _dias >= 0 -- Es ascendente
	THEN
		_dia := 0;
		while _dia <= _dias
		loop
			insert into _tmp_fechadiaria
			select _fecha + (cast(_dia as text) || ' day'::text)::interval;
			_dia := _dia + 1;
		end loop;
	ELSE
		_dia := 0;
		while _dia >= _dias
		loop
			insert into _tmp_fechadiaria
			select _fecha + (cast(_dia as text) || ' day'::text)::interval;
			_dia := _dia - 1;
		end loop;
	END IF;

	RETURN QUERY
	select * from _tmp_fechadiaria;
	
	drop table _tmp_fechadiaria;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_fechadiaria(timestamp without time zone, timestamp without time zone)
  OWNER TO [[owner]];

--@FIN_BLOQUE
ALTER TABLE tbl_cont_polizas_detalle
ADD saldo numeric(19,4);

UPDATE tbl_cont_polizas_detalle
SET saldo = 0.0;

ALTER TABLE tbl_cont_polizas_detalle
ALTER COLUMN saldo SET NOT NULL;

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cont_polizas_agregar(
    _tipo character,
    _fecha timestamp without time zone,
    _concepto character varying,
    _transito bit,
    _ref character varying,
    _total numeric,
    _id_clasificacion character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE
	_err int; _result varchar(255); _mes smallint; _ano smallint; _numero int; __IDENTITY int; _SumDebe numeric(19,4); _SumHaber numeric(19,4); _Status char(1);
BEGIN
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La poliza se ha registrado satisfactoriamente'
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_numero := (select Numero from TBL_CONT_POLIZAS_TIPOS where ID_Tipo = _Tipo);
	_SumDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE);
	_SumHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE);
	_Status := case when _Transito = '1' then 'T' else 'G' end;
	
	IF( (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = '1') > 0
		OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1 )
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;
	
	IF _SumDebe <> _SumHaber
	THEN
		_err := 3;
		_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La sumas desiguales para la partida doble'
	END IF;

	IF (select count(*) from _TMP_CONT_POLIZAS_DETALLE d join TBL_CONT_CATALOGO c on d.Cuenta = c.Cuenta where c.Estatus = 'D') > 0
	THEN
		_err := 3;
		_result := (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Alguna de las cuentas contables de esta póliza esta descontinuada. cambia el estatus de la cuenta a Activa'
	END IF;
	
	IF _err = 0
	THEN
		INSERT INTO TBL_CONT_POLIZAS(Tipo, Numero, Fecha, Concepto, Ref, Status, Total, ID_Clasificacion)
		VALUES(_Tipo, _numero, _Fecha, _Concepto, _Ref, _Status, _SumDebe, _id_clasificacion)
		RETURNING currval(pg_get_serial_sequence('TBL_CONT_POLIZAS', 'id')) INTO __IDENTITY;
		
		INSERT INTO TBL_CONT_POLIZAS_DETALLE
		SELECT __IDENTITY, Part, Cuenta, Concepto, Parcial, Moneda, TC, Debe, Haber, 0.0 --Este saldo 0.0 será actualizado en la actualización de saldos contables
		FROM _TMP_CONT_POLIZAS_DETALLE;

		BEGIN
			INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_CHEQUES(  id_pol, id_part, num, banco, ctaori, fecha, monto, benef, rfc, banemisext, moneda, tipcamb )
			SELECT __IDENTITY, id_part, num, banco, ctaori, fecha, monto, benef, rfc, banemisext, moneda, tipcamb
			FROM _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES;
		EXCEPTION WHEN undefined_table THEN
			-- Simplemente ignora.
		END;

		BEGIN
			INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS( id_pol, id_part, ctaori, bancoori, monto, ctadest, bancodest, fecha, benef, rfc, bancooriext, bancodestext, moneda, tipcamb )
			SELECT __IDENTITY, id_part, ctaori, bancoori, monto, ctadest, bancodest, fecha, benef, rfc, bancooriext, bancodestext, moneda, tipcamb
			FROM _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS;
		EXCEPTION WHEN undefined_table THEN
			-- Simplemente ignora.
		END;

		BEGIN
			INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO(  id_pol, id_part, metpagopol, fecha, benef, rfc, monto, moneda, tipcamb  )
			SELECT __IDENTITY, id_part, metpagopol, fecha, benef, rfc, monto, moneda, tipcamb
			FROM _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO;
		EXCEPTION WHEN undefined_table THEN
			-- Simplemente ignora.
		END;

		BEGIN
			INSERT INTO TBL_CONT_POLIZAS_DETALLE_CE_COMPROBANTES( id_pol, id_part, uuid_cfdi, monto, rfc, id_tipo, moneda, tipcamb, cfd_cbb_serie, cfd_cbb_numfol, numfactext, taxid )
			SELECT __IDENTITY, id_part, uuid_cfdi, monto, rfc, id_tipo, moneda, tipcamb, cfd_cbb_serie, cfd_cbb_numfol, numfactext, taxid
			FROM _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
		EXCEPTION WHEN undefined_table THEN
			-- Simplemente ignora.
		END;
		
		UPDATE TBL_CONT_POLIZAS_TIPOS
		SET Numero = _numero + 1
		WHERE ID_Tipo = _Tipo;
		
	END IF;

	RETURN QUERY SELECT _err, _result, __IDENTITY;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cont_polizas_agregar(character, timestamp without time zone, character varying, bit, character varying, numeric, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cont_catalogo_actualizar_sdos()
  RETURNS SETOF record AS
$BODY$
DECLARE 	
	_err int; _Mes smallint; _Ano smallint; 
	_saldoini numeric(19,4); _saldofin numeric(19,4); _saldomed numeric(19,4); _saldopol numeric(19,4); 
	_abonos numeric(19,4); _retiros numeric(19,4); 
	_PER RECORD; _CTA RECORD; _POL RECORD; 
BEGIN
	_err := 0;
	
	-- crea la tabla temporal de saldos de la cual se actualizar?n los saldos finales
	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_CATALOGO_DETALLE
	(
		mes smallint NOT NULL,
		ano integer NOT NULL,
		cuenta character(19) NOT NULL,
		saldoinicial numeric(19,4) NOT NULL,
		abonos numeric(19,4) NOT NULL,
		retiros numeric(19,4) NOT NULL,
		saldofinal numeric(19,4)
	);	

	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE
	(
		id integer NOT NULL,
		part smallint NOT NULL,
		saldo numeric(19,4)
	);
	
	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_RESULTADOS
	(
		mes smallint NOT NULL,
		ano smallint NOT NULL,
		resultado numeric(19,4) NOT NULL
	);
	
	_Mes := (	select Mes -- el ultimo mes cerrado ( si no hay meses cerrados sera nulo )
				from TBL_CONT_CATALOGO_PERIODOS
				where Cerrado = '1' 
				order by Ano Desc, Mes Desc limit 1);
	_Ano := (	select Ano -- igual en el a?o
				from TBL_CONT_CATALOGO_PERIODOS 
				where Cerrado = '1' 
				order by Ano Desc, Mes Desc limit 1);

	FOR _CTA IN
		( 	SELECT Cuenta FROM TBL_CONT_CATALOGO WHERE Acum = '0' ORDER BY Cuenta Asc  )
	LOOP
		--raise notice 'Cuenta: %', _CTA.Cuenta;
		_saldoini := (select SaldoFinal from TBL_CONT_CATALOGO_DETALLE where Mes = _Mes and Ano = _Ano and Cuenta = _CTA.Cuenta);
		_saldomed := (select SaldoFinal from TBL_CONT_CATALOGO_DETALLE where Mes = _Mes and Ano = _Ano and Cuenta = _CTA.Cuenta);
		_saldopol := (select SaldoFinal from TBL_CONT_CATALOGO_DETALLE where Mes = _Mes and Ano = _Ano and Cuenta = _CTA.Cuenta);

		FOR _PER IN (select * from TBL_CONT_CATALOGO_PERIODOS where Cerrado = '0' order by Ano Asc, Mes Asc)
		LOOP
			-- Mes 13 debería contar la póliza de cierre anual.
			IF _PER.Mes = 13
			THEN
				_retiros := ( SELECT coalesce(sum(d.Debe),0.0) 
							FROM TBL_CONT_POLIZAS_DETALLE_CA d 
							WHERE d.Mes = _PER.Mes and d.Ano = _PER.Ano and d.Cuenta = _CTA.Cuenta ); 
				_abonos := ( SELECT coalesce(sum(d.Haber),0.0) 
							FROM TBL_CONT_POLIZAS_DETALLE_CA d 
							WHERE d.Mes = _PER.Mes and d.Ano = _PER.Ano and d.Cuenta = _CTA.Cuenta );
				_saldopol := _saldopol + _retiros - _abonos;
			ELSE	--Recorre poliza por poliza en la que aparece esta cuenta para actualizar
				_retiros := 0.0;
				_abonos := 0.0;
				FOR _POL IN (select p.fecha, d.id, d.part, d.debe, d.haber from TBL_CONT_POLIZAS_DETALLE d join TBL_CONT_POLIZAS p on p.ID = d.ID where p.Status <> 'C' and date_part('month',p.Fecha) = _PER.Mes and date_part('year',p.Fecha) = _PER.Ano and d.Cuenta = _CTA.Cuenta order by p.fecha, d.id, d.Part )
				LOOP
					_retiros := _retiros + _POL.Debe; 
					_abonos := _abonos + _POL.Haber;
					_saldopol := _saldopol + _POL.Debe - _POL.Haber;

					INSERT INTO _TMP_CONT_POLIZAS_DETALLE(id,part,saldo) 
					VALUES(_POL.ID,_POL.Part,_saldopol);
				END LOOP;
				/* Version anterior cuando no se registraba saldo en la poliza
				_retiros := ( SELECT coalesce(sum(d.Debe),0.0) 
							FROM TBL_CONT_POLIZAS_DETALLE d JOIN TBL_CONT_POLIZAS p ON
								p.ID = d.ID
							WHERE p.Status <> 'C' and date_part('month',p.Fecha) = _PER.Mes and date_part('year',p.Fecha) = _PER.Ano and d.Cuenta = _CTA.Cuenta ); 
				_abonos := ( SELECT coalesce(sum(d.Haber),0.0) 
							FROM TBL_CONT_POLIZAS_DETALLE d JOIN TBL_CONT_POLIZAS p ON
								p.ID = d.ID
							WHERE p.Status <> 'C' and date_part('month',p.Fecha) = _PER.Mes and date_part('year',p.Fecha) = _PER.Ano and d.Cuenta = _CTA.Cuenta ); 
				*/
			END IF;
			
			_saldofin := _saldomed + _retiros - _abonos;
			--raise notice '%/%:   % % - %', _PER.Mes, _PER.Ano, _abonos, _retiros, _saldofin;
			
			--Agrega el saldo del cliente de este periodo
			INSERT INTO _TMP_CONT_CATALOGO_DETALLE(mes,ano,cuenta,saldoinicial,abonos,retiros,saldofinal) 
			VALUES(_PER.Mes, _PER.Ano, _CTA.Cuenta, _SaldoIni, _Abonos, _Retiros, _SaldoFin);
				
			_saldoini := _saldofin;
			_saldomed := _saldofin;

										
		END LOOP;

	END LOOP;

	-- Actualiza los saldos finales
	UPDATE TBL_CONT_CATALOGO_DETALLE
	SET	SaldoInicial = tm.SaldoInicial, Abonos = tm.Abonos, Retiros = tm.Retiros, SaldoFinal = tm.SaldoFinal
	FROM _TMP_CONT_CATALOGO_DETALLE tm, TBL_CONT_CATALOGO_DETALLE c
	WHERE tm.Cuenta = c.Cuenta and tm.Mes = c.Mes and tm.Ano = c.Ano and 
		c.Cuenta = TBL_CONT_CATALOGO_DETALLE.Cuenta and c.Mes = TBL_CONT_CATALOGO_DETALLE.Mes and c.Ano = TBL_CONT_CATALOGO_DETALLE.Ano;

	-- Actualiza los saldos en polizas
	UPDATE TBL_CONT_POLIZAS_DETALLE
	SET	Saldo = tm.Saldo
	FROM _TMP_CONT_POLIZAS_DETALLE tm, TBL_CONT_POLIZAS_DETALLE c
	WHERE tm.ID = c.ID and tm.Part = c.Part and 
		c.ID = TBL_CONT_POLIZAS_DETALLE.ID and c.Part = TBL_CONT_POLIZAS_DETALLE.Part;

	--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	-- CREA LOS SALDOS ACUMULATIVOS DE PRIMER NIVEL
	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_CATALOGO_DETALLE1 AS
	SELECT d.Mes, d.Ano, substring(d.Cuenta,1,4) as Cuenta, sum(d.SaldoInicial) as TotSI, sum(d.Retiros) as TotC, sum(d.Abonos) as TotA, sum(d.SaldoFinal) as TotSF 
	FROM TBL_CONT_CATALOGO_DETALLE d JOIN TBL_CONT_CATALOGO c ON 
		c.Cuenta = d.Cuenta AND c.Acum = '0' JOIN TBL_CONT_CATALOGO_PERIODOS p ON
			p.Mes = d.Mes and p.Ano = d.Ano and p.Cerrado = '0'
	GROUP BY d.Mes, d.Ano, substring(d.Cuenta,1,4);

	UPDATE TBL_CONT_CATALOGO_DETALLE 
	SET SaldoInicial = tm.TotSI, Retiros = tm.TotC, Abonos = tm.TotA, SaldoFinal = tm.TotSF 
	FROM _TMP_CONT_CATALOGO_DETALLE1 tm, TBL_CONT_CATALOGO_DETALLE d
	WHERE d.Mes = tm.Mes AND d.Ano = tm.Ano AND d.Cuenta = tm.Cuenta || '000000000000000' and 
		d.Mes = TBL_CONT_CATALOGO_DETALLE.Mes AND d.Ano = TBL_CONT_CATALOGO_DETALLE.Ano AND d.Cuenta = TBL_CONT_CATALOGO_DETALLE.Cuenta;

	DROP TABLE _TMP_CONT_CATALOGO_DETALLE1;
	-- CREA LOS SALDOS ACUMULATIVOS DE SEGUNDO NIVEL
	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_CATALOGO_DETALLE1 AS
	SELECT d.Mes, d.Ano, substring(d.Cuenta,1,7) as Cuenta, sum(d.SaldoInicial) as TotSI, sum(d.Retiros) as TotC, sum(d.Abonos) as TotA, sum(d.SaldoFinal) as TotSF 
	FROM TBL_CONT_CATALOGO_DETALLE d JOIN TBL_CONT_CATALOGO c ON 
		c.Cuenta = d.Cuenta AND c.Acum = '0' JOIN TBL_CONT_CATALOGO_PERIODOS p ON
			p.Mes = d.Mes and p.Ano = d.Ano and p.Cerrado = '0'
	GROUP BY d.Mes, d.Ano, substring(d.Cuenta,1,7);

	UPDATE TBL_CONT_CATALOGO_DETALLE 
	SET SaldoInicial = tm.TotSI, Retiros = tm.TotC, Abonos = tm.TotA, SaldoFinal = tm.TotSF 
	FROM _TMP_CONT_CATALOGO_DETALLE1 tm, TBL_CONT_CATALOGO_DETALLE d
	WHERE d.Mes = tm.Mes AND d.Ano = tm.Ano AND d.Cuenta = tm.Cuenta || '000000000000' and 
		d.Mes = TBL_CONT_CATALOGO_DETALLE.Mes AND d.Ano = TBL_CONT_CATALOGO_DETALLE.Ano AND d.Cuenta = TBL_CONT_CATALOGO_DETALLE.Cuenta;

	DROP TABLE _TMP_CONT_CATALOGO_DETALLE1;
	-- CREA LOS SALDOS ACUMULATIVOS DE TERCER NIVEL
	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_CATALOGO_DETALLE1 AS
	SELECT d.Mes, d.Ano, substring(d.Cuenta,1,10) as Cuenta, sum(d.SaldoInicial) as TotSI, sum(d.Retiros) as TotC, sum(d.Abonos) as TotA, sum(d.SaldoFinal) as TotSF 
	FROM TBL_CONT_CATALOGO_DETALLE d JOIN TBL_CONT_CATALOGO c ON 
		c.Cuenta = d.Cuenta AND c.Acum = '0' JOIN TBL_CONT_CATALOGO_PERIODOS p ON
			p.Mes = d.Mes and p.Ano = d.Ano and p.Cerrado = '0'
	GROUP BY d.Mes, d.Ano, substring(d.Cuenta,1,10);

	UPDATE TBL_CONT_CATALOGO_DETALLE 
	SET SaldoInicial = tm.TotSI, Retiros = tm.TotC, Abonos = tm.TotA, SaldoFinal = tm.TotSF 
	FROM _TMP_CONT_CATALOGO_DETALLE1 tm, TBL_CONT_CATALOGO_DETALLE d
	WHERE d.Mes = tm.Mes AND d.Ano = tm.Ano AND d.Cuenta = tm.Cuenta || '000000000' and 
		d.Mes = TBL_CONT_CATALOGO_DETALLE.Mes AND d.Ano = TBL_CONT_CATALOGO_DETALLE.Ano AND d.Cuenta = TBL_CONT_CATALOGO_DETALLE.Cuenta;

	DROP TABLE _TMP_CONT_CATALOGO_DETALLE1;
	-- CREA LOS SALDOS ACUMULATIVOS DE CUARTO NIVEL
	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_CATALOGO_DETALLE1 AS
	SELECT d.Mes, d.Ano, substring(d.Cuenta,1,13) as Cuenta, sum(d.SaldoInicial) as TotSI, sum(d.Retiros) as TotC, sum(d.Abonos) as TotA, sum(d.SaldoFinal) as TotSF 
	FROM TBL_CONT_CATALOGO_DETALLE d JOIN TBL_CONT_CATALOGO c ON 
		c.Cuenta = d.Cuenta AND c.Acum = '0' JOIN TBL_CONT_CATALOGO_PERIODOS p ON
			p.Mes = d.Mes and p.Ano = d.Ano and p.Cerrado = '0'
	GROUP BY d.Mes, d.Ano, substring(d.Cuenta,1,13);

	UPDATE TBL_CONT_CATALOGO_DETALLE 
	SET SaldoInicial = tm.TotSI, Retiros = tm.TotC, Abonos = tm.TotA, SaldoFinal = tm.TotSF 
	FROM _TMP_CONT_CATALOGO_DETALLE1 tm, TBL_CONT_CATALOGO_DETALLE d
	WHERE d.Mes = tm.Mes AND d.Ano = tm.Ano AND d.Cuenta = tm.Cuenta || '000000' and 
		d.Mes = TBL_CONT_CATALOGO_DETALLE.Mes AND d.Ano = TBL_CONT_CATALOGO_DETALLE.Ano AND d.Cuenta = TBL_CONT_CATALOGO_DETALLE.Cuenta;

	DROP TABLE _TMP_CONT_CATALOGO_DETALLE1;
	-- CREA LOS SALDOS ACUMULATIVOS DE QUINTO NIVEL
	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_CATALOGO_DETALLE1 AS
	SELECT d.Mes, d.Ano, substring(d.Cuenta,1,16) as Cuenta, sum(d.SaldoInicial) as TotSI, sum(d.Retiros) as TotC, sum(d.Abonos) as TotA, sum(d.SaldoFinal) as TotSF 
	FROM TBL_CONT_CATALOGO_DETALLE d JOIN TBL_CONT_CATALOGO c ON 
		c.Cuenta = d.Cuenta AND c.Acum = '0' JOIN TBL_CONT_CATALOGO_PERIODOS p ON
			p.Mes = d.Mes and p.Ano = d.Ano and p.Cerrado = '0'
	GROUP BY d.Mes, d.Ano, substring(d.Cuenta,1,16);

	UPDATE TBL_CONT_CATALOGO_DETALLE 
	SET SaldoInicial = tm.TotSI, Retiros = tm.TotC, Abonos = tm.TotA, SaldoFinal = tm.TotSF 
	FROM _TMP_CONT_CATALOGO_DETALLE1 tm, TBL_CONT_CATALOGO_DETALLE d
	WHERE d.Mes = tm.Mes AND d.Ano = tm.Ano AND d.Cuenta = tm.Cuenta || '000' and 
		d.Mes = TBL_CONT_CATALOGO_DETALLE.Mes AND d.Ano = TBL_CONT_CATALOGO_DETALLE.Ano AND d.Cuenta = TBL_CONT_CATALOGO_DETALLE.Cuenta;

	DROP TABLE _TMP_CONT_CATALOGO_DETALLE1;
	--//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	-- Calcula el resultado del ejercicio
	UPDATE TBL_CONT_RESULTADOS
	SET Resultado =
		(select case when sum(d.SaldoFinal) - sum(d.SaldoInicial) is null then 0.0000 
			     else sum(d.SaldoFinal) - sum(d.SaldoInicial) end
	   		from TBL_CONT_CATALOGO_DETALLE d INNER JOIN TBL_CONT_CATALOGO c ON 
			d.Cuenta = c.Cuenta INNER JOIN TBL_CONT_RUBROS r ON
	   		substring(d.Cuenta,1,4) between r.CuentaInicial and r.CuentaFinal
  	   		where c.Acum = '0' and d.Mes = tm.Mes and d.Ano = tm.Ano and (r.Tipo = 'RI' or r.Tipo = 'RC' or r.Tipo = 'RG' or
				r.Tipo = 'RO'))
	FROM TBL_CONT_CATALOGO_PERIODOS tm, TBL_CONT_RESULTADOS c
	WHERE tm.Cerrado = '0' and
		tm.Mes = c.Mes and tm.Ano = c.Ano and 
		c.Mes = TBL_CONT_RESULTADOS.Mes and c.Ano = TBL_CONT_RESULTADOS.Ano;

	-- Actualiza los saldos entidades de cuentas
	_Mes := (	select Mes -- el ultimo mes abierto ( si no hay meses abiertos sera nulo )
				from TBL_CONT_CATALOGO_PERIODOS
				where Mes <> 13 and Cerrado = '0' 
				order by Ano Desc, Mes Desc limit 1);
	_Ano := (	select Ano -- igual en el a?o
				from TBL_CONT_CATALOGO_PERIODOS 
				where Mes <> 13 and Cerrado = '0' 
				order by Ano Desc, Mes Desc limit 1);

	IF _Mes is not null AND _Ano is not null
	THEN
		--raise notice 'FINAL DEL PERIODO: mes % ano %', _Mes, _Ano;

		UPDATE TBL_CONT_CATALOGO
		SET Saldo = c.SaldoFinal 
		FROM TBL_CONT_CATALOGO e, TBL_CONT_CATALOGO_DETALLE c 
		WHERE
			e.Cuenta = c.Cuenta and
			c.Cuenta =  TBL_CONT_CATALOGO.Cuenta and
			c.Ano = _Ano and c.Mes = _Mes;
	END IF;

	DROP TABLE _TMP_CONT_CATALOGO_DETALLE ;
	DROP TABLE _TMP_CONT_RESULTADOS ;
	DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
	
	RETURN QUERY 
	SELECT 0 as err, 'LOS SALDOS DE CUENTAS CONTABLES SE ACTUALIZARON SATISFACTORIAMENTE'::varchar as res;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_cont_catalogo_actualizar_sdos()
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getchar19account(_account character varying)
  RETURNS character varying AS
$BODY$  
DECLARE _cuenta character varying(4); _res character varying(19);
BEGIN
	_cuenta := substring(_Account, 1, 4);
	_res := _cuenta;

	_cuenta := substring(_Account, 6 , 3);
	--raise notice 'L2:%', _cuenta;
	if _cuenta <> ''
	then
		_res := _res || _cuenta;
		_cuenta := substring(_Account, 10 , 3);
		--raise notice 'L3%', _cuenta;
		if _cuenta <> ''
		then
			_res := _res || _cuenta;
			_cuenta := substring(_Account, 14 , 3);
			--raise notice 'L4%', _cuenta;
			if _cuenta <> ''
			then
				_res := _res || _cuenta;
				_cuenta := substring(_Account, 18 , 3);
				--raise notice 'L5%', _cuenta;
				if _cuenta <> ''
				then
					_res := _res || _cuenta;
					_cuenta := substring(_Account, 22 , 3);
					--raise notice 'L6%', _cuenta;
					if _cuenta <> ''
					then
						_res := _res || _cuenta;
					else
						_res := _res || '000';
					end if;
				else
					_res := _res || '000000';
				end if;
			else
				_res := _res || '000000000';
			end if;
		else
			_res := _res || '000000000000';
		end if;
	else
		_res := _res || '000000000000000';
	end if;

	return _res;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getchar19account(character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getmeslargo(_mes integer)
  RETURNS character varying AS
$BODY$  
BEGIN
	if _Mes = 1 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'ENE');
	elsif _Mes = 2 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'FEB');
	elsif _Mes = 3 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'MAR');
	elsif _Mes = 4 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'ABR');
	elsif _Mes = 5
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'MAY');
	elsif _Mes = 6 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'JUN');
	elsif _Mes = 7 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'JUL');
	elsif _Mes = 8 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'AGO');
	elsif _Mes = 9 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'SEP');
	elsif _Mes = 10 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'OCT');
	elsif _Mes = 11 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'NOV');
	elsif _Mes = 12 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'DIC');
	elsif _Mes = 13 
	then 
		return (select msj4 from tbl_msj m where m.alc = 'GLB' and m.mod = 'GLB' and m.sub = 'GLB' and m.elm = 'ESP');
	else
		return ('Err: mes ' + cast(Mes as varchar))::varchar;
	end if;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getmeslargo(integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getpolizaconce(_id_pol integer)
  RETURNS boolean AS
$BODY$  
BEGIN
	IF (SELECT count(*) FROM tbl_cont_polizas_detalle_ce_comprobantes WHERE tbl_cont_polizas_detalle_ce_comprobantes.id_pol = _id_pol) > 0
	THEN
		RETURN true;
	ELSIF (SELECT count(*) FROM tbl_cont_polizas_detalle_ce_cheques WHERE tbl_cont_polizas_detalle_ce_cheques.id_pol = _id_pol) > 0 
	THEN 
		RETURN true;
	ELSIF (SELECT count(*) FROM tbl_cont_polizas_detalle_ce_transferencias WHERE tbl_cont_polizas_detalle_ce_transferencias.id_pol = _id_pol) > 0
	THEN 
		RETURN true;
	ELSIF (SELECT count(*) FROM tbl_cont_polizas_detalle_ce_otrmetodopago WHERE tbl_cont_polizas_detalle_ce_otrmetodopago.id_pol = _id_pol) > 0 
	THEN 
		RETURN true;
        ELSE
		RETURN false;
	END IF;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getpolizaconce(integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION gettipopoliza(_tipo character)
  RETURNS character varying AS
$BODY$  
BEGIN
	if _tipo = 'DR'
	then 
		return 'Diario';
	elsif _tipo = 'IG'
	then
		return 'Ingresos';
	elsif _tipo = 'EG'
	then
		return 'Egresos';
	elsif _tipo = 'AJ'
	then
		return 'Ajustes';
	elsif _tipo = 'PE'
	then 
		return 'Especial';
	else 
		return 'Otra';
	end if;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION gettipopoliza(character)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cont_polizas_detalle_ce_cheques AS 
 SELECT ce.id, ce.id_pol, ce.id_part, ce.num AS cheque, ce.banco AS id_banco, 
        CASE
            WHEN ce.banco <> '000'::bpchar THEN b.nombre
            ELSE ce.banemisext
        END AS banco, ce.ctaori AS cuenta, ce.fecha, ce.monto, ce.benef AS beneficiario, ce.rfc, ce.moneda, ce.tipcamb AS tc, round(ce.monto * ce.tipcamb, 2) AS total
   FROM tbl_cont_polizas_detalle_ce_cheques ce
   JOIN tbl_sat_bancos b ON ce.banco = b.clave;

ALTER TABLE view_cont_polizas_detalle_ce_cheques
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cont_polizas_detalle_ce_comprobantes AS 
 SELECT ce.id, ce.id_pol, ce.id_part, ce.uuid_cfdi AS uuid, ce.monto, ce.rfc, ce.id_tipo AS tipo, ce.moneda, ce.tipcamb AS tc, ce.cfd_cbb_serie AS serie, ce.cfd_cbb_numfol AS folio, ce.numfactext AS factura, ce.taxid, round(ce.monto * ce.tipcamb, 2) AS total
   FROM tbl_cont_polizas_detalle_ce_comprobantes ce;

ALTER TABLE view_cont_polizas_detalle_ce_comprobantes
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cont_polizas_detalle_ce_otrmetodopago AS 
 SELECT ce.id, ce.id_pol, ce.id_part, ce.metpagopol, mp.nombre AS metodo, ce.fecha, ce.benef AS beneficiario, ce.rfc, ce.monto, ce.moneda, ce.tipcamb AS tc, round(ce.monto * ce.tipcamb, 2) AS total
   FROM tbl_cont_polizas_detalle_ce_otrmetodopago ce
   JOIN tbl_sat_metodospago mp ON ce.metpagopol = mp.clave;

ALTER TABLE view_cont_polizas_detalle_ce_otrmetodopago
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cont_polizas_detalle_ce_transferencias AS 
 SELECT ce.id, ce.id_pol, ce.id_part, ce.ctaori AS cuenta, ce.bancoori AS id_banco, ce.monto, ce.ctadest AS cuentadest, ce.bancodest AS id_bancodest, ce.fecha, ce.benef AS beneficiario, ce.rfc, 
        CASE
            WHEN ce.bancoori <> '000'::bpchar THEN b.nombre
            ELSE ce.bancooriext
        END AS banco, 
        CASE
            WHEN ce.bancodest <> '000'::bpchar THEN ( SELECT tbl_sat_bancos.nombre
               FROM tbl_sat_bancos
              WHERE tbl_sat_bancos.clave = ce.bancodest)
            ELSE ce.bancodestext
        END AS bancodest, ce.moneda, ce.tipcamb AS tc, round(ce.monto * ce.tipcamb, 2) AS total
   FROM tbl_cont_polizas_detalle_ce_transferencias ce
   JOIN tbl_sat_bancos b ON ce.bancoori = b.clave;

ALTER TABLE view_cont_polizas_detalle_ce_transferencias
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getformataccount(_account character)
  RETURNS character varying AS
$BODY$  
DECLARE _res character varying(24);
BEGIN
	IF getlevelaccount(_account) = 1
	 THEN
		_res := substring(_account, 1, 4);
	ELSIF getlevelaccount(_account) = 2
	 THEN 
		_res := substring(_account, 1, 4) || '-' || substring(_account, 5, 3);
	ELSIF getlevelaccount(_account) = 3
	THEN 
		_res := substring(_account, 1, 4) || '-' || substring(_account, 5, 3) || '-' || substring(_account, 8, 3);
	ELSIF getlevelaccount(_account) = 4 
	THEN 
		_res := substring(_account, 1, 4) || '-' || substring(_account, 5, 3) || '-' || substring(_account, 8, 3) || '-' || substring(_account, 11, 3);
	ELSIF getlevelaccount(_account) = 5
	THEN 
		_res := substring(_account, 1, 4) || '-' || substring(_account, 5, 3) || '-' || substring(_account, 8, 3) || '-' || substring(_account, 11, 3) || '-' || substring(_account, 14, 3);
	ELSIF getlevelaccount(_account) = 6
	THEN
		_res := substring(_account, 1, 4) || '-' || substring(_account, 5, 3) || '-' || substring(_account, 8, 3) || '-' || substring(_account, 11, 3) || '-' || substring(_account, 14, 3) || '-' || substring(_account, 17, 3);
	ELSE
		_res := _account;
	END IF;

	return _res;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getformataccount(character)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_clasificaciones AS 
 SELECT tbl_cont_polizas_clasificaciones.id_clasificacion AS clave, tbl_cont_polizas_clasificaciones.descripcion, tbl_cont_polizas_clasificaciones.ficha AS especial
   FROM tbl_cont_polizas_clasificaciones;

ALTER TABLE view_catalog_clasificaciones
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_cuentascontdet AS 
 SELECT 
        CASE
            WHEN getconfcc() = 1 THEN "substring"(c1.cuenta::text, 1, 4)::bpchar
            WHEN getconfcc() = 2 THEN (("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3))::bpchar
            WHEN getconfcc() = 3 THEN (((("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3)) || '-'::text) || "substring"(c1.cuenta::text, 8, 3))::bpchar
            WHEN getconfcc() = 4 THEN (((((("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3)) || '-'::text) || "substring"(c1.cuenta::text, 8, 3)) || '-'::text) || "substring"(c1.cuenta::text, 11, 3))::bpchar
            WHEN getconfcc() = 5 THEN (((((((("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3)) || '-'::text) || "substring"(c1.cuenta::text, 8, 3)) || '-'::text) || "substring"(c1.cuenta::text, 11, 3)) || '-'::text) || "substring"(c1.cuenta::text, 14, 3))::bpchar
            WHEN getconfcc() = 6 THEN (((((((((("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3)) || '-'::text) || "substring"(c1.cuenta::text, 8, 3)) || '-'::text) || "substring"(c1.cuenta::text, 11, 3)) || '-'::text) || "substring"(c1.cuenta::text, 14, 3)) || '-'::text) || "substring"(c1.cuenta::text, 17, 3))::bpchar
            ELSE c1.cuenta
        END AS clave, 
        CASE
            WHEN getlevelaccount(c1.cuenta) = 1 THEN c1.nombre
            WHEN getlevelaccount(c1.cuenta) = 2 THEN ((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || c1.nombre::text)::character varying
            WHEN getlevelaccount(c1.cuenta) = 3 THEN ((((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || ((( SELECT c3.nombre
               FROM tbl_cont_catalogo c3
              WHERE ("substring"(c1.cuenta::text, 1, 7) || '000000000000'::text) = c3.cuenta::text))::text)) || '/'::text) || c1.nombre::text)::character varying
            WHEN getlevelaccount(c1.cuenta) = 4 THEN ((((((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || ((( SELECT c3.nombre
               FROM tbl_cont_catalogo c3
              WHERE ("substring"(c1.cuenta::text, 1, 7) || '000000000000'::text) = c3.cuenta::text))::text)) || '/'::text) || ((( SELECT c4.nombre
               FROM tbl_cont_catalogo c4
              WHERE ("substring"(c1.cuenta::text, 1, 10) || '000000000'::text) = c4.cuenta::text))::text)) || '/'::text) || c1.nombre::text)::character varying
            WHEN getlevelaccount(c1.cuenta) = 5 THEN ((((((((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || ((( SELECT c3.nombre
               FROM tbl_cont_catalogo c3
              WHERE ("substring"(c1.cuenta::text, 1, 7) || '000000000000'::text) = c3.cuenta::text))::text)) || '/'::text) || ((( SELECT c4.nombre
               FROM tbl_cont_catalogo c4
              WHERE ("substring"(c1.cuenta::text, 1, 10) || '000000000'::text) = c4.cuenta::text))::text)) || '/'::text) || ((( SELECT c5.nombre
               FROM tbl_cont_catalogo c5
              WHERE ("substring"(c1.cuenta::text, 1, 13) || '000000'::text) = c5.cuenta::text))::text)) || '/'::text) || c1.nombre::text)::character varying
            WHEN getlevelaccount(c1.cuenta) = 6 THEN ((((((((((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || ((( SELECT c3.nombre
               FROM tbl_cont_catalogo c3
              WHERE ("substring"(c1.cuenta::text, 1, 7) || '000000000000'::text) = c3.cuenta::text))::text)) || '/'::text) || ((( SELECT c4.nombre
               FROM tbl_cont_catalogo c4
              WHERE ("substring"(c1.cuenta::text, 1, 10) || '000000000'::text) = c4.cuenta::text))::text)) || '/'::text) || ((( SELECT c5.nombre
               FROM tbl_cont_catalogo c5
              WHERE ("substring"(c1.cuenta::text, 1, 13) || '000000'::text) = c5.cuenta::text))::text)) || '/'::text) || ((( SELECT c6.nombre
               FROM tbl_cont_catalogo c6
              WHERE ("substring"(c1.cuenta::text, 1, 16) || '000'::text) = c6.cuenta::text))::text)) || '/'::text) || c1.nombre::text)::character varying
            ELSE c1.nombre
        END AS descripcion, '&nbsp;'::text AS especial
   FROM tbl_cont_catalogo c1
  WHERE c1.acum = B'0'::"bit";

ALTER TABLE view_catalog_cuentascontdet
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION view_reportes(
    _id_usuario character varying,
    _tipo character varying,
    _entidad character varying)
  RETURNS SETOF record AS
$BODY$ 
DECLARE
	_modulo character varying(30) ;
BEGIN
		
	IF _ID_Usuario = 'cef-su'
	THEN
		IF _Entidad = 'CEF-X' -- Regresa todo el set
		THEN
			IF position( '_' in _tipo ) = 0 THEN _modulo := _tipo || '_%'; ELSE _modulo := _tipo; END IF;
			
			RETURN QUERY
			SELECT _id_usuario as id_usuario, m.id_report, m.description, ms.msj2 as tipo, m.subtipo, m.clave, m.graficar
			FROM tbl_reports m JOIN tbl_msj ms ON 
				ms.alc::text = 'CEF'::text AND ms.mod::text = 'PERMISOS'::text AND ms.sub::text = 'CATALOGO'::text AND ms.elm::text = m.tipo::text
			WHERE m.tipo LIKE _modulo
			ORDER BY m.id_report ASC; 
		ELSE -- _Entidad = 'CEF-1' --Regresa el reporte especificado en _tipo... _tipo contendrá el id del reporte
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, m.id_report, m.description, ms.msj2 as tipo, m.subtipo, m.clave, m.graficar
			FROM tbl_reports m JOIN tbl_msj ms ON 
				ms.alc::text = 'CEF'::text AND ms.mod::text = 'PERMISOS'::text AND ms.sub::text = 'CATALOGO'::text AND ms.elm::text = m.tipo::text
			WHERE m.id_report = _tipo::smallint;
		END IF;
	ELSE
		IF _Entidad = 'CEF-X' -- Regresa todo el set
		THEN
			IF position( '_' in _tipo ) = 0 THEN _modulo := _tipo || '_%'; ELSE _modulo := _tipo; END IF;
			
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, m.id_report, m.description, ms.msj2 as tipo, m.subtipo, m.clave, m.graficar
			FROM tbl_reports m JOIN tbl_msj ms ON 
				ms.alc::text = 'CEF'::text AND ms.mod::text = 'PERMISOS'::text AND ms.sub::text = 'CATALOGO'::text AND ms.elm::text = m.tipo::text
			JOIN tbl_usuarios_submodulo_reportes u ON m.id_report = u.id_report
			WHERE m.tipo LIKE _modulo AND u.id_usuario = _id_usuario
			ORDER BY m.id_report ASC;
		ELSE -- _Entidad = 'CEF-1' --Especifico
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, m.id_report, m.description, ms.msj2 as tipo, m.subtipo, m.clave, m.graficar
			FROM tbl_reports m JOIN tbl_msj ms ON 
				ms.alc::text = 'CEF'::text AND ms.mod::text = 'PERMISOS'::text AND ms.sub::text = 'CATALOGO'::text AND ms.elm::text = m.tipo::text
			JOIN tbl_usuarios_submodulo_reportes u ON m.id_report = u.id_report
			WHERE m.id_report = _tipo::smallint  AND u.id_usuario = _id_usuario;
		END IF;
	END IF;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION view_reportes(character varying, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_bancaj_estadocuenta(
    _acum bit,
    _bancaj smallint,
    _clave smallint,
    _mes smallint,
    _ano smallint,
    _orden bit)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_SaldoIni numeric(19,4); _Depositos numeric(19,4); 
		_Retiros numeric(19,4); _SaldoFin numeric(19,4); _SaldoFinEst numeric(19,4);
BEGIN
	IF _Acum = '0'
	THEN
		if _Orden = '0'
		then
			RETURN QUERY 
			select Num, Fecha, Concepto, Beneficiario, Doc as Ref,  Deposito, Retiro, Saldo
			from TBL_BANCOS_MOVIMIENTOS
			where Tipo = _BanCaj and Clave = _Clave and Estatus <> 'C' and date_part('Month',Fecha) = _Mes and date_part('Year',Fecha) = _Ano
			order by Fecha asc, Num asc;
		else
			RETURN QUERY 
			select Num, Fecha, Concepto, Beneficiario, Doc as Ref,  Deposito, Retiro, Saldo
			from TBL_BANCOS_MOVIMIENTOS
			where Tipo = _BanCaj and Clave = _Clave and Estatus <> 'C' and date_part('Month',Fecha) = _Mes and date_part('Year',Fecha) = _Ano
			order by Fecha desc, Num desc;
		end if;
	ELSE
		_SaldoIni := coalesce( ( 	select SaldoIni
								from TBL_BANCOS_CUENTAS_SALDOS
								where Tipo = _BanCaj and Clave = _Clave and Mes = _Mes and Ano = _Ano), 0);
		_SaldoFin := coalesce( ( 	select SaldoFin
									from TBL_BANCOS_CUENTAS_SALDOS
									where Tipo = _BanCaj and Clave = _Clave and Mes = _Mes and Ano = _Ano), 0);
		_Depositos := ( 	select coalesce(sum(Deposito),0)
						from TBL_BANCOS_MOVIMIENTOS  
						where Tipo = _BanCaj and Clave = _Clave and Estatus <> 'C' and date_part('Month',Fecha) = _Mes and date_part('Year',Fecha) = _Ano );
		_Retiros := ( select coalesce(sum(Retiro),0)
					from TBL_BANCOS_MOVIMIENTOS
					where Tipo = _BanCaj and Clave = _Clave and Estatus <> 'C' and date_part('Month',Fecha) = _Mes and date_part('Year',Fecha) = _Ano );

		RETURN QUERY 
		select 'TOTALES:'::varchar as Totales, _SaldoIni as Inicial, _Depositos as Depositos, 
			 _Retiros as Retiros, _SaldoFin as Saldo;
	END IF;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_bancaj_estadocuenta(bit, smallint, smallint, smallint, smallint, bit)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getmoduloref(_ref character varying)
  RETURNS text AS
$BODY$  
DECLARE _mod4 character(4); _idreg character varying(10); _res text;
BEGIN
	IF _Ref is null or _Ref = ''
	THEN
		RETURN ''::text;
	END IF;
	
	_mod4 := split_part(_Ref, '|', 1);
	_idreg := split_part(_Ref, '|', 2);
	--Empieza en el modulo de ventas
	IF _mod4 = 'VDEV' 
	THEN
		_res := (
		select 'Devolución s/venta - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_VENTAS_DEVOLUCIONES_CAB c inner join TBL_VENTAS_ENTIDADES e on c.id_entidad = e.id_entidadventa
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'VFAC' 
	THEN
		_res := (
		select 'Factura - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_VENTAS_FACTURAS_CAB c inner join TBL_VENTAS_ENTIDADES e on c.id_entidad = e.id_entidadventa
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'VREM' 
	THEN
		_res := (
		select 'Remisión - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_VENTAS_REMISIONES_CAB c inner join TBL_VENTAS_ENTIDADES e on c.id_entidad = e.id_entidadventa
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'VPED' 
	THEN
		_res := (
		select 'Pedido - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_VENTAS_PEDIDOS_CAB c inner join TBL_VENTAS_ENTIDADES e on c.id_entidad = e.id_entidadventa
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'VCOT' 
	THEN
		_res := (
		select 'Cotización - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_VENTAS_COTIZACIONES_CAB c inner join TBL_VENTAS_ENTIDADES e on c.id_entidad = e.id_entidadventa
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'VCXC' 
	THEN
		_res := (
		select 'Cuenta por cobrar - Entidad: ' || e.Descripcion || ' ' || 
			(case when c.id_tipocp = 'ALT' then 'Cuenta' 
			when c.id_tipocp = 'ANT' then 'Anticipo' 
			when c.id_tipocp = 'PAG' then 'Pago' 
			when c.id_tipocp = 'SAL' then 'Saldo' 
			when c.id_tipocp = 'APL' then 'Aplicacion' 
			when c.id_tipocp = 'DPA' then 'Descuento' 
			when c.id_tipocp = 'DEV' then 'Devolucion' 
			else '???' end) || ': ' || c.id_cp
		from TBL_CLIENT_CXC c inner join TBL_VENTAS_ENTIDADES e on c.id_entidad = e.id_entidadventa 
		where ID_CP = cast( _idreg as int) );
	--Prosigue con el modulo de compras
	ELSIF _mod4 = 'CDEV' 
	THEN
		_res := (
		select 'Devolución s/compra - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_COMPRAS_DEVOLUCIONES_CAB c inner join TBL_COMPRAS_ENTIDADES e on c.id_entidad = e.id_entidadcompra
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'CFAC' 
	THEN
		_res := (
		select 'Compra - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_COMPRAS_FACTURAS_CAB c inner join TBL_COMPRAS_ENTIDADES e on c.id_entidad = e.id_entidadcompra
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'CREC' 
	THEN
		_res := (
		select 'Recepción - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_COMPRAS_RECEPCIONES_CAB c inner join TBL_COMPRAS_ENTIDADES e on c.id_entidad = e.id_entidadcompra
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'CORD' 
	THEN
		_res := (
		select 'Orden de compra - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_COMPRAS_ORDENES_CAB c inner join TBL_COMPRAS_ENTIDADES e on c.id_entidad = e.id_entidadcompra
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'CGAS' 
	THEN
		_res := (
		select 'Gasto - Entidad: ' || e.Descripcion || ' Num: ' || c.Numero
		from TBL_COMPRAS_GASTOS_CAB c inner join TBL_COMPRAS_ENTIDADES e on c.id_entidad = e.id_entidadcompra
		where ID_VC = cast( _idreg as int) );
	ELSIF _mod4 = 'CCXP' 
	THEN
		_res := (
		select 'Cuenta por pagar - Entidad: ' || e.Descripcion || ' ' || 
			(case when c.id_tipocp = 'ALT' then 'Cuenta' 
			when c.id_tipocp = 'ANT' then 'Anticipo' 
			when c.id_tipocp = 'PAG' then 'Pago' 
			when c.id_tipocp = 'SAL' then 'Saldo' 
			when c.id_tipocp = 'APL' then 'Aplicacion' 
			when c.id_tipocp = 'DPA' then 'Descuento' 
			when c.id_tipocp = 'DEV' then 'Devolucion' 
			else '???' end) || ': ' || c.id_cp 
		from TBL_PROVEE_CXP c inner join TBL_COMPRAS_ENTIDADES e on c.id_entidad = e.id_entidadcompra
		where ID_CP = cast( _idreg as int) );
	--Modulo de caja y bancos
	ELSIF _mod4 = 'MBAN' 
	THEN
		_res := (
		select 'Mov. de Banco - Entidad: ' || e.Cuenta || ' Num: ' || c.Num
		from TBL_BANCOS_MOVIMIENTOS c inner join TBL_BANCOS_CUENTAS e on c.tipo = e.tipo and c.clave = e.clave
		where ID = cast( _idreg as int) );
	ELSIF _mod4 = 'MCAJ' 
	THEN
		_res := (
		select 'Mov. de Caja - Entidad: ' || e.Cuenta || ' Num: ' || c.Num
		from TBL_BANCOS_MOVIMIENTOS c inner join TBL_BANCOS_CUENTAS e on c.tipo = e.tipo and c.clave = e.clave
		where ID = cast( _idreg as int) );
	--Modulo de nómina
	ELSIF _mod4 = 'NNOM' 
	THEN
		_res := (
		select 'Nómina - Entidad: ' || e.Descripcion || ' Nom: ' || c.Numero_Nomina || ' - ' || c.Ano
		from TBL_NOM_CALCULO_NOMINA c inner join TBL_COMPANIAS e on c.id_compania = e.id_compania and c.id_sucursal = e.id_sucursal
		where ID_Nomina = cast( _idreg as int) );
	--Modulo de almacen
	ELSIF _mod4 = 'MALM' 
	THEN
		_res := (
		select 'Mov. al Almacén - Bodega: ' || e.Nombre || ' Num: ' || c.Numero
		from tbl_invserv_almacen_movim_cab c inner join tbl_invserv_bodegas e on c.id_bodega = e.id_bodega
		where ID_Movimiento = cast( _idreg as int) );
	ELSIF _mod4 = 'PALM' 
	THEN
		_res := (
		select 'Plantilla - Bodega: ' || e.Nombre || ' Num: ' || c.Numero
		from tbl_invserv_almacen_movim_plant_cab c inner join tbl_invserv_bodegas e on c.id_bodega = e.id_bodega
		where ID_MovimPlant = cast( _idreg as int) );
	ELSIF _mod4 = 'TALM' 
	THEN
		_res := (
		select 'Traspaso - Bodega: ' || e.Nombre || ' Num: ' || c.Salida
		from tbl_invserv_almacen_bod_mov_cab c inner join tbl_invserv_bodegas e on c.id_bodega = e.id_bodega
		where ID_Movimiento = cast( _idreg as int) );
	ELSIF _mod4 = 'RALM' 
	THEN
		_res := (
		select 'Requerimiento - Bodega: ' || e.Nombre || ' Num: ' || c.Requerimiento
		from tbl_invserv_almacen_bod_req_cab c inner join tbl_invserv_bodegas e on c.id_bodega = e.id_bodega
		where ID_Movimiento = cast( _idreg as int) );
	--Reportes de produccion
	ELSIF _mod4 = 'PPRD' 
	THEN
		_res := (
		select 'Reporte - Entidad: ' || e.Nombre || ' Num: ' || c.Numero || ' Partida: ' || split_part(_Ref, '|', 4)
		from tbl_produccion_reportes_cab c inner join tbl_produccion_entidades e on c.id_entidadprod = e.id_entidadprod
		where ID_Reporte = cast( _idreg as int) );

		
	ELSE
		_res := _ref;
	END IF;
	
	return _res;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getmoduloref(character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_invserv_existencias(
    _id_prod character varying,
    _periodo bit,
    _mes smallint,
    _ano smallint,
    _id_bodega smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_Mes2 smallint; _Ano2 smallint;  _FechaDesde timestamp; _FechaTotal timestamp; _NoAuditados boolean;
BEGIN
	_NoAuditados := false;
	
	if(_Periodo = '1')
	then
		_Mes := ( select Mes from TBL_CONT_CATALOGO_PERIODOS where Cerrado = '1' order by Ano desc, Mes desc limit 1 );
		if _Mes = 13
		then
			_Mes := 12;
		end if;
		_Ano := ( select Ano from TBL_CONT_CATALOGO_PERIODOS where Cerrado = '1' order by Ano desc, Mes desc limit 1 );
	end if;

	_FechaDesde := getfecha(1,_Mes,_Ano);

	_Mes2 := ( select Mes from TBL_CONT_CATALOGO_PERIODOS order by Ano desc, Mes desc limit 1 );
	if _Mes2 = 13
	then
		_Mes2 := 12;
	end if;
	
	_Ano2 := ( select Ano from TBL_CONT_CATALOGO_PERIODOS order by Ano desc, Mes desc limit 1 );
	_FechaDesde := _FechaDesde + '1 month'::interval;
	 
	CREATE LOCAL TEMPORARY TABLE _TMP_MOVIMIENTOS (
		ID_Clave serial NOT NULL ,
		Fecha varchar (20) NOT NULL ,
		Descripcion varchar (254) NOT NULL ,
		Entrada numeric (9,3) NOT NULL ,
		Salida numeric (9,3) NOT NULL ,
		Existencia numeric (9,3) NOT NULL 

	);

	-- Verifica que no hayan movimientos sin auditar
	_FechaTotal := getfecha(1,_Mes2,_Ano2) + '1 month'::interval;

	IF( select id_invserv from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega ) = 'P'
	THEN 
		IF(	select Count(*) from TBL_INVSERV_ALMACEN_MOVIM_CAB
				where ID_Bodega = _ID_Bodega and Fecha < _FechaTotal and ( Status = 'G' or Status = 'P' or Status = 'R' ) ) > 0
		THEN
			_NoAuditados = true;
			INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
			VALUES ('<b>NOTA:</b>', 'EXISTEN MOVIMIENTOS SIN AUDITAR EN ESTA BODEGA. PUEDE QUE EL REPORTE ARROJE RESULTADOS INESPERADOS',0,0,0);

			INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
			SELECT '', '', 0, 0, 0;
		END IF;
	END IF;
	
	INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
	select e.ID_Prod, c.Descripcion || ' SALDO A ' || 
		case when _Mes = 1 then 'ENERO ' 
				when _Mes = 2 then 'FEBRERO '
				when _Mes = 3 then 'MARZO '
				when _Mes = 4 then 'ABRIL '
				when _Mes = 5 then 'MAYO '
				when _Mes = 6 then 'JUNIO '
				when _Mes = 7 then 'JULIO '
				when _Mes = 8 then 'AGOSTO '
				when _Mes = 9 then 'SEPTIEMBRE '
				when _Mes = 10 then 'OCTUBRE '
				when _Mes = 11 then 'NOVIEMBRE '
				else 'DICIEMBRE ' 
			end || cast(_Ano as varchar), 0, 0, coalesce(e.EXistenciaFin,0)
	from TBL_INVSERV_EXISTENCIAS_PERIODOS e INNER JOIN TBL_INVSERV_INVENTARIOS c ON
			e.ID_Prod = c.ID_Prod
	where e.ID_Bodega = _ID_Bodega and e.Mes = _Mes and e.Ano = _Ano and c.ID_Prod = _ID_Prod;

	IF( select id_invserv from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega ) = 'P'
	THEN 
		INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
		SELECT to_char(c.Fecha, 'DD-MM-YYYY')::varchar, (case when _NoAuditados = true and m.Status <> 'U' then '<font color="blue">Estado: ' || m.Status || '</font> ' else '' end) || cc.Descripcion || '  ' || getmoduloref(m.Ref), c.Entrada, c.Salida, c.ExistBod
		FROM TBL_INVSERV_COSTOS_DETALLE c INNER JOIN TBL_INVSERV_INVENTARIOS i ON 
			i.ID_Prod = c.ID_Prod	INNER JOIN TBL_INVSERV_ALMACEN_MOVIM_CAB m ON 
			m.ID_Movimiento = c.ID_Movimiento INNER JOIN VIEW_INVSERV_COSTOS_CONCEPTOS cc ON
			c.ID_Concepto = cc.ID_Concepto
		WHERE  c.Fecha >= _FechaDesde and c.Status <> 'C' and m.ID_Bodega = _ID_Bodega and c.ID_Prod = _ID_Prod
		ORDER BY c.Fecha ASC, c.ID_Concepto ASC, c.ID_Costo ASC;		
	ELSE
		INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
		SELECT to_char(c.Fecha, 'DD-MM-YYYY')::varchar, m.Concepto || '  ' || getmoduloref(m.Ref), c.Entrada, c.Salida, c.ExistBod
		FROM TBL_INVSERV_COSTOS_DETALLE c INNER JOIN TBL_INVSERV_INVENTARIOS i ON 
			i.ID_Prod = c.ID_Prod	INNER JOIN TBL_INVSERV_ALMACEN_MOVIM_CAB m ON 
			m.ID_Movimiento = c.ID_Movimiento 
		WHERE  c.Fecha >= _FechaDesde and c.Status <> 'C' and m.ID_Bodega = _ID_Bodega and c.ID_Prod = _ID_Prod
		ORDER BY c.Fecha ASC, c.ID_Concepto ASC, c.ID_Costo ASC;		
	END IF;

	INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
	SELECT '', '', 0, 0, 0;

	INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
	select '', 'SALDO AL ULTIMO PERIODO CREADO: ' || 
		case when _Mes2 = 1 then 'ENERO ' 
					when _Mes2 = 2 then 'FEBRERO '
					when _Mes2 = 3 then 'MARZO '
					when _Mes2 = 4 then 'ABRIL '
					when _Mes2 = 5 then 'MAYO '
					when _Mes2 = 6 then 'JUNIO '
					when _Mes2 = 7 then 'JULIO '
					when _Mes2 = 8 then 'AGOSTO '
					when _Mes2 = 9 then 'SEPTIEMBRE '
					when _Mes2 = 10 then 'OCTUBRE '
					when _Mes2 = 11 then 'NOVIEMBRE '
					else 'DICIEMBRE ' 
		end || cast(_Ano2 as varchar), 0, 0, coalesce(e.EXistenciaFin,0)
	from TBL_INVSERV_EXISTENCIAS_PERIODOS e INNER JOIN TBL_INVSERV_INVENTARIOS c ON
		e.ID_Prod = c.ID_Prod
	where e.ID_Bodega = _ID_Bodega and e.Mes = _Mes2 and e.Ano = _Ano2 and c.ID_Prod = _ID_Prod;

	INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
	SELECT '', '', 0, 0, 0;

	INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
	SELECT '', 'SALDO DE ESTA BODEGA EN "EXISTENCIAS"', 0, 0, Existencia
	FROM TBL_INVSERV_EXISTENCIAS
	WHERE ID_Bodega = _ID_Bodega and ID_Prod = _ID_Prod;

	INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
	SELECT '', '', 0, 0, 0;

	INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
	SELECT '', 'TOTALES DE ENTRADAS Y SALIDAS...  Y  SALDO REAL', 
		coalesce(	(	SELECT sum(c.Entrada)
				FROM TBL_INVSERV_COSTOS_DETALLE c INNER JOIN TBL_INVSERV_INVENTARIOS i ON 
							i.ID_Prod = c.ID_Prod	INNER JOIN TBL_INVSERV_ALMACEN_MOVIM_CAB m ON 
							m.ID_Movimiento = c.ID_Movimiento INNER JOIN TBL_INVSERV_COSTOS_CONCEPTOS cc ON
							c.ID_Concepto = cc.ID_Concepto
				WHERE  c.Fecha >= _FechaDesde and c.Status <> 'C' and m.ID_Bodega = _ID_Bodega and c.ID_Prod = _ID_Prod ),0), 
		coalesce(	(	SELECT sum(c.Salida)
				FROM TBL_INVSERV_COSTOS_DETALLE c INNER JOIN TBL_INVSERV_INVENTARIOS i ON 
							i.ID_Prod = c.ID_Prod	INNER JOIN TBL_INVSERV_ALMACEN_MOVIM_CAB m ON 
							m.ID_Movimiento = c.ID_Movimiento INNER JOIN TBL_INVSERV_COSTOS_CONCEPTOS cc ON
							c.ID_Concepto = cc.ID_Concepto
				WHERE  c.Fecha >= _FechaDesde and c.Status <> 'C' and m.ID_Bodega = _ID_Bodega and c.ID_Prod = _ID_Prod ),0),
		coalesce(	( select e.EXistenciaFin
					from TBL_INVSERV_EXISTENCIAS_PERIODOS e INNER JOIN TBL_INVSERV_INVENTARIOS c ON
							e.ID_Prod = c.ID_Prod
					where e.ID_Bodega = _ID_Bodega and e.Mes = _Mes and e.Ano = _Ano and c.ID_Prod = _ID_Prod )
				+ 
			(	SELECT sum(c.Entrada) - sum(c.Salida)
				FROM TBL_INVSERV_COSTOS_DETALLE c INNER JOIN TBL_INVSERV_INVENTARIOS i ON 
							i.ID_Prod = c.ID_Prod	INNER JOIN TBL_INVSERV_ALMACEN_MOVIM_CAB m ON 
							m.ID_Movimiento = c.ID_Movimiento INNER JOIN TBL_INVSERV_COSTOS_CONCEPTOS cc ON
							c.ID_Concepto = cc.ID_Concepto
				WHERE  c.Fecha >= _FechaDesde and c.Status <> 'C' and m.ID_Bodega = _ID_Bodega and c.ID_Prod = _ID_Prod ),0);
				
	INSERT INTO _TMP_MOVIMIENTOS ( Fecha, Descripcion, Entrada, Salida, Existencia )
	SELECT '', '', 0, 0, 0;
	
	RETURN QUERY
	select Fecha, Descripcion, Entrada, Salida, Existencia
	from _TMP_MOVIMIENTOS
	order by ID_Clave desc;

	DROP TABLE _TMP_MOVIMIENTOS;
	
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_invserv_existencias(character varying, bit, smallint, smallint, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getchar19lastaccount(_account character varying)
  RETURNS character varying AS
$BODY$  
DECLARE _cuenta character varying(4); _res character varying(19);
BEGIN
	_cuenta := substring(_Account, 1, 4);
	_res := _cuenta;

	_cuenta := substring(_Account, 6 , 3);
	--raise notice 'L2:%', _cuenta;
	if _cuenta <> '' and _cuenta <> '000'
	then
		_res := _res || _cuenta;
		_cuenta := substring(_Account, 10 , 3);
		--raise notice 'L3%', _cuenta;
		if _cuenta <> '' and _cuenta <> '000'
		then
			_res := _res || _cuenta;
			_cuenta := substring(_Account, 14 , 3);
			--raise notice 'L4%', _cuenta;
			if _cuenta <> '' and _cuenta <> '000'
			then
				_res := _res || _cuenta;
				_cuenta := substring(_Account, 18 , 3);
				--raise notice 'L5%', _cuenta;
				if _cuenta <> '' and _cuenta <> '000'
				then
					_res := _res || _cuenta;
					_cuenta := substring(_Account, 22 , 3);
					--raise notice 'L6%', _cuenta;
					if _cuenta <> '' and _cuenta <> '000'
					then
						_res := _res || _cuenta;
					else
						_res := _res || '999';
					end if;
				else
					_res := _res || '999999';
				end if;
			else
				_res := _res || '999999999';
			end if;
		else
			_res := _res || '999999999999';
		end if;
	else
		_res := _res || '999999999999999';
	end if;

	return _res;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getchar19lastaccount(character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_cuentascontacum AS 
 SELECT 
        CASE
            WHEN getconfcc() = 1 THEN "substring"(c1.cuenta::text, 1, 4)::bpchar
            WHEN getconfcc() = 2 THEN (("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3))::bpchar
            WHEN getconfcc() = 3 THEN (((("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3)) || '-'::text) || "substring"(c1.cuenta::text, 8, 3))::bpchar
            WHEN getconfcc() = 4 THEN (((((("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3)) || '-'::text) || "substring"(c1.cuenta::text, 8, 3)) || '-'::text) || "substring"(c1.cuenta::text, 11, 3))::bpchar
            WHEN getconfcc() = 5 THEN (((((((("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3)) || '-'::text) || "substring"(c1.cuenta::text, 8, 3)) || '-'::text) || "substring"(c1.cuenta::text, 11, 3)) || '-'::text) || "substring"(c1.cuenta::text, 14, 3))::bpchar
            WHEN getconfcc() = 6 THEN (((((((((("substring"(c1.cuenta::text, 1, 4) || '-'::text) || "substring"(c1.cuenta::text, 5, 3)) || '-'::text) || "substring"(c1.cuenta::text, 8, 3)) || '-'::text) || "substring"(c1.cuenta::text, 11, 3)) || '-'::text) || "substring"(c1.cuenta::text, 14, 3)) || '-'::text) || "substring"(c1.cuenta::text, 17, 3))::bpchar
            ELSE c1.cuenta
        END AS clave, 
        CASE
            WHEN getlevelaccount(c1.cuenta) = 1 THEN c1.nombre
            WHEN getlevelaccount(c1.cuenta) = 2 THEN ((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || c1.nombre::text)::character varying
            WHEN getlevelaccount(c1.cuenta) = 3 THEN ((((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || ((( SELECT c3.nombre
               FROM tbl_cont_catalogo c3
              WHERE ("substring"(c1.cuenta::text, 1, 7) || '000000000000'::text) = c3.cuenta::text))::text)) || '/'::text) || c1.nombre::text)::character varying
            WHEN getlevelaccount(c1.cuenta) = 4 THEN ((((((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || ((( SELECT c3.nombre
               FROM tbl_cont_catalogo c3
              WHERE ("substring"(c1.cuenta::text, 1, 7) || '000000000000'::text) = c3.cuenta::text))::text)) || '/'::text) || ((( SELECT c4.nombre
               FROM tbl_cont_catalogo c4
              WHERE ("substring"(c1.cuenta::text, 1, 10) || '000000000'::text) = c4.cuenta::text))::text)) || '/'::text) || c1.nombre::text)::character varying
            WHEN getlevelaccount(c1.cuenta) = 5 THEN ((((((((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || ((( SELECT c3.nombre
               FROM tbl_cont_catalogo c3
              WHERE ("substring"(c1.cuenta::text, 1, 7) || '000000000000'::text) = c3.cuenta::text))::text)) || '/'::text) || ((( SELECT c4.nombre
               FROM tbl_cont_catalogo c4
              WHERE ("substring"(c1.cuenta::text, 1, 10) || '000000000'::text) = c4.cuenta::text))::text)) || '/'::text) || ((( SELECT c5.nombre
               FROM tbl_cont_catalogo c5
              WHERE ("substring"(c1.cuenta::text, 1, 13) || '000000'::text) = c5.cuenta::text))::text)) || '/'::text) || c1.nombre::text)::character varying
            WHEN getlevelaccount(c1.cuenta) = 6 THEN ((((((((((((( SELECT c2.nombre
               FROM tbl_cont_catalogo c2
              WHERE ("substring"(c1.cuenta::text, 1, 4) || '000000000000000'::text) = c2.cuenta::text))::text) || '/'::text) || ((( SELECT c3.nombre
               FROM tbl_cont_catalogo c3
              WHERE ("substring"(c1.cuenta::text, 1, 7) || '000000000000'::text) = c3.cuenta::text))::text)) || '/'::text) || ((( SELECT c4.nombre
               FROM tbl_cont_catalogo c4
              WHERE ("substring"(c1.cuenta::text, 1, 10) || '000000000'::text) = c4.cuenta::text))::text)) || '/'::text) || ((( SELECT c5.nombre
               FROM tbl_cont_catalogo c5
              WHERE ("substring"(c1.cuenta::text, 1, 13) || '000000'::text) = c5.cuenta::text))::text)) || '/'::text) || ((( SELECT c6.nombre
               FROM tbl_cont_catalogo c6
              WHERE ("substring"(c1.cuenta::text, 1, 16) || '000'::text) = c6.cuenta::text))::text)) || '/'::text) || c1.nombre::text)::character varying
            ELSE c1.nombre
        END AS descripcion, '&nbsp;'::text AS especial
   FROM tbl_cont_catalogo c1
  WHERE c1.acum = B'1'::"bit";

ALTER TABLE view_catalog_cuentascontacum
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_invserv_almacen_movim_modulo AS 
 SELECT m.id_movimiento, m.numero AS num, m.status, m.fecha, m.id_bodega, b.nombre AS bodega, m.id_concepto, 
        CASE
            WHEN m.id_concepto = (-1) THEN ( SELECT msj.msj1
               FROM tbl_msj msj
              WHERE msj.alc::text = 'GLB'::text AND msj.mod::text = 'GLB'::text AND msj.sub::text = 'GLB'::text AND msj.elm::text = 'ENTRADA'::text)
            WHEN m.id_concepto = (-2) THEN ( SELECT msj.msj1
               FROM tbl_msj msj
              WHERE msj.alc::text = 'GLB'::text AND msj.mod::text = 'GLB'::text AND msj.sub::text = 'GLB'::text AND msj.elm::text = 'SALIDA'::text)
            ELSE 
            CASE
                WHEN c.desistema = B'1'::"bit" THEN ( SELECT msj.msj1
                   FROM tbl_msj msj
                  WHERE msj.alc::text = 'CEF'::text AND msj.mod::text = 'ALMACEN'::text AND msj.sub::text = 'CAT_CON'::text AND msj.elm::text = c.id_concepto::text)
                ELSE c.descripcion
            END
        END AS descripcion, m.concepto, m.referencia, m.id_pol, 
        CASE
            WHEN m.id_pol = (-1) THEN ''::text
            ELSE ( SELECT (tbl_cont_polizas.tipo::text || ' - '::text) || tbl_cont_polizas.numero::character varying::text
               FROM tbl_cont_polizas
              WHERE tbl_cont_polizas.id = m.id_pol)
        END AS pol, m.ref, m.cr_pri, m.cr_sec
   FROM tbl_invserv_almacen_movim_cab m
   JOIN tbl_invserv_costos_conceptos c ON m.id_concepto = c.id_concepto
   JOIN tbl_invserv_bodegas b ON m.id_bodega = b.id_bodega;

ALTER TABLE view_invserv_almacen_movim_modulo
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_invserv_costos_bodegas AS 
 SELECT e.mes, e.ano, e.id_bodega, e.id_prod, e.existenciaini, c.costopromini, c.ultimocostoini, round(COALESCE(e.existenciaini * c.saldoini / NULLIF(c.existenciaini, 0::numeric), 0::numeric)::numeric(19,4), 2) AS saldoini, e.existenciafin, c.costopromfin, c.ultimocostofin, round(COALESCE(e.existenciafin * c.saldofin / NULLIF(c.existenciafin, 0::numeric), 0::numeric)::numeric(19,4), 2) AS saldofin
   FROM tbl_invserv_existencias_periodos e
   JOIN tbl_invserv_costos c ON e.mes = c.mes AND e.ano = c.ano AND e.id_prod::text = c.id_prod::text;

ALTER TABLE view_invserv_costos_bodegas
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_alm_cost_acum(
    _mes smallint,
    _ano smallint,
    _ultimos smallint,
    _noacumulados bit,
    _entcons smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_cont smallint; _TotalEnt smallint; _NumEnt smallint; _NombreEnt varchar(15);
	_TotalAlmacen numeric(19,4); _UltimoAno numeric(19,4); _PenultimoAno numeric(19,4); _AnteriorAno numeric(19,4);
	_ENE numeric(19,4); _FEB numeric(19,4); _MAR numeric(19,4); _ABR numeric(19,4); _MAY numeric(19,4); _JUN numeric(19,4); 
	_JUL numeric(19,4); _AGO numeric(19,4); _SEP numeric(19,4); _OCT numeric(19,4);	_NOV numeric(19,4); _DIC numeric(19,4);
	_TVE RECORD;
BEGIN

	

CREATE LOCAL TEMPORARY TABLE _TMP_ALMACEN_ENT (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_ALMACEN (
		part serial NOT NULL ,
		Periodo varchar (20)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	);
	

CREATE LOCAL TEMPORARY TABLE _TMP_ALMACEN_ENT_ANO (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Ultimo numeric(19,4) NOT NULL ,
		Penultimo numeric(19,4) NOT NULL ,
		Anterior numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_ALMACEN_ENT_MES (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		ENE numeric(19,4) NOT NULL ,
		FEB numeric(19,4) NOT NULL ,
		MAR numeric(19,4) NOT NULL ,
		ABR numeric(19,4) NOT NULL ,
		MAY numeric(19,4) NOT NULL ,
		JUN numeric(19,4) NOT NULL ,
		JUL numeric(19,4) NOT NULL ,
		AGO numeric(19,4) NOT NULL ,
		SEP numeric(19,4) NOT NULL ,
		OCT numeric(19,4) NOT NULL ,
		NOV numeric(19,4) NOT NULL ,
		DIC numeric(19,4) NOT NULL 
	); 

	IF(_NoAcumulados = '0') -- es acumulados generales
	THEN
		IF(_EntCons = -1) -- Se trata del acumulado de entidades generales
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					_TotalAlmacen := coalesce(( select sum(saldofin) from tbl_invserv_costos where mes = _cont and ano = _Ano ),0);

					INSERT INTO _TMP_ALMACEN (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalAlmacen,0);
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual

				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					_TotalAlmacen := coalesce(( select sum(saldofin) from tbl_invserv_costos where mes = 12 and ano = _cont ),0);

					INSERT INTO _TMP_ALMACEN (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalAlmacen,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		
		ELSE -- se trata de una entidad en especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					_TotalAlmacen := coalesce(( select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _EntCons and mes = _cont and ano = _Ano ),0);
					INSERT INTO _TMP_ALMACEN (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalAlmacen,0); 
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual
				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					_TotalAlmacen := coalesce(( select sum(saldofin) from view_invserv_costos_bodegas where  id_bodega = _EntCons and mes = 12 and ano = _cont ),0);
					
					INSERT INTO _TMP_ALMACEN (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalAlmacen,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		END IF;

		RETURN QUERY
		select Periodo, Total
		from _TMP_ALMACEN
		order by part ASC;
	
	ELSE -- Se maneja por detalles de entidad
	
		IF(_EntCons = -1) -- Se trata de comparativos todas las entidades en todos los meses o a?os
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT ID_Bodega, Nombre
								FROM TBL_INVSERV_BODEGAS
								WHERE ID_InvServ = 'P'
								ORDER BY ID_Bodega asc
							)
				loop
					_NumEnt := _TVE.ID_Bodega;
					_NombreEnt := _TVE.Nombre;
		
					_ENE := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 1 and ano = _Ano ), 0);
					_FEB := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 2 and ano = _Ano ), 0);
					_MAR := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 3 and ano = _Ano ), 0);
					_ABR := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 4 and ano = _Ano ), 0);
					_MAY := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 5 and ano = _Ano ), 0);
					_JUN := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 6 and ano = _Ano ), 0);
					_JUL := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 7 and ano = _Ano ), 0);
					_AGO := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 8 and ano = _Ano ), 0);
					_SEP := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 9 and ano = _Ano ), 0);
					_OCT := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 10 and ano = _Ano ), 0);
					_NOV := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 11 and ano = _Ano ), 0);
					_DIC := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where id_bodega = _NumEnt and mes = 12 and ano = _Ano ), 0);
		
					INSERT INTO _TMP_ALMACEN_ENT_MES (Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC)
					SELECT _NombreEnt, round(_ENE,0), round(_FEB,0), round(_MAR,0),  round(_ABR,0),  round(_MAY,0),  round(_JUN,0),  
						round(_JUL,0),  round(_AGO,0),  round(_SEP,0), round(_OCT,0),  round(_NOV,0),  round(_DIC,0);
									
				end loop;

				RETURN QUERY
				select Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC
				from _TMP_ALMACEN_ENT_MES;
		
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT ID_Bodega, Nombre
								FROM TBL_INVSERV_BODEGAS
								WHERE ID_InvServ = 'P'
								ORDER BY ID_Bodega asc
							)
				loop
					_NumEnt := _TVE.ID_Bodega;
					_NombreEnt := _TVE.Nombre;
		
					_UltimoAno := coalesce(( select sum(saldofin) from view_invserv_costos_bodegas where  id_bodega = _NumEnt and mes = 12 and ano = _Ano ),0);
					_PenultimoAno := coalesce(( select sum(saldofin) from view_invserv_costos_bodegas where  id_bodega = _NumEnt and mes = 12 and ano = (_Ano-1) ),0);
					_AnteriorAno := coalesce(( select sum(saldofin) from view_invserv_costos_bodegas where  id_bodega = _NumEnt and mes = 12 and ano = (_Ano-2) ),0);
		
					INSERT INTO _TMP_ALMACEN_ENT_ANO (Entidad,Ultimo,Penultimo,Anterior)
					SELECT _NombreEnt, round(_UltimoAno,0), round(_PenultimoAno,0), round(_AnteriorAno,0);

				end loop;
				
				RETURN QUERY
				select Entidad,Ultimo,Penultimo,Anterior
				from _TMP_ALMACEN_ENT_ANO;
		
			end if;
		
		ELSE -- se trata de comparativos de todas las entidades pero en un mes o ano especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT ID_Bodega, Nombre
								FROM TBL_INVSERV_BODEGAS
								WHERE ID_InvServ = 'P'
								ORDER BY ID_Bodega asc
							)
				loop
					_NumEnt := _TVE.ID_Bodega;
					_NombreEnt := _TVE.Nombre;
		
					_TotalAlmacen := coalesce( (	select sum(saldofin) from view_invserv_costos_bodegas where  id_bodega = _NumEnt and mes = _Mes and ano = _Ano ), 0);
		
					INSERT INTO _TMP_ALMACEN_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalAlmacen,0);   
									
				end loop;
				
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT ID_Bodega, Nombre
								FROM TBL_INVSERV_BODEGAS
								WHERE ID_InvServ = 'P'
								ORDER BY ID_Bodega asc
							)
				loop
					_NumEnt := _TVE.ID_Bodega;
					_NombreEnt := _TVE.Nombre;
					
					_TotalAlmacen := coalesce((  select sum(saldofin) from view_invserv_costos_bodegas where  id_bodega = _NumEnt and mes = 12 and ano = _Ano ),0);
		
					INSERT INTO _TMP_ALMACEN_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalAlmacen,0);  
					
				end loop;
		
			end if;

			RETURN QUERY
			select Entidad,Total
			from _TMP_ALMACEN_ENT;

		END IF;

	END IF;
	
	DROP TABLE _TMP_ALMACEN;
	DROP TABLE _TMP_ALMACEN_ENT;
	DROP TABLE _TMP_ALMACEN_ENT_MES;
	DROP TABLE _TMP_ALMACEN_ENT_ANO;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_alm_cost_acum(smallint, smallint, smallint, bit, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_bancaj_saldos_acum(
    _mes smallint,
    _ano smallint,
    _ultimos smallint,
    _noacumulados bit,
    _tipocons smallint,
    _entcons smallint,
    _promedio bit)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_cont smallint; _TotalEnt smallint; _NumEnt smallint; _NombreEnt varchar(15);
	_TotalBancaj numeric(19,4); _UltimoAno numeric(19,4); _PenultimoAno numeric(19,4); _AnteriorAno numeric(19,4);
	_ENE numeric(19,4); _FEB numeric(19,4); _MAR numeric(19,4); _ABR numeric(19,4); _MAY numeric(19,4); _JUN numeric(19,4); 
	_JUL numeric(19,4); _AGO numeric(19,4); _SEP numeric(19,4); _OCT numeric(19,4);	_NOV numeric(19,4); _DIC numeric(19,4);
	_TVE RECORD;
BEGIN

	

CREATE LOCAL TEMPORARY TABLE _TMP_BANCAJ_ENT (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_BANCAJ (
		part serial NOT NULL ,
		Periodo varchar (20)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	);
	

CREATE LOCAL TEMPORARY TABLE _TMP_BANCAJ_ENT_ANO (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Ultimo numeric(19,4) NOT NULL ,
		Penultimo numeric(19,4) NOT NULL ,
		Anterior numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_BANCAJ_ENT_MES (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		ENE numeric(19,4) NOT NULL ,
		FEB numeric(19,4) NOT NULL ,
		MAR numeric(19,4) NOT NULL ,
		ABR numeric(19,4) NOT NULL ,
		MAY numeric(19,4) NOT NULL ,
		JUN numeric(19,4) NOT NULL ,
		JUL numeric(19,4) NOT NULL ,
		AGO numeric(19,4) NOT NULL ,
		SEP numeric(19,4) NOT NULL ,
		OCT numeric(19,4) NOT NULL ,
		NOV numeric(19,4) NOT NULL ,
		DIC numeric(19,4) NOT NULL 
	); 

	IF(_NoAcumulados = '0') -- es acumulados generales
	THEN
		IF(_EntCons = -1) -- Se trata del acumulado de entidades generales
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					if _Promedio = '0'
					then
						_TotalBancaj := coalesce(( select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.mes = _cont and s.ano = _Ano ),0);
					else
						_TotalBancaj := coalesce(( select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.mes = _cont and s.ano = _Ano ),0);
					end if;
					
					INSERT INTO _TMP_BANCAJ (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalBancaj,0);
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual

				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					if _Promedio = '0'
					then
						_TotalBancaj := coalesce(( select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.mes = 12 and s.ano = _cont ),0);
					else
						_TotalBancaj := coalesce(( select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.mes = 12 and s.ano = _cont ),0);
					end if;
					
					INSERT INTO _TMP_BANCAJ (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalBancaj,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		
		ELSE -- se trata de una entidad en especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					if _Promedio = '0'
					then
						_TotalBancaj := coalesce(( select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _EntCons and s.mes = _cont and s.ano = _Ano ),0);
					else
						_TotalBancaj := coalesce(( select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _EntCons and s.mes = _cont and s.ano = _Ano ),0);
					end if;
					
					INSERT INTO _TMP_BANCAJ (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalBancaj,0); 
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual
				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					if _Promedio = '0'
					then
						_TotalBancaj := coalesce(( select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _EntCons and s.mes = 12 and s.ano = _cont ),0);
					else
						_TotalBancaj := coalesce(( select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _EntCons and mes = 12 and ano = _cont ),0);
					end if;
					
					INSERT INTO _TMP_BANCAJ (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalBancaj,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		END IF;

		RETURN QUERY
		select Periodo, Total
		from _TMP_BANCAJ
		order by part ASC;
	
	ELSE -- Se maneja por detalles de entidad
	
		IF(_EntCons = -1) -- Se trata de comparativos todas las entidades en todos los meses o a?os
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT Clave, Cuenta
								FROM TBL_BANCOS_CUENTAS
								WHERE Tipo = _TipoCons
								ORDER BY Cuenta asc
							)
				loop
					_NumEnt := _TVE.Clave;
					_NombreEnt := _TVE.Cuenta;
		
					if _Promedio = '0'
					then
						_ENE := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 1 and s.ano = _Ano ), 0);
						_FEB := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 2 and s.ano = _Ano ), 0);
						_MAR := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 3 and s.ano = _Ano ), 0);
						_ABR := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 4 and s.ano = _Ano ), 0);
						_MAY := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 5 and s.ano = _Ano ), 0);
						_JUN := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 6 and s.ano = _Ano ), 0);
						_JUL := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 7 and s.ano = _Ano ), 0);
						_AGO := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 8 and s.ano = _Ano ), 0);
						_SEP := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 9 and s.ano = _Ano ), 0);
						_OCT := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 10 and s.ano = _Ano ), 0);
						_NOV := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 11 and s.ano = _Ano ), 0);
						_DIC := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = _Ano ), 0);
					else
						_ENE := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 1 and s.ano = _Ano ), 0);
						_FEB := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 2 and s.ano = _Ano ), 0);
						_MAR := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 3 and s.ano = _Ano ), 0);
						_ABR := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 4 and s.ano = _Ano ), 0);
						_MAY := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 5 and s.ano = _Ano ), 0);
						_JUN := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 6 and s.ano = _Ano ), 0);
						_JUL := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 7 and s.ano = _Ano ), 0);
						_AGO := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 8 and s.ano = _Ano ), 0);
						_SEP := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 9 and s.ano = _Ano ), 0);
						_OCT := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 10 and s.ano = _Ano ), 0);
						_NOV := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 11 and s.ano = _Ano ), 0);
						_DIC := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = _Ano ), 0);
					end if;
					
					INSERT INTO _TMP_BANCAJ_ENT_MES (Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC)
					SELECT _NombreEnt, round(_ENE,0), round(_FEB,0), round(_MAR,0),  round(_ABR,0),  round(_MAY,0),  round(_JUN,0),  
						round(_JUL,0),  round(_AGO,0),  round(_SEP,0), round(_OCT,0),  round(_NOV,0),  round(_DIC,0);
									
				end loop;

				RETURN QUERY
				select Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC
				from _TMP_BANCAJ_ENT_MES;
		
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT Clave, Cuenta
								FROM TBL_BANCOS_CUENTAS
								WHERE Tipo = _TipoCons
								ORDER BY Cuenta asc
							)
				loop
					_NumEnt := _TVE.Clave;
					_NombreEnt := _TVE.Cuenta;
		
					if _Promedio = '0'
					then
						_UltimoAno := coalesce(( select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = _Ano ),0);
						_PenultimoAno := coalesce(( select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = (_Ano-1) ),0);
						_AnteriorAno := coalesce(( select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = (_Ano-2) ),0);
					else
						_UltimoAno := coalesce(( select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = _Ano ),0);
						_PenultimoAno := coalesce(( select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = (_Ano-1) ),0);
						_AnteriorAno := coalesce(( select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = (_Ano-2) ),0);
					end if;
						
					INSERT INTO _TMP_BANCAJ_ENT_ANO (Entidad,Ultimo,Penultimo,Anterior)
					SELECT _NombreEnt, round(_UltimoAno,0), round(_PenultimoAno,0), round(_AnteriorAno,0);

				end loop;
				
				RETURN QUERY
				select Entidad,Ultimo,Penultimo,Anterior
				from _TMP_BANCAJ_ENT_ANO;
		
			end if;
		
		ELSE -- se trata de comparativos de todas las entidades pero en un mes o ano especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT Clave, Cuenta
								FROM TBL_BANCOS_CUENTAS
								WHERE Tipo = _TipoCons
								ORDER BY Cuenta asc
							)
				loop
					_NumEnt := _TVE.Clave;
					_NombreEnt := _TVE.Cuenta;
		
					if _Promedio = '0'
					then
						_TotalBancaj := coalesce( (	select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = _Mes and s.ano = _Ano ), 0);
					else
						_TotalBancaj := coalesce( (	select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = _Mes and s.ano = _Ano ), 0);
					end if;
					
					INSERT INTO _TMP_BANCAJ_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalBancaj,0);   
									
				end loop;
				
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT Clave, Cuenta
								FROM TBL_BANCOS_CUENTAS
								WHERE Tipo = _TipoCons
								ORDER BY Cuenta asc
							)
				loop
					_NumEnt := _TVE.Clave;
					_NombreEnt := _TVE.Cuenta;
					
					if _Promedio = '0'
					then
						_TotalBancaj := coalesce((  select sum(s.saldofin * m.tc) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = _Ano ),0);
					else
						_TotalBancaj := coalesce((  select ((sum(s.saldoini * m.tc) + sum(s.saldofin * m.tc)) / 2) from tbl_bancos_cuentas_saldos s join tbl_bancos_cuentas c on s.tipo = c.tipo and s.clave = c.clave join tbl_cont_monedas m on c.id_moneda = m.clave where s.tipo = _TipoCons and s.clave = _NumEnt and s.mes = 12 and s.ano = _Ano ),0);
					end if;
					
					INSERT INTO _TMP_BANCAJ_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalBancaj,0);  
					
				end loop;
		
			end if;

			RETURN QUERY
			select Entidad,Total
			from _TMP_BANCAJ_ENT;

		END IF;

	END IF;
	
	DROP TABLE _TMP_BANCAJ;
	DROP TABLE _TMP_BANCAJ_ENT;
	DROP TABLE _TMP_BANCAJ_ENT_MES;
	DROP TABLE _TMP_BANCAJ_ENT_ANO;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_bancaj_saldos_acum(smallint, smallint, smallint, bit, smallint, smallint, bit)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cont_balance_general_orden AS 
 SELECT d.mes, d.ano, r.clave, r.tipo, r.nombre AS rubro, getlevelaccount(d.cuenta) AS nivel, d.cuenta, c.nombre, d.retiros AS debe, d.abonos AS haber
   FROM tbl_cont_catalogo_detalle d
   JOIN tbl_cont_catalogo c ON d.cuenta = c.cuenta
   JOIN tbl_cont_rubros r ON "substring"(d.cuenta::text, 1, 4) >= r.cuentainicial::text AND "substring"(d.cuenta::text, 1, 4) <= r.cuentafinal::text
  WHERE c.acum = B'1'::"bit" AND d.retiros <> 0.0 AND r.tipo = 'IP'::bpchar;

ALTER TABLE view_cont_balance_general_orden
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cont_balance_general_det_orden AS 
 SELECT d.mes, d.ano, r.tipo, getlevelaccount(d.cuenta) AS nivel, c.acum, d.cuenta, c.nombre, d.retiros AS debe, d.abonos AS haber
   FROM tbl_cont_catalogo_detalle d
   JOIN tbl_cont_catalogo c ON d.cuenta = c.cuenta
   JOIN tbl_cont_rubros r ON "substring"(d.cuenta::text, 1, 4) >= r.cuentainicial::text AND "substring"(d.cuenta::text, 1, 4) <= r.cuentafinal::text
  WHERE d.retiros <> 0.0 AND r.tipo = 'IP'::bpchar;

ALTER TABLE view_cont_balance_general_det_orden
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_cont_balance(
    _mes smallint,
    _ano smallint,
    _detallado bit)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_TotalAC numeric(19,4); _TotalAD numeric(19,4); _TotalAF numeric(19,4); _TotalActivo numeric(19,4); _TotalPC numeric(19,4);
		_TotalPL numeric(19,4); _TotalPD numeric(19,4); _TotalPasivo numeric(19,4); _TotalCC numeric(19,4); _ResultadoEA numeric(19,4);
BEGIN

	CREATE LOCAL TEMPORARY TABLE _TMP_BALANCE (
		Part serial NOT NULL ,
		Clave varchar (50) NOT NULL ,
		Concepto varchar (400) NOT NULL ,
		Parcial numeric(19,4) NOT NULL ,
		Total numeric(19,4) NOT NULL ,
		Suma numeric(19,4) NOT NULL ,
		AC char (1) NOT NULL
	);

	_TotalAC := ( select coalesce(sum(Saldo),0) from view_cont_balance_general
				where Tipo = 'AC' and	Mes = _Mes and Ano = _Ano );
	_TotalAF := ( select coalesce(sum(Saldo),0) from view_cont_balance_general
				where Tipo = 'AF' and	Mes = _Mes and Ano = _Ano );
	_TotalAD := ( select coalesce(sum(Saldo),0) from view_cont_balance_general
				where Tipo = 'AD' and	Mes = _Mes and Ano = _Ano );
	_TotalPC := ( select coalesce(sum(Saldo),0) from view_cont_balance_general
				where Tipo = 'PC' and	Mes = _Mes and Ano = _Ano );
	_TotalPL := ( select coalesce(sum(Saldo),0) from view_cont_balance_general
				where Tipo = 'PL' and	Mes = _Mes and Ano = _Ano );
	_TotalPD := ( select coalesce(sum(Saldo),0) from view_cont_balance_general
				where Tipo = 'PD' and	Mes = _Mes and Ano = _Ano );
	_TotalCC := ( select coalesce(sum(Saldo),0) from view_cont_balance_general
				where Tipo = 'CC' and	Mes = _Mes and Ano = _Ano );

	_ResultadoEA = case when (	select coalesce(sum(Resultado),0) from TBL_CONT_RESULTADOS 
																	where Ano < _Ano or (Ano = _Ano and Mes <= _Mes) ) <= 0.0  
													then	abs( (	select coalesce(sum(Resultado),0) from TBL_CONT_RESULTADOS 
																				where Ano < _Ano or (Ano = _Ano and Mes <= _Mes) ) )
													else -((	select coalesce(sum(Resultado),0) from TBL_CONT_RESULTADOS 
																		where Ano < _Ano or (Ano = _Ano and Mes <= _Mes) ) )
													end;

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>ACTIVO_CIRCULANTE</b>',0,0,0,'';
	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------',0,0,coalesce(sum(Saldo),0), '' from view_cont_balance_general
	where Tipo = 'AC' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_balance_general
		where Tipo = 'AC' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0,
						case when Acum = '1' then '+' else ' ' end
		from view_cont_balance_general_det
		where Tipo = 'AC' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes = _Mes and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta; 
	end if;

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>ACTIVO_FIJO</b>',0,0,0,'';
	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------',0,0,coalesce(sum(Saldo),0), '' from view_cont_balance_general
	where Tipo = 'AF' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_balance_general
		where Tipo = 'AF' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0,
						case when Acum = '1' then '+' else ' ' end
		from view_cont_balance_general_det
		where Tipo = 'AF' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes = _Mes and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta; 
	end if;

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0, '';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>ACTIVO_DIFERIDO</b>',0,0,0,'';
	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------',0,0,coalesce(sum(Saldo),0), '' from view_cont_balance_general
	where Tipo = 'AD' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_balance_general
		where Tipo = 'AD' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0,
						case when Acum = '1' then '+' else ' ' end
		from view_cont_balance_general_det
		where Tipo = 'AD' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes = _Mes and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta;
	end if;

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0, '';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '','<b>TOTAL DE ACTIVO:</b>', 0, 0,coalesce((_TotalAC + _TotalAF + _TotalAD),0), '';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	-- PASIVO

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>PASIVO_A_CORTO_PLAZO</b>',0,0,0,'';
	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------',0,0,coalesce(sum(Saldo),0), '' from view_cont_balance_general
	where Tipo = 'PC' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_balance_general
		where Tipo = 'PC' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0,
						case when Acum = '1' then '+' else ' ' end
		from view_cont_balance_general_det
		where Tipo = 'PC' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes = _Mes and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta;
	end if;

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>PASIVO_A_LARGO_PLAZO</b>',0,0,0,'';
	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------',0,0,coalesce(sum(Saldo),0), '' from view_cont_balance_general
	where Tipo = 'PL' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_balance_general
		where Tipo = 'PL' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0,
						case when Acum = '1' then '+' else ' ' end
		from view_cont_balance_general_det
		where Tipo = 'PL' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes = _Mes and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta; 
	end if;

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0, '';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>PASIVO_DIFERIDO</b>',0,0,0,'';
	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------',0,0,coalesce(sum(Saldo),0), '' from view_cont_balance_general
	where Tipo = 'PD' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_balance_general
		where Tipo = 'PD' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0,
						case when Acum = '1' then '+' else ' ' end
		from view_cont_balance_general_det
		where Tipo = 'PD' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes = _Mes and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta; 
	end if;

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0, '';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '','<b>TOTAL DE PASIVO:</b>', 0, 0, coalesce((_TotalPC + _TotalPL + _TotalPD),0), '';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '<b>CUENTA</b>', '<b>CAPITAL_CONTABLE</b>',0,0,0,'';
	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '------', '------------------------------------------------',0,0,coalesce(sum(Saldo),0), '' from view_cont_balance_general
	where Tipo = 'CC' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano;
	if _Detallado = '0'
	then
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select substring(Cuenta,1,4), Nombre, 0, sum(Saldo), 0, '+' from view_cont_balance_general
		where Tipo = 'CC' and Saldo <> 0.0000 and  
		Mes = _Mes and Ano = _Ano
		group by Cuenta, Nombre
		order by Cuenta;
	else
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select case 	when Nivel = 1 then substring(Cuenta,1,4) 
									when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
						case 	when Nivel = 1 then Nombre 
									when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
									when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
									else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
						case when sum(Parcial) = 0.0000 then 0 else sum(Parcial) end, 
						case when sum(Saldo) = 0.0000 then 0 else sum(Saldo) end, 0,
						case when Acum = '1' then '+' else ' ' end
		from view_cont_balance_general_det
		where Tipo = 'CC' and (Saldo <> 0.0000 or Parcial <> 0.0000) and  
		Mes = _Mes and Ano = _Ano
		group by Nivel, Acum, Cuenta, Nombre
		order by Cuenta;
	end if;
	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '','RESULTADO_DE_EJERCICIOS_ANTERIORES', 0,_ResultadoEA,0, '+';
	if _Detallado = '1'
	then
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select '','&nbsp;&nbsp;EJERCICIO ' || cast(Mes as varchar) || ' / ' || cast(Ano as varchar), 
			case when Resultado <= 0 then abs(Resultado) else -(Resultado) end, 0, 0 , ''  
		from TBL_CONT_RESULTADOS
		where Ano < _Ano or (Ano = _Ano and Mes <= _Mes)
		order by Ano desc, Mes desc;
	end if;

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0, '';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '','<b>TOTAL PASIVO MAS CAPITAL:</b>', 0, 0, coalesce((_TotalCC + _ResultadoEA + _TotalPC + _TotalPL + _TotalPD),0), '';

	insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
	select '&nbsp;','',0,0,0,'';

	--/////////////////////////// revisa por cuentas de orden ////////////////////////////////////////////////////////
	if (select count(*) from view_cont_balance_general_orden where Debe <> 0.0000 and Nivel = 1 and Mes = _Mes and Ano = _Ano) > 0
	then
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select '&nbsp;','',0,0,0, '';
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select '&nbsp;','',0,0,0, '';
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select '&nbsp;','',0,0,0, '';
		
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select '<b>CUENTA</b>', '<b>CUENTAS DE ORDEN</b>',0,0,0,'';
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select '------', '------------------------------------------------',0,0,coalesce(sum(Debe),0), '' from view_cont_balance_general_orden
		where Debe <> 0.0000 and Nivel = 1 and 
			Mes = _Mes and Ano = _Ano;
		if _Detallado = '0'
		then
			insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
			select substring(Cuenta,1,4), Nombre, 0, sum(Debe), 0, '+' from view_cont_balance_general_orden
			where Debe <> 0.0000 and Nivel = 1 and 
				Mes = _Mes and Ano = _Ano
			group by Cuenta, Nombre
			order by Cuenta;
		else
			insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
			select case 	when Nivel = 1 then substring(Cuenta,1,4) 
										when Nivel = 2 then '&nbsp;&nbsp;&nbsp;' || substring(Cuenta,5,3) 
										when Nivel = 3 then '&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,8,3) 
										when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,11,3) 
										when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,14,3) 
										else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || substring(Cuenta,17,3) end, 
							case 	when Nivel = 1 then Nombre 
										when Nivel = 2 then '&nbsp;&nbsp;' || Nombre 
										when Nivel = 3 then '&nbsp;&nbsp;&nbsp;' || Nombre 
										when Nivel = 4 then '&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
										when Nivel = 5 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre 
										else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || Nombre end, 
							case when Nivel = 1 then 0 else sum(Debe) end, 
							case when Nivel = 1 then sum(Debe) else 0 end, 0,
							case when Acum = '1' then '+' else ' ' end
			from view_cont_balance_general_det_orden
			where Debe <> 0.0000 and  
				Mes = _Mes and Ano = _Ano
			group by Nivel, Acum, Cuenta, Nombre
			order by Cuenta;
		end if;
		
		insert into _TMP_BALANCE(Clave,Concepto,Parcial,Total,Suma,AC)
		select '&nbsp;','',0,0,0, '';
	end if;
	--/////////////////////////// fin de cuentas de orden /////////////////////////////////////////////////////////////
	RETURN QUERY
	select Clave, Concepto, Parcial, Total, Suma, AC from _TMP_BALANCE
	order by part ASC;

	DROP TABLE _TMP_BALANCE;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_cont_balance(smallint, smallint, bit)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_entidades_gastos AS 
 SELECT c.id_entidadcompra AS clave, c.descripcion, c.status AS especial
   FROM tbl_compras_entidades c
  WHERE c.id_tipoentidad = 2;

ALTER TABLE view_catalog_entidades_gastos
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_entidades_compras AS 
 SELECT c.id_entidadcompra AS clave, c.descripcion, c.status AS especial
   FROM tbl_compras_entidades c
  WHERE c.id_tipoentidad = 0;

ALTER TABLE view_catalog_entidades_compras
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_provee_poriden AS 
 SELECT c.id_clave AS clave, c.nombre AS descripcion, (e.descripcion::text || ':'::text) || c.id_numero AS especial, c.id_entidad
   FROM tbl_provee_provee c
   JOIN tbl_compras_entidades e ON c.id_entidad = e.id_entidadcompra
  WHERE c.status = 'A'::bpchar;

ALTER TABLE view_catalog_provee_poriden
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getcliproid(
    _clpr character,
    _ent integer,
    _num integer)
  RETURNS integer AS
$BODY$
DECLARE
	_resultado integer;
BEGIN
	IF _num = 0
	THEN
		return 0;
	END IF;
	
	IF _clpr = 'CL'
	THEN
		_resultado := (select id_clave from tbl_client_client where id_tipo = 'CL' and id_numero = _num and id_entidad = _ent);
	ELSE -- _clpr = 'PR'
		_resultado := (select id_clave from tbl_provee_provee where id_tipo = 'PR' and id_numero = _num and id_entidad = _ent);
	END IF;

	IF _resultado is null
	THEN
		return -1;
	ELSE
		return _resultado;
	END IF;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getcliproid(character, integer, integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_comp_fact_acum(
    _mes smallint,
    _ano smallint,
    _ultimos smallint,
    _noacumulados bit,
    _entcons smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_cont smallint; _TotalEnt smallint; _NumEnt smallint; _NombreEnt varchar(15);
	_TotalCompras numeric(19,4); _UltimoAno numeric(19,4); _PenultimoAno numeric(19,4); _AnteriorAno numeric(19,4);
	_ENE numeric(19,4); _FEB numeric(19,4); _MAR numeric(19,4); _ABR numeric(19,4); _MAY numeric(19,4); _JUN numeric(19,4); 
	_JUL numeric(19,4); _AGO numeric(19,4); _SEP numeric(19,4); _OCT numeric(19,4);	_NOV numeric(19,4); _DIC numeric(19,4);
	_TVE RECORD;
BEGIN

	

CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS_ENT (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS (
		part serial NOT NULL ,
		Periodo varchar (20)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	);
	

CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS_ENT_ANO (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Ultimo numeric(19,4) NOT NULL ,
		Penultimo numeric(19,4) NOT NULL ,
		Anterior numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS_ENT_MES (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		ENE numeric(19,4) NOT NULL ,
		FEB numeric(19,4) NOT NULL ,
		MAR numeric(19,4) NOT NULL ,
		ABR numeric(19,4) NOT NULL ,
		MAY numeric(19,4) NOT NULL ,
		JUN numeric(19,4) NOT NULL ,
		JUL numeric(19,4) NOT NULL ,
		AGO numeric(19,4) NOT NULL ,
		SEP numeric(19,4) NOT NULL ,
		OCT numeric(19,4) NOT NULL ,
		NOV numeric(19,4) NOT NULL ,
		DIC numeric(19,4) NOT NULL 
	); 

	IF(_NoAcumulados = '0') -- es acumulados generales
	THEN
		IF(_EntCons = -1) -- Se trata del acumulado de entidades generales
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					_TotalCompras := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_FACTURAS_CAB
																			where Status <> 'C' and date_part('Month',Fecha) = _cont and date_part('Year',Fecha) = _Ano ),0);
					INSERT INTO _TMP_COMPRAS (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalCompras,0);
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual

				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					_TotalCompras := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_FACTURAS_CAB
																			where Status <> 'C' and date_part('Year',Fecha) = _cont ),0);
					
					INSERT INTO _TMP_COMPRAS (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalCompras,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		
		ELSE -- se trata de una entidad en especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					_TotalCompras := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_FACTURAS_CAB
																			where ID_Entidad = _EntCons and Status <> 'C' and date_part('Month',Fecha) = _cont and date_part('Year',Fecha) = _Ano ),0);
					INSERT INTO _TMP_COMPRAS (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalCompras,0); 
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual
				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					_TotalCompras := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_FACTURAS_CAB
																			where ID_Entidad = _EntCons and Status <> 'C' and date_part('Year',Fecha) = _cont ),0);
					
					INSERT INTO _TMP_COMPRAS (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalCompras,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		END IF;

		RETURN QUERY
		select Periodo, Total
		from _TMP_COMPRAS
		order by part ASC;
	
	ELSE -- Se maneja por detalles de entidad
	
		IF(_EntCons = -1) -- Se trata de comparativos todas las entidades en todos los meses o a?os
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 0
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;
		
					_ENE := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 1 and date_part('Year',Fecha) = _Ano ), 0);
					_FEB := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 2 and date_part('Year',Fecha) = _Ano ), 0);
					_MAR := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 3 and date_part('Year',Fecha) = _Ano ), 0);
					_ABR := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 4 and date_part('Year',Fecha) = _Ano ), 0);
					_MAY := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 5 and date_part('Year',Fecha) = _Ano ), 0);
					_JUN := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 6 and date_part('Year',Fecha) = _Ano ), 0);
					_JUL := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 7 and date_part('Year',Fecha) = _Ano ), 0);
					_AGO := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 8 and date_part('Year',Fecha) = _Ano ), 0);
					_SEP := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 9 and date_part('Year',Fecha) = _Ano ), 0);
					_OCT := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 10 and date_part('Year',Fecha) = _Ano ), 0);
					_NOV := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 11 and date_part('Year',Fecha) = _Ano ), 0);
					_DIC := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_FACTURAS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 12 and date_part('Year',Fecha) = _Ano ), 0);
		
					INSERT INTO _TMP_COMPRAS_ENT_MES (Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC)
					SELECT _NombreEnt, round(_ENE,0), round(_FEB,0), round(_MAR,0),  round(_ABR,0),  round(_MAY,0),  round(_JUN,0),  
						round(_JUL,0),  round(_AGO,0),  round(_SEP,0), round(_OCT,0),  round(_NOV,0),  round(_DIC,0);
									
				end loop;

				RETURN QUERY
				select Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC
				from _TMP_COMPRAS_ENT_MES;
		
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 0
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;
		
					_UltimoAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_FACTURAS_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = _Ano ),0);
					_PenultimoAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_FACTURAS_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = (_Ano-1) ),0);
					_AnteriorAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_FACTURAS_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = (_Ano-2) ),0);
		
					INSERT INTO _TMP_COMPRAS_ENT_ANO (Entidad,Ultimo,Penultimo,Anterior)
					SELECT _NombreEnt, round(_UltimoAno,0), round(_PenultimoAno,0), round(_AnteriorAno,0);

				end loop;
				
				RETURN QUERY
				select Entidad,Ultimo,Penultimo,Anterior
				from _TMP_COMPRAS_ENT_ANO;
		
			end if;
		
		ELSE -- se trata de comparativos de todas las entidades pero en un mes o ano especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 0
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;
		
					_TotalCompras := coalesce( (	select sum(SubTotal * TC)
																				from TBL_COMPRAS_FACTURAS_CAB
																				where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = _Mes and date_part('Year',Fecha) = _Ano ), 0);
		
					INSERT INTO _TMP_COMPRAS_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalCompras,0);   
									
				end loop;
				
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 0
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;
					
					_TotalCompras := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_FACTURAS_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = _Ano ),0);
		
					INSERT INTO _TMP_COMPRAS_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalCompras,0);  
					
				end loop;
		
			end if;

			RETURN QUERY
			select Entidad,Total
			from _TMP_COMPRAS_ENT;

		END IF;

	END IF;
	
	DROP TABLE _TMP_COMPRAS;
	DROP TABLE _TMP_COMPRAS_ENT;
	DROP TABLE _TMP_COMPRAS_ENT_MES;
	DROP TABLE _TMP_COMPRAS_ENT_ANO;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_comp_fact_acum(smallint, smallint, smallint, bit, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_entidades_compgas AS 
 SELECT c.id_entidadcompra AS clave, c.descripcion, c.status AS especial
   FROM tbl_compras_entidades c;

ALTER TABLE view_catalog_entidades_compgas
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_clipro_estado_cuenta(
    _clipro character varying,
    _id_clipro integer,
    _entidad smallint,
    _orden character varying,
    _direccion character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE
	 _CUR_CP refcursor;  _CUR_CXCP refcursor;  _CUR_PAG refcursor;
	_CP RECORD; _CXCP RECORD; _PAG RECORD;
	_TBL_CLIPRO varchar(50); _TBL_CLIPROSL varchar(50); _TBL_CLIPRO_CXPC varchar(50); _TBL_CLIPRO_CXPC_CON varchar(50); _TBL_CLIPROSLMON varchar(50); 
	_TBL_VENCOMP_ENT varchar(50); _id_entidadvencomp varchar(50);	_AND_ID_CLIPRO varchar(50); _AND_ID_ENTIDAD varchar(50);
BEGIN
	IF _clipro = 'PR'
	THEN
		_TBL_CLIPRO := 'TBL_PROVEE_PROVEE';
		_TBL_CLIPROSL := 'TBL_PROVEE_SALDOS_MONEDAS';
		_TBL_CLIPRO_CXPC := 'TBL_PROVEE_CXP';
		_TBL_CLIPRO_CXPC_CON := 'TBL_PROVEE_CXP_CONCEPTOS';
		_TBL_CLIPROSLMON := 'tbl_provee_saldos_monedas';
		_TBL_VENCOMP_ENT := 'tbl_compras_entidades';
		_id_entidadvencomp := 'ID_EntidadCompra';
	ELSE
		_TBL_CLIPRO := 'TBL_CLIENT_CLIENT';
		_TBL_CLIPROSL := 'TBL_CLIENT_SALDOS_MONEDAS';
		_TBL_CLIPRO_CXPC := 'TBL_CLIENT_CXC';
		_TBL_CLIPRO_CXPC_CON := 'TBL_CLIENT_CXC_CONCEPTOS';
		_TBL_CLIPROSLMON := 'tbl_client_saldos_monedas';
		_TBL_VENCOMP_ENT := 'tbl_ventas_entidades';
		_id_entidadvencomp := 'ID_EntidadVenta';
	END IF;
	
	CREATE LOCAL TEMPORARY TABLE _TMP_CLIPRO_ESTADO_CUENTA (
		Part serial NOT NULL ,
		Clave varchar (50) NOT NULL ,
		Concepto varchar (400) NOT NULL ,
		Total varchar(100) NOT NULL ,
		Saldo varchar(100) NOT NULL
	);

	IF _id_clipro = -1
	THEN
		_AND_ID_CLIPRO := ''; --No aplica a ningun cliente o proveedor, por lo tanto no candiciona a nada... tomará a todos los clientes o proveedores
	ELSE
		_AND_ID_CLIPRO := 'and c.ID_Clave = ' || quote_literal(_id_clipro) ; 
	END IF;
	
	IF _entidad = -1 --Todas las entidades
	THEN
		_AND_ID_ENTIDAD := ''; --No aplica a ningun cliente o proveedor, por lo tanto no candiciona a nada... tomará a todos los clientes o proveedores
	ELSIF _entidad = -2 --Solo entidades contables
	THEN
		_AND_ID_ENTIDAD := 'AND ent.Fija = ''0''';
	ELSIF _entidad = -3 --Solo entidades fijas
	THEN
		_AND_ID_ENTIDAD := 'AND ent.Fija = ''1''';
	ELSE --La entidad seleccionada
		_AND_ID_ENTIDAD := 'AND c.ID_Entidad = ' || quote_literal(_entidad) ; 
	END IF;

	OPEN _CUR_CP FOR EXECUTE 'SELECT c.id_clave, ent.Descripcion, c.id_numero, c.nombre, getcliproslds(c.id_tipo, c.id_clave) AS saldo
						FROM ' || _TBL_CLIPRO || ' c INNER JOIN ' || _TBL_VENCOMP_ENT || ' ent 
							ON c.ID_Entidad = ent.' || _id_entidadvencomp || '
						WHERE  ( select sum(s.saldo) from ' || _TBL_CLIPROSL || ' s where s.id_tipo = c.id_tipo and s.id_clave = c.id_clave ) <> 0 ' ||
						_AND_ID_ENTIDAD || ' ' || _AND_ID_CLIPRO || '
						ORDER BY ' || _Orden || ' ' || _Direccion;
	LOOP
		FETCH NEXT FROM _CUR_CP INTO _CP;
		EXIT WHEN _CP IS NULL;

		INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
		VALUES(  '<b>' || (case when (_entidad = -1 or _entidad = -2 or _entidad = -3) then _CP.Descripcion || ':' else '' end) || ' ' || _CP.ID_Numero || '</b>', '<b>' || _CP.Nombre || '</b>', '', '<b>' || _CP.Saldo || '</b>');
		
		OPEN _CUR_CXCP FOR EXECUTE 'SELECT c.ID_CP, c.ID_TipoCP, c.ID_Aplicacion, c.Fecha,  m.Simbolo,
							CASE
							    WHEN con.desistema = B''1''::"bit" THEN ( SELECT m.msj1
									FROM tbl_msj m
								      WHERE m.alc::text = ''CEF''::text AND m.mod::text = ''CXPC_ENLACES''::text AND m.sub::text = ''CATALOGO''::text AND m.elm::text = con.id_concepto::text)
								    ELSE con.descripcion
								END AS Descripcion, c.Concepto, c.Vencimiento,  
									date_part(''day'', NOW() - c.Vencimiento) as Retraso,   
								CASE WHEN c.ID_TipoCP = ''ANT'' then -c.Total ELSE c.Total END AS Total, 
								CASE WHEN c.ID_TipoCP = ''ANT'' then -c.Saldo ELSE c.Saldo END AS Saldo
							FROM ' || _TBL_CLIPRO_CXPC || ' c inner join ' || _TBL_CLIPRO_CXPC_CON || ' con on   
								c.ID_Concepto = con.ID_Concepto  inner join tbl_cont_monedas m on
								m.clave = c.moneda
							WHERE (c.ID_TipoCP = ''ALT'' or c.ID_TipoCP = ''ANT'') and c.Status <> ''C'' and c.Saldo <> 0 and  c.ID_TipoCliPro = ''' || _clipro || ''' and c.ID_ClaveCliPro = ' || _CP.ID_Clave || '
							ORDER BY c.Fecha DESC, c.ID_CP DESC';
		LOOP
			FETCH NEXT FROM _CUR_CXCP INTO _CXCP;
			EXIT WHEN _CXCP IS NULL;
		
			INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
			SELECT to_char(_CXCP.Fecha, 'DD/MM/YYYY'), _CXCP.Descripcion || ' / ' || _CXCP.Concepto || 
				CASE WHEN _CXCP.ID_TipoCP = 'ALT' THEN ' Vence: ' || to_char(_CXCP.Vencimiento, 'DD/MM/YYYY') || ' (' || _CXCP.Retraso || ' Dias)' ELSE ' ' END, _CXCP.Simbolo || ' ' || cast(round(_CXCP.Total,2) as varchar), _CXCP.Simbolo || ' ' || cast(round(_CXCP.Saldo,2) as varchar);
				OPEN _CUR_PAG FOR EXECUTE 'SELECT c.ID_CP, c.ID_Aplicacion, c.ID_TipoCP, c.Fecha,  m.Simbolo,
								CASE
								    WHEN con.desistema = B''1''::"bit" THEN ( SELECT m.msj1
								       FROM tbl_msj m
								      WHERE m.alc::text = ''CEF''::text AND m.mod::text = ''CXPC_ENLACES''::text AND m.sub::text = ''CATALOGO''::text AND m.elm::text = con.id_concepto::text)
								    ELSE con.descripcion
								END AS Descripcion, c.Concepto,    
								CASE WHEN c.ID_TipoCP = ''PAG'' or c.ID_TipoCP = ''SAL'' or c.ID_TipoCP = ''APL'' then -c.Total ELSE c.Total END AS Total  
							FROM ' || _TBL_CLIPRO_CXPC || ' c inner join ' || _TBL_CLIPRO_CXPC_CON || ' con on   
								c.ID_Concepto = con.ID_Concepto  inner join tbl_cont_monedas m on
								m.clave = c.moneda
							WHERE c.ID_Aplicacion = ' || _CXCP.ID_CP || ' and c.Status <> ''C'' and c.ID_TipoCP <> ''' || _CXCP.ID_TipoCP || '''
							ORDER BY c.Fecha DESC, c.ID_CP DESC';
			LOOP
				FETCH NEXT FROM _CUR_PAG INTO _PAG;
				EXIT WHEN _PAG IS NULL;
		
				INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
				SELECT '', '&nbsp;&nbsp;&nbsp;&nbsp;' || to_char(_PAG.Fecha, 'DD/MM/YYYY') || ' ' || _PAG.Descripcion || ' / ' || _PAG.Concepto, _PAG.Simbolo || ' ' || cast(round(_PAG.Total,2) as varchar), '';
			END LOOP;
			CLOSE _CUR_PAG;
		END LOOP;
		CLOSE _CUR_CXCP;
		INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
		VALUES('&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;');
	END LOOP;
	
	CLOSE _CUR_CP;

	INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
	VALUES('&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;');
	INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
	VALUES('', '', '', '<b>SALDOS TOTALES</b>');
	INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
	VALUES('&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;');

	IF _id_clipro = -1
	THEN
		_AND_ID_CLIPRO := ''; --No aplica a ningun cliente o proveedor, por lo tanto no candiciona a nada... tomará a todos los clientes o proveedores
	ELSE
		_AND_ID_CLIPRO := 'and s.id_clave = ' || quote_literal(_id_clipro) ; 
	END IF;
	IF _entidad = -1
	THEN
		_AND_ID_ENTIDAD := ''; --No aplica a ningun cliente o proveedor, por lo tanto no candiciona a nada... tomará a todos los clientes o proveedores
	ELSIF _entidad = -2 --Solo entidades contables
	THEN
		_AND_ID_ENTIDAD := 'and ent.fija =''0''';
	ELSIF _entidad = -3 --Solo entidades fijas
	THEN
		_AND_ID_ENTIDAD := 'and ent.fija = ''1''';
	ELSE --La entidad seleccionada
		_AND_ID_ENTIDAD := 'and cat.id_entidad = ' || quote_literal(_entidad) ; 
	END IF;

	OPEN _CUR_CP FOR EXECUTE 'select ent.descripcion, m.clave, m.simbolo, m.tc, coalesce(sum(s.saldo),0) as saldo 
									from tbl_cont_monedas m join ' || _TBL_CLIPROSLMON || ' s 
										on m.clave = s.id_moneda join ' || _TBL_CLIPRO || ' cat 
										on s.id_clave = cat.id_clave join ' || _TBL_VENCOMP_ENT || ' ent 
										on cat.ID_Entidad = ent.' || _id_entidadvencomp || '
									where s.saldo <> 0.00 and s.id_tipo = ''' || _clipro || ''' ' || _AND_ID_ENTIDAD || ' ' || _AND_ID_CLIPRO || '
									group by cat.id_entidad, ent.descripcion, m.clave, m.simbolo, m.tc
									order by cat.id_entidad asc, m.clave asc';
	LOOP
		FETCH NEXT FROM _CUR_CP INTO _CP;
		EXIT WHEN _CP IS NULL;
		
		INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
		VALUES((case when (_entidad = -1 or _entidad = -2 or _entidad = -3) then '<b>' || _CP.Descripcion || '</b>' else '' end), '', 
					'<b>' || _CP.Simbolo || ' ' || cast(round(_CP.Saldo,2) as varchar) || '</b>','<b>' || (case when _CP.Clave <> 1 then ' $ ' || cast(round(_CP.TC,2) as varchar) else '' end) || '&nbsp;&nbsp;&nbsp;$ ' || cast(round((_CP.Saldo * _CP.TC),2) as varchar) || '</b>' );
	END LOOP;

	CLOSE _CUR_CP;

	INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
	VALUES('&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;');

	OPEN _CUR_CP FOR EXECUTE 'select coalesce(sum(s.saldo * m.tc),0) as saldo 
									from tbl_cont_monedas m join ' || _TBL_CLIPROSLMON || ' s 
										on m.clave = s.id_moneda join ' || _TBL_CLIPRO || ' cat 
										on s.id_clave = cat.id_clave join ' || _TBL_VENCOMP_ENT || ' ent 
										on cat.ID_Entidad = ent.' || _id_entidadvencomp || '
									where s.saldo <> 0.00 and s.id_tipo = ''' || _clipro || ''' ' || _AND_ID_ENTIDAD || ' ' || _AND_ID_CLIPRO;
	LOOP
		FETCH NEXT FROM _CUR_CP INTO _CP;
		EXIT WHEN _CP IS NULL;
		
		INSERT INTO _TMP_CLIPRO_ESTADO_CUENTA (Clave,Concepto,Total,Saldo)
		VALUES('&nbsp;', '&nbsp;', '&nbsp;', '<b>$ ' || cast(round(_CP.Saldo,2) as varchar) || '</b>');
	END LOOP;
		
	RETURN QUERY
	select Clave, Concepto, Total, Saldo
	from _TMP_CLIPRO_ESTADO_CUENTA
	order by part ASC;
	
	
	DROP TABLE _TMP_CLIPRO_ESTADO_CUENTA;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_clipro_estado_cuenta(character varying, integer, smallint, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_clipro_antiguedad_saldos(
    _periodo character,
    _detallado boolean,
    _clipro character varying,
    _id_clipro integer,
    _entidad smallint,
    _orden character varying,
    _direccion character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE
	 _CUR_CP refcursor;  _CUR_CXCP refcursor;  _CUR_PAG refcursor;
	_CP RECORD; _CXCP RECORD; _PAG RECORD;
	_TBL_CLIPRO varchar(50); _TBL_CLIPROSL varchar(50); _TBL_CLIPRO_CXPC varchar(50); _TBL_CLIPRO_CXPC_CON varchar(50); _TBL_CLIPROSLMON varchar(50); 
	_TBL_VENCOMP_ENT varchar(50); _id_entidadvencomp varchar(50);	_AND_ID_CLIPRO varchar(50); _AND_ID_ENTIDAD varchar(50);
	_DESDE1 varchar(3); _HASTA1 varchar(3); _DESDE2 varchar(3); _HASTA2 varchar(3); _DESDE3 varchar(3); _HASTA3 varchar(3);
BEGIN
	IF _periodo = 'SEM'
	THEN
		_DESDE1 := '1';
		_HASTA1 := '7';
		_DESDE2 := '8';
		_HASTA2 := '14';
		_DESDE3 := '15';
		_HASTA3 := '21';
	ELSIF _periodo = 'QUI'
	THEN
		_DESDE1 := '1';
		_HASTA1 := '15';
		_DESDE2 := '16';
		_HASTA2 := '30';
		_DESDE3 := '31';
		_HASTA3 := '45';
	ELSIF _periodo = 'MEN'
	THEN
		_DESDE1 := '1';
		_HASTA1 := '30';
		_DESDE2 := '31';
		_HASTA2 := '60';
		_DESDE3 := '61';
		_HASTA3 := '90';
	ELSIF _periodo = 'BIM'
	THEN
		_DESDE1 := '1';
		_HASTA1 := '60';
		_DESDE2 := '61';
		_HASTA2 := '120';
		_DESDE3 := '121';
		_HASTA3 := '180';
	ELSIF _periodo = 'TRI'
	THEN
		_DESDE1 := '1';
		_HASTA1 := '90';
		_DESDE2 := '91';
		_HASTA2 := '180';
		_DESDE3 := '181';
		_HASTA3 := '270';
	ELSE -- 'SEM' semestral
		_DESDE1 := '1';
		_HASTA1 := '182';
		_DESDE2 := '183';
		_HASTA2 := '365';
		_DESDE3 := '366';
		_HASTA3 := '548';
	END IF;	
	
	IF _clipro = 'PR'
	THEN
		_TBL_CLIPRO := 'TBL_PROVEE_PROVEE';
		_TBL_CLIPROSL := 'TBL_PROVEE_SALDOS_MONEDAS';
		_TBL_CLIPRO_CXPC := 'TBL_PROVEE_CXP';
		_TBL_CLIPRO_CXPC_CON := 'TBL_PROVEE_CXP_CONCEPTOS';
		_TBL_CLIPROSLMON := 'tbl_provee_saldos_monedas';
		_TBL_VENCOMP_ENT := 'tbl_compras_entidades';
		_id_entidadvencomp := 'ID_EntidadCompra';
	ELSE
		_TBL_CLIPRO := 'TBL_CLIENT_CLIENT';
		_TBL_CLIPROSL := 'TBL_CLIENT_SALDOS_MONEDAS';
		_TBL_CLIPRO_CXPC := 'TBL_CLIENT_CXC';
		_TBL_CLIPRO_CXPC_CON := 'TBL_CLIENT_CXC_CONCEPTOS';
		_TBL_CLIPROSLMON := 'tbl_client_saldos_monedas';
		_TBL_VENCOMP_ENT := 'tbl_ventas_entidades';
		_id_entidadvencomp := 'ID_EntidadVenta';
	END IF;
	
	CREATE LOCAL TEMPORARY TABLE _TMP_CLIPRO_ANTIGUEDAD_SALDOS (
		Part serial NOT NULL ,
		Clave varchar (50) NOT NULL ,
		Nombre varchar (400) NOT NULL ,
		Saldo varchar(100) NOT NULL,
		NV varchar(50) NOT NULL,
		V1 varchar(50) NOT NULL,
		V2 varchar(50) NOT NULL,
		V3 varchar(50) NOT NULL,
		V4 varchar(50) NOT NULL
	);

	CREATE LOCAL TEMPORARY TABLE _TMP_PRE_ACUM (
		ID_Entidad smallint NOT NULL ,
		Fija bit NOT NULL,
		ID_Clave integer NOT NULL ,
		ID_Moneda smallint NOT NULL,
		Saldo numeric(19,4) NOT NULL,
		NV numeric(19,4) NOT NULL,
		V1 numeric(19,4) NOT NULL,
		V2 numeric(19,4) NOT NULL,
		V3 numeric(19,4) NOT NULL,
		V4 numeric(19,4) NOT NULL
	);
	
	IF _id_clipro = -1
	THEN
		_AND_ID_CLIPRO := ''; --No aplica a ningun cliente o proveedor, por lo tanto no candiciona a nada... tomará a todos los clientes o proveedores
	ELSE
		_AND_ID_CLIPRO := 'and pr.ID_Clave = ' || quote_literal(_id_clipro) ; 
	END IF;
	
	IF _entidad = -1 --Todas las entidades
	THEN
		_AND_ID_ENTIDAD := ''; --No aplica a ningun cliente o proveedor, por lo tanto no candiciona a nada... tomará a todos los clientes o proveedores
	ELSIF _entidad = -2 --Solo entidades contables
	THEN
		_AND_ID_ENTIDAD := 'AND ent.Fija = ''0''';
	ELSIF _entidad = -3 --Solo entidades fijas
	THEN
		_AND_ID_ENTIDAD := 'AND ent.Fija = ''1''';
	ELSE --La entidad seleccionada
		_AND_ID_ENTIDAD := 'AND c.ID_Entidad = ' || quote_literal(_entidad) ; 
	END IF;

	INSERT INTO _TMP_CLIPRO_ANTIGUEDAD_SALDOS (Clave, Nombre, Saldo, NV, V1, V2, V3, V4) 
	VALUES(  '&nbsp', '&nbsp;','Total','Sin retraso',_DESDE1 || ' a ' || _HASTA1, _DESDE2 || ' a ' || _HASTA2, _DESDE3 || ' a ' || _HASTA3,'Mas de ' || _HASTA3 ); 
		
	OPEN _CUR_CP FOR EXECUTE 'SELECT pr.ID_Clave, ent.' || _id_entidadvencomp || ' as ID_Entidad, ent.Fija, ent.Descripcion, pr.ID_Numero, pr.Nombre, m.Clave as ID_Moneda, m.Simbolo, 
										sum( (case when  c.ID_TipoCP = ''ALT'' then c.Saldo else -c.Saldo end)) as Saldo,
								      (select sum(case when  ic.ID_TipoCP = ''ALT'' then ic.Saldo else -ic.Saldo end)
									from ' || _TBL_CLIPRO_CXPC || ' ic inner join tbl_cont_monedas im on
										im.clave = ic.moneda inner join ' || _TBL_CLIPRO || ' ipr on
										ipr.ID_Clave = ic.ID_ClaveCliPro
									where  (ic.ID_TipoCP = ''ALT'' OR ic.ID_TipoCP = ''ANT'') and ic.Status <> ''C'' and ic.Saldo <> 0 and ic.ID_TipoCliPro = ''' || _clipro || ''' and im.clave = m.clave and ipr.ID_Clave = pr.ID_Clave
										and date_part(''day'', NOW() - ic.Vencimiento) <= 0 ) as NV,
									 (select sum(case when  ic.ID_TipoCP = ''ALT'' then ic.Saldo else -ic.Saldo end)
									from ' || _TBL_CLIPRO_CXPC || ' ic inner join tbl_cont_monedas im on
										im.clave = ic.moneda inner join ' || _TBL_CLIPRO || ' ipr on
										ipr.ID_Clave = ic.ID_ClaveCliPro
									where  (ic.ID_TipoCP = ''ALT'' OR ic.ID_TipoCP = ''ANT'') and ic.Status <> ''C'' and ic.Saldo <> 0 and ic.ID_TipoCliPro = ''' || _clipro || ''' and im.clave = m.clave and ipr.ID_Clave = pr.ID_Clave
										and date_part(''day'', NOW() - ic.Vencimiento) between ' || _DESDE1 || ' and ' || _HASTA1 || ') as V1,
									 (select sum(case when  ic.ID_TipoCP = ''ALT'' then ic.Saldo else -ic.Saldo end)
									from ' || _TBL_CLIPRO_CXPC || ' ic inner join tbl_cont_monedas im on
										im.clave = ic.moneda inner join ' || _TBL_CLIPRO || ' ipr on
										ipr.ID_Clave = ic.ID_ClaveCliPro
									where  (ic.ID_TipoCP = ''ALT'' OR ic.ID_TipoCP = ''ANT'') and ic.Status <> ''C'' and ic.Saldo <> 0 and ic.ID_TipoCliPro = ''' || _clipro || ''' and im.clave = m.clave and ipr.ID_Clave = pr.ID_Clave
										and date_part(''day'', NOW() - ic.Vencimiento) between ' || _DESDE2 || ' and ' || _HASTA2 || ') as V2,
									 (select sum(case when  ic.ID_TipoCP = ''ALT'' then ic.Saldo else -ic.Saldo end)
									from ' || _TBL_CLIPRO_CXPC || ' ic inner join tbl_cont_monedas im on
										im.clave = ic.moneda inner join ' || _TBL_CLIPRO || ' ipr on
										ipr.ID_Clave = ic.ID_ClaveCliPro
									where  (ic.ID_TipoCP = ''ALT'' OR ic.ID_TipoCP = ''ANT'') and ic.Status <> ''C'' and ic.Saldo <> 0 and ic.ID_TipoCliPro = ''' || _clipro || ''' and im.clave = m.clave and ipr.ID_Clave = pr.ID_Clave
										and date_part(''day'', NOW() - ic.Vencimiento) between ' || _DESDE3 || ' and ' || _HASTA3 || ') as V3,
									 (select sum(case when  ic.ID_TipoCP = ''ALT'' then ic.Saldo else -ic.Saldo end)
									from ' || _TBL_CLIPRO_CXPC || ' ic inner join tbl_cont_monedas im on
										im.clave = ic.moneda inner join ' || _TBL_CLIPRO || ' ipr on
										ipr.ID_Clave = ic.ID_ClaveCliPro
									where  (ic.ID_TipoCP = ''ALT'' OR ic.ID_TipoCP = ''ANT'') and ic.Status <> ''C'' and ic.Saldo <> 0 and ic.ID_TipoCliPro = ''' || _clipro || ''' and im.clave = m.clave and ipr.ID_Clave = pr.ID_Clave
										and date_part(''day'', NOW() - ic.Vencimiento) > ' || _HASTA3 || ') as V4
								FROM ' || _TBL_CLIPRO_CXPC || ' c inner join tbl_cont_monedas m on
										m.clave = c.moneda inner join ' || _TBL_CLIPRO || ' pr on
										pr.ID_Clave = c.ID_ClaveCliPro INNER JOIN ' || _TBL_VENCOMP_ENT || ' ent on 
										c.ID_Entidad = ent.' || _id_entidadvencomp || '
								WHERE (c.ID_TipoCP = ''ALT'' OR c.ID_TipoCP = ''ANT'') and c.Status <> ''C'' and c.Saldo <> 0 and  c.ID_TipoCliPro = ''' || _clipro || ''' ' ||
									/*and c.ID_Entidad = 1  and pr.ID_Clave = 30*/
									_AND_ID_ENTIDAD || ' ' || _AND_ID_CLIPRO || '
								GROUP BY pr.ID_Clave, ent.' || _id_entidadvencomp || ', ent.Fija, ent.Descripcion, pr.ID_Numero, pr.Nombre, m.Clave, m.Simbolo
								ORDER BY ' || _Orden || ' ' || _Direccion;
	LOOP
		FETCH NEXT FROM _CUR_CP INTO _CP;
		EXIT WHEN _CP IS NULL;

		INSERT INTO _TMP_PRE_ACUM (ID_Entidad, Fija, ID_Clave, ID_Moneda, Saldo, NV, V1, V2, V3, V4)
		VALUES(  _CP.ID_Entidad, _CP.Fija, _CP.ID_Clave, _CP.ID_Moneda, coalesce(_CP.Saldo,0), coalesce(_CP.NV,0), coalesce(_CP.V1,0),  coalesce(_CP.V2,0),  coalesce(_CP.V3,0),  coalesce(_CP.V4,0) );
		
		INSERT INTO _TMP_CLIPRO_ANTIGUEDAD_SALDOS (Clave, Nombre, Saldo, NV, V1, V2, V3, V4) 
		VALUES(  '<b>' || (case when (_entidad = -1 or _entidad = -2 or _entidad = -3) then _CP.Descripcion || ':' else '' end) || ' ' || _CP.ID_Numero || '</b>', '<b>' || _CP.Nombre || '</b>', 
					(case when _CP.Saldo is not null then '<b>' || _CP.Simbolo || ' ' || cast(round(_CP.Saldo,2) as varchar) || '</b>' else '&nbsp;' end),
					(case when _CP.NV is not null then '<b>' || _CP.Simbolo || ' ' || cast(round(_CP.NV,2) as varchar) || '</b>' else '&nbsp;' end),
					(case when _CP.V1 is not null then '<b>' || _CP.Simbolo || ' ' || cast(round(_CP.V1,2) as varchar) || '</b>' else '&nbsp;' end),
					(case when _CP.V2 is not null then '<b>' || _CP.Simbolo || ' ' || cast(round(_CP.V2,2) as varchar) || '</b>' else '&nbsp;' end),
					(case when _CP.V3 is not null then '<b>' || _CP.Simbolo || ' ' || cast(round(_CP.V3,2) as varchar) || '</b>' else '&nbsp;' end),
					(case when _CP.V4 is not null then '<b>' || _CP.Simbolo || ' ' || cast(round(_CP.V4,2) as varchar) || '</b>' else '&nbsp;' end) );

		IF _Detallado = true
		THEN			
			OPEN _CUR_CXCP FOR EXECUTE 
					'SELECT c.ID_CP, c.ID_TipoCP, c.ID_Aplicacion, c.Fecha,  m.Simbolo,
							CASE
							    WHEN con.desistema = B''1''::"bit" THEN ( SELECT m.msj1
									FROM tbl_msj m
								      WHERE m.alc::text = ''CEF''::text AND m.mod::text = ''CXPC_ENLACES''::text AND m.sub::text = ''CATALOGO''::text AND m.elm::text = con.id_concepto::text)
								    ELSE con.descripcion
								END AS Descripcion, c.Concepto, c.Vencimiento,  
									date_part(''day'', NOW() - c.Vencimiento) as Retraso,   
								CASE WHEN c.ID_TipoCP = ''ANT'' then -c.Saldo ELSE c.Saldo END AS Saldo
							FROM ' || _TBL_CLIPRO_CXPC || ' c inner join ' || _TBL_CLIPRO_CXPC_CON || ' con on   
								c.ID_Concepto = con.ID_Concepto  inner join tbl_cont_monedas m on
								m.clave = c.moneda
							WHERE (c.ID_TipoCP = ''ALT'' or c.ID_TipoCP = ''ANT'') and c.Status <> ''C'' and c.Saldo <> 0 and  c.ID_TipoCliPro = ''' || _clipro || ''' and c.ID_ClaveCliPro = ' || _CP.ID_Clave || ' and c.moneda = ' || _CP.ID_Moneda || '
							ORDER BY c.Fecha DESC, c.ID_CP DESC';
			LOOP
				FETCH NEXT FROM _CUR_CXCP INTO _CXCP;
				EXIT WHEN _CXCP IS NULL;
		
				INSERT INTO _TMP_CLIPRO_ANTIGUEDAD_SALDOS (Clave, Nombre, Saldo, NV, V1, V2, V3, V4) 
				SELECT to_char(_CXCP.Fecha, 'DD/MM/YYYY'), _CXCP.Descripcion || ' / ' || _CXCP.Concepto || CASE WHEN _CXCP.ID_TipoCP = 'ALT' THEN ' Vence: ' || to_char(_CXCP.Vencimiento, 'DD/MM/YYYY') || ' (' || _CXCP.Retraso || ' Dias)' ELSE ' ' END, 
					_CXCP.Simbolo || ' ' || cast(round(_CXCP.Saldo,2) as varchar),
					case when _CXCP.Retraso <= 0 then (_CXCP.Simbolo || ' ' || cast(round(_CXCP.Saldo,2) as varchar)) else '&nbsp;' end,
					case when _CXCP.Retraso between cast(_DESDE1 as integer) and  cast(_HASTA1 as integer) then (_CXCP.Simbolo || ' ' || cast(round(_CXCP.Saldo,2) as varchar)) else '&nbsp;' end,
					case when _CXCP.Retraso between cast(_DESDE2 as integer) and  cast(_HASTA2 as integer) then (_CXCP.Simbolo || ' ' || cast(round(_CXCP.Saldo,2) as varchar)) else '&nbsp;' end,
					case when _CXCP.Retraso between cast(_DESDE3 as integer) and  cast(_HASTA3 as integer) then (_CXCP.Simbolo || ' ' || cast(round(_CXCP.Saldo,2) as varchar)) else '&nbsp;' end,
					case when _CXCP.Retraso > cast(_HASTA3 as integer) then (_CXCP.Simbolo || ' ' || cast(round(_CXCP.Saldo,2) as varchar)) else '&nbsp;' end;

			END LOOP;
			CLOSE _CUR_CXCP;

		END IF;
		
	END LOOP;
	CLOSE _CUR_CP;

	INSERT INTO _TMP_CLIPRO_ANTIGUEDAD_SALDOS (Clave, Nombre, Saldo, NV, V1, V2, V3, V4) 
	VALUES('&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;');

	IF _id_clipro = -1
	THEN
		_AND_ID_CLIPRO := ''; --No aplica a ningun cliente o proveedor, por lo tanto no candiciona a nada... tomará a todos los clientes o proveedores
	ELSE
		_AND_ID_CLIPRO := 'and tmp.ID_Clave = ' || quote_literal(_id_clipro) ; 
	END IF;
	
	IF _entidad = -1 --Todas las entidades
	THEN
		_AND_ID_ENTIDAD := ''; --No aplica a ningun cliente o proveedor, por lo tanto no candiciona a nada... tomará a todos los clientes o proveedores
	ELSIF _entidad = -2 --Solo entidades contables
	THEN
		_AND_ID_ENTIDAD := 'AND tmp.Fija = ''0''';
	ELSIF _entidad = -3 --Solo entidades fijas
	THEN
		_AND_ID_ENTIDAD := 'AND tmp.Fija = ''1''';
	ELSE --La entidad seleccionada
		_AND_ID_ENTIDAD := 'AND tmp.ID_Entidad = ' || quote_literal(_entidad) ; 
	END IF;

	OPEN _CUR_CP FOR EXECUTE 'select ent.descripcion, tmp.id_moneda, m.simbolo, m.tc, coalesce(sum(tmp.saldo),0) as saldo, coalesce(sum(tmp.nv),0) as nv, coalesce(sum(tmp.v1),0) as v1, coalesce(sum(tmp.v2),0) as v2, coalesce(sum(tmp.v3),0) as v3, coalesce(sum(tmp.v4),0) as v4 
									from _TMP_PRE_ACUM tmp inner join tbl_cont_monedas m 
										on tmp.id_moneda = m.clave inner  join ' || _TBL_CLIPRO || ' cat 
										on tmp.id_clave = cat.id_clave inner join ' || _TBL_VENCOMP_ENT || ' ent 
										on tmp.ID_Entidad = ent.' || _id_entidadvencomp || '
									where tmp.saldo <> 0.00 ' || _AND_ID_ENTIDAD || ' ' || _AND_ID_CLIPRO || '
									group by tmp.id_entidad, ent.descripcion, tmp.id_moneda, m.simbolo, m.tc
									order by tmp.id_entidad asc, tmp.id_moneda asc';
	LOOP
		FETCH NEXT FROM _CUR_CP INTO _CP;
		EXIT WHEN _CP IS NULL;
		
		INSERT INTO _TMP_CLIPRO_ANTIGUEDAD_SALDOS (Clave, Nombre, Saldo, NV, V1, V2, V3, V4) 
		VALUES(  '<b>' || (case when (_entidad = -1 or _entidad = -2 or _entidad = -3) then _CP.Descripcion else '' end) || '</b>', '', 
					'<b>' || _CP.Simbolo || ' ' || cast(round(_CP.Saldo,2) as varchar) || (case when _CP.ID_Moneda <> 1 then ' $ ' || cast(round(_CP.TC,2) as varchar) else '' end) || '</b>',
					'<b>' || _CP.Simbolo || ' ' || cast(round(_CP.NV,2) as varchar) || '</b>',
					'<b>' || _CP.Simbolo || ' ' || cast(round(_CP.V1,2) as varchar) || '</b>',
					'<b>' || _CP.Simbolo || ' ' || cast(round(_CP.V2,2) as varchar) || '</b>',
					'<b>' || _CP.Simbolo || ' ' || cast(round(_CP.V3,2) as varchar) || '</b>',
					'<b>' || _CP.Simbolo || ' ' || cast(round(_CP.V4,2) as varchar) || '</b>' );	
	END LOOP;

	CLOSE _CUR_CP;

	INSERT INTO _TMP_CLIPRO_ANTIGUEDAD_SALDOS (Clave, Nombre, Saldo, NV, V1, V2, V3, V4) 
	VALUES('&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;', '&nbsp;');

	OPEN _CUR_CP FOR EXECUTE 'select coalesce(sum(tmp.saldo * m.tc),0) as saldo, coalesce(sum(tmp.nv * m.tc),0) as nv, coalesce(sum(tmp.v1 * m.tc),0) as v1, coalesce(sum(tmp.v2 * m.tc),0) as v2, coalesce(sum(tmp.v3 * m.tc),0) as v3, coalesce(sum(tmp.v4 * m.tc),0) as v4 
									from _TMP_PRE_ACUM tmp inner join tbl_cont_monedas m 
										on tmp.id_moneda = m.clave inner  join ' || _TBL_CLIPRO || ' cat 
										on tmp.id_clave = cat.id_clave inner join ' || _TBL_VENCOMP_ENT || ' ent 
										on tmp.ID_Entidad = ent.' || _id_entidadvencomp || '
									where tmp.saldo <> 0.00 ' || _AND_ID_ENTIDAD || ' ' || _AND_ID_CLIPRO;
	LOOP
		FETCH NEXT FROM _CUR_CP INTO _CP;
		EXIT WHEN _CP IS NULL;

		INSERT INTO _TMP_CLIPRO_ANTIGUEDAD_SALDOS (Clave, Nombre, Saldo, NV, V1, V2, V3, V4) 
		VALUES(  '&nbsp;', '&nbsp;',
					'<b>$ ' || cast(round(_CP.Saldo,2) as varchar) || '</b>',
					'<b>$ ' || cast(round(_CP.NV,2) as varchar) || '</b>',
					'<b>$ ' || cast(round(_CP.V1,2) as varchar) || '</b>',
					'<b>$ ' || cast(round(_CP.V2,2) as varchar) || '</b>',
					'<b>$ ' || cast(round(_CP.V3,2) as varchar) || '</b>',
					'<b>$ ' || cast(round(_CP.V4,2) as varchar) || '</b>' ); 
	END LOOP;

	CLOSE _CUR_CP;
		
	RETURN QUERY
	select Clave, Nombre, Saldo, NV, V1, V2, V3, V4
	from _TMP_CLIPRO_ANTIGUEDAD_SALDOS
	order by part ASC;
	
	
	DROP TABLE _TMP_CLIPRO_ANTIGUEDAD_SALDOS;
	DROP TABLE  _TMP_PRE_ACUM;
	
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_clipro_antiguedad_saldos(character, boolean, character varying, integer, smallint, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION gettipocxpc(_tipocp character)
  RETURNS text AS
$BODY$  
BEGIN
	if _tipocp = 'ALT' 
	then 
		return 'Cuenta';
	elsif _tipocp = 'ANT' 
	then 
		return 'Anticipo';
	elsif _tipocp = 'PAG' 
	then 
		return 'Pago';
	elsif _tipocp = 'SAL' 
	then 
		return 'Saldo';
	elsif _tipocp = 'APL' 
	then 
		return 'Aplicacion';
	elsif _tipocp = 'DPA' 
	then 
		return 'Descuento';
	elsif _tipocp = 'DEV' 
	then 
		return 'Devolucion';
	else
		return '?????????';
	end if;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION gettipocxpc(character)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_prod2_gas AS 
 SELECT c.id_prod AS clave, c.descripcion, c.id_unidadsalida AS especial
   FROM tbl_invserv_inventarios c
  WHERE (c.id_tipo = 'P'::bpchar OR c.id_tipo = 'G'::bpchar) AND c.seproduce = B'0'::"bit" AND c.status = 'V'::bpchar;

ALTER TABLE view_catalog_prod2_gas
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_lineas_prodgas AS 
 SELECT tbl_invserv_lineas.id_linea AS clave, tbl_invserv_lineas.descripcion, 
        CASE
            WHEN tbl_invserv_lineas.id_invserv = 'P'::bpchar THEN 'Productos'::text
            ELSE 'Gastos'::text
        END AS especial
   FROM tbl_invserv_lineas
  WHERE tbl_invserv_lineas.id_invserv = 'P'::bpchar OR tbl_invserv_lineas.id_invserv = 'G'::bpchar;

ALTER TABLE view_catalog_lineas_prodgas
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getmodulofrom4(_mod4 character)
  RETURNS text AS
$BODY$  
DECLARE _res text;
BEGIN
	IF _mod4 is null
	THEN
		RETURN ''::text;
	END IF;
	
	--Empieza en el modulo de ventas
	IF _mod4 = 'VDEV' 
	THEN
		_res := 'Devolución s/venta';
	ELSIF _mod4 = 'VFAC' 
	THEN
		_res := 'Factura';
	ELSIF _mod4 = 'VREM' 
	THEN
		_res := 'Remisión';
	ELSIF _mod4 = 'VPED' 
	THEN
		_res := 'Pedido';
	ELSIF _mod4 = 'VCOT' 
	THEN
		_res := 'Cotización';
	ELSIF _mod4 = 'VCXC' 
	THEN
		_res := 'Cuenta por cobrar';
	--Prosigue con el modulo de compras
	ELSIF _mod4 = 'CDEV' 
	THEN
		_res := 'Devolución s/compra';
	ELSIF _mod4 = 'CFAC' 
	THEN
		_res := 'Compra';
	ELSIF _mod4 = 'CREC' 
	THEN
		_res := 'Recepción';
	ELSIF _mod4 = 'CORD' 
	THEN
		_res := 'Orden de compra';
	ELSIF _mod4 = 'CGAS' 
	THEN
		_res := 'Gasto';
	ELSIF _mod4 = 'CCXP' 
	THEN
		_res := 'Cuenta por pagar';
	--Modulo de caja y bancos
	ELSIF _mod4 = 'MBAN' 
	THEN
		_res := 'Mov. de Banco';
	ELSIF _mod4 = 'MCAJ' 
	THEN
		_res := 'Mov. de Caja';
	--Modulo de nómina
	ELSIF _mod4 = 'NNOM' 
	THEN
		_res := 'Nómina';
	--Modulo de almacen
	ELSIF _mod4 = 'MALM' 
	THEN
		_res := 'Mov. al Almacén';
	ELSIF _mod4 = 'PALM' 
	THEN
		_res := 'Plantilla';
	ELSIF _mod4 = 'TALM' 
	THEN
		_res := 'Traspaso';
	ELSIF _mod4 = 'RALM' 
	THEN
		_res := 'Requerimiento';
	--Reportes de produccion
	ELSIF _mod4 = 'PPRD' 
	THEN
		_res := 'Reporte';
	
	ELSE
		_res := _ref;
	END IF;
	
	return _res;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getmodulofrom4(character)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_comp_dev_acum(
    _mes smallint,
    _ano smallint,
    _ultimos smallint,
    _noacumulados bit,
    _entcons smallint,
    _tipo character)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_cont smallint; _TotalEnt smallint; _NumEnt smallint; _NombreEnt varchar(15);
	_TotalDevoluciones numeric(19,4); _UltimoAno numeric(19,4); _PenultimoAno numeric(19,4); _AnteriorAno numeric(19,4);
	_ENE numeric(19,4); _FEB numeric(19,4); _MAR numeric(19,4); _ABR numeric(19,4); _MAY numeric(19,4); _JUN numeric(19,4); 
	_JUL numeric(19,4); _AGO numeric(19,4); _SEP numeric(19,4); _OCT numeric(19,4);	_NOV numeric(19,4); _DIC numeric(19,4);
	_TVE RECORD;
BEGIN

	

CREATE LOCAL TEMPORARY TABLE _TMP_DEVOLUCIONES_ENT (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_DEVOLUCIONES (
		part serial NOT NULL ,
		Periodo varchar (20)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	);
	

CREATE LOCAL TEMPORARY TABLE _TMP_DEVOLUCIONES_ENT_ANO (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Ultimo numeric(19,4) NOT NULL ,
		Penultimo numeric(19,4) NOT NULL ,
		Anterior numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_DEVOLUCIONES_ENT_MES (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		ENE numeric(19,4) NOT NULL ,
		FEB numeric(19,4) NOT NULL ,
		MAR numeric(19,4) NOT NULL ,
		ABR numeric(19,4) NOT NULL ,
		MAY numeric(19,4) NOT NULL ,
		JUN numeric(19,4) NOT NULL ,
		JUL numeric(19,4) NOT NULL ,
		AGO numeric(19,4) NOT NULL ,
		SEP numeric(19,4) NOT NULL ,
		OCT numeric(19,4) NOT NULL ,
		NOV numeric(19,4) NOT NULL ,
		DIC numeric(19,4) NOT NULL 
	); 

	IF(_NoAcumulados = '0') -- es acumulados generales
	THEN
		IF(_EntCons = -1) -- Se trata del acumulado de entidades generales
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					if _Tipo = '---'
					then
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and date_part('Month',Fecha) = _cont and date_part('Year',Fecha) = _Ano ),0);
					else
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and date_part('Month',Fecha) = _cont and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ),0);
					end if;

					INSERT INTO _TMP_DEVOLUCIONES (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalDevoluciones,0);
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual

				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					if _Tipo = '---'
					then
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and date_part('Year',Fecha) = _cont ),0);
					else
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and date_part('Year',Fecha) = _cont and DevReb = _Tipo ),0);
					end if;
					
					INSERT INTO _TMP_DEVOLUCIONES (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalDevoluciones,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		
		ELSE -- se trata de una entidad en especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					if _Tipo = '---'
					then
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where ID_Entidad = _EntCons and Status <> 'C' and date_part('Month',Fecha) = _cont and date_part('Year',Fecha) = _Ano ),0);
					else
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where ID_Entidad = _EntCons and Status <> 'C' and date_part('Month',Fecha) = _cont and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ),0);
					end if;
					
					INSERT INTO _TMP_DEVOLUCIONES (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalDevoluciones,0); 
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual
				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					if _Tipo = '---'
					then
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where ID_Entidad = _EntCons and Status <> 'C' and date_part('Year',Fecha) = _cont ),0);
					else
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where ID_Entidad = _EntCons and Status <> 'C' and date_part('Year',Fecha) = _cont and DevReb = _Tipo ),0);
					end if;
					
					INSERT INTO _TMP_DEVOLUCIONES (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalDevoluciones,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		END IF;

		RETURN QUERY
		select Periodo, Total
		from _TMP_DEVOLUCIONES
		order by part ASC;
	
	ELSE -- Se maneja por detalles de entidad
	
		IF(_EntCons = -1) -- Se trata de comparativos todas las entidades en todos los meses o a?os
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 0
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;

					if _Tipo = '---'
					then
						_ENE := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 1 and date_part('Year',Fecha) = _Ano ), 0);
						_FEB := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 2 and date_part('Year',Fecha) = _Ano ), 0);
						_MAR := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 3 and date_part('Year',Fecha) = _Ano ), 0);
						_ABR := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 4 and date_part('Year',Fecha) = _Ano ), 0);
						_MAY := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 5 and date_part('Year',Fecha) = _Ano ), 0);
						_JUN := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 6 and date_part('Year',Fecha) = _Ano ), 0);
						_JUL := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 7 and date_part('Year',Fecha) = _Ano ), 0);
						_AGO := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 8 and date_part('Year',Fecha) = _Ano ), 0);
						_SEP := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 9 and date_part('Year',Fecha) = _Ano ), 0);
						_OCT := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 10 and date_part('Year',Fecha) = _Ano ), 0);
						_NOV := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 11 and date_part('Year',Fecha) = _Ano ), 0);
						_DIC := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 12 and date_part('Year',Fecha) = _Ano ), 0);
					else
						_ENE := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 1 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_FEB := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 2 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_MAR := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 3 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_ABR := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 4 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_MAY := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 5 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_JUN := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 6 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_JUL := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 7 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_AGO := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 8 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_SEP := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 9 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_OCT := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 10 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_NOV := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 11 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
						_DIC := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_DEVOLUCIONES_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 12 and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
					end if;
					
					INSERT INTO _TMP_DEVOLUCIONES_ENT_MES (Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC)
					SELECT _NombreEnt, round(_ENE,0), round(_FEB,0), round(_MAR,0),  round(_ABR,0),  round(_MAY,0),  round(_JUN,0),  
						round(_JUL,0),  round(_AGO,0),  round(_SEP,0), round(_OCT,0),  round(_NOV,0),  round(_DIC,0);
									
				end loop;

				RETURN QUERY
				select Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC
				from _TMP_DEVOLUCIONES_ENT_MES;
		
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 0
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;

					if _Tipo = '---'
					then
						_UltimoAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = _Ano ),0);
						_PenultimoAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = (_Ano-1) ),0);
						_AnteriorAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = (_Ano-2) ),0);
					else
						_UltimoAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ),0);
						_PenultimoAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = (_Ano-1) and DevReb = _Tipo ),0);
						_AnteriorAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = (_Ano-2) and DevReb = _Tipo ),0);
					end if;
					
					INSERT INTO _TMP_DEVOLUCIONES_ENT_ANO (Entidad,Ultimo,Penultimo,Anterior)
					SELECT _NombreEnt, round(_UltimoAno,0), round(_PenultimoAno,0), round(_AnteriorAno,0);

				end loop;
				
				RETURN QUERY
				select Entidad,Ultimo,Penultimo,Anterior
				from _TMP_DEVOLUCIONES_ENT_ANO;
		
			end if;
		
		ELSE -- se trata de comparativos de todas las entidades pero en un mes o ano especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 0
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;

					if _Tipo = '---'
					then
						_TotalDevoluciones := coalesce( (	select sum(SubTotal * TC)
																				from TBL_COMPRAS_DEVOLUCIONES_CAB
																				where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = _Mes and date_part('Year',Fecha) = _Ano ), 0);
					else
						_TotalDevoluciones := coalesce( (	select sum(SubTotal * TC)
																				from TBL_COMPRAS_DEVOLUCIONES_CAB
																				where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = _Mes and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ), 0);
					end if;
					
					INSERT INTO _TMP_DEVOLUCIONES_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalDevoluciones,0);   
									
				end loop;
				
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 0
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;

					if _Tipo = '---'
					then
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = _Ano ),0);
					else
						_TotalDevoluciones := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_DEVOLUCIONES_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = _Ano and DevReb = _Tipo ),0);
					end if;
					
					INSERT INTO _TMP_DEVOLUCIONES_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalDevoluciones,0);  
					
				end loop;
		
			end if;

			RETURN QUERY
			select Entidad,Total
			from _TMP_DEVOLUCIONES_ENT;

		END IF;

	END IF;
	
	DROP TABLE _TMP_DEVOLUCIONES;
	DROP TABLE _TMP_DEVOLUCIONES_ENT;
	DROP TABLE _TMP_DEVOLUCIONES_ENT_MES;
	DROP TABLE _TMP_DEVOLUCIONES_ENT_ANO;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_comp_dev_acum(smallint, smallint, smallint, bit, smallint, character)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_comp_gas_acum(
    _mes smallint,
    _ano smallint,
    _ultimos smallint,
    _noacumulados bit,
    _entcons smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_cont smallint; _TotalEnt smallint; _NumEnt smallint; _NombreEnt varchar(15);
	_TotalGastos numeric(19,4); _UltimoAno numeric(19,4); _PenultimoAno numeric(19,4); _AnteriorAno numeric(19,4);
	_ENE numeric(19,4); _FEB numeric(19,4); _MAR numeric(19,4); _ABR numeric(19,4); _MAY numeric(19,4); _JUN numeric(19,4); 
	_JUL numeric(19,4); _AGO numeric(19,4); _SEP numeric(19,4); _OCT numeric(19,4);	_NOV numeric(19,4); _DIC numeric(19,4);
	_TVE RECORD;
BEGIN

	

CREATE LOCAL TEMPORARY TABLE _TMP_GASTOS_ENT (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_GASTOS (
		part serial NOT NULL ,
		Periodo varchar (20)  NOT NULL ,
		Total numeric(19,4) NOT NULL 
	);
	

CREATE LOCAL TEMPORARY TABLE _TMP_GASTOS_ENT_ANO (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		Ultimo numeric(19,4) NOT NULL ,
		Penultimo numeric(19,4) NOT NULL ,
		Anterior numeric(19,4) NOT NULL 
	); 
	

CREATE LOCAL TEMPORARY TABLE _TMP_GASTOS_ENT_MES (
		part serial NOT NULL ,
		Entidad varchar (15)  NOT NULL ,
		ENE numeric(19,4) NOT NULL ,
		FEB numeric(19,4) NOT NULL ,
		MAR numeric(19,4) NOT NULL ,
		ABR numeric(19,4) NOT NULL ,
		MAY numeric(19,4) NOT NULL ,
		JUN numeric(19,4) NOT NULL ,
		JUL numeric(19,4) NOT NULL ,
		AGO numeric(19,4) NOT NULL ,
		SEP numeric(19,4) NOT NULL ,
		OCT numeric(19,4) NOT NULL ,
		NOV numeric(19,4) NOT NULL ,
		DIC numeric(19,4) NOT NULL 
	); 

	IF(_NoAcumulados = '0') -- es acumulados generales
	THEN
		IF(_EntCons = -1) -- Se trata del acumulado de entidades generales
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					_TotalGastos := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_GASTOS_CAB
																			where Status <> 'C' and date_part('Month',Fecha) = _cont and date_part('Year',Fecha) = _Ano ),0);
					INSERT INTO _TMP_GASTOS (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalGastos,0);
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual

				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					_TotalGastos := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_GASTOS_CAB
																			where Status <> 'C' and date_part('Year',Fecha) = _cont ),0);
					
					INSERT INTO _TMP_GASTOS (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalGastos,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		
		ELSE -- se trata de una entidad en especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				_cont := 1;
				while _cont <= 12
				loop
					_TotalGastos := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_GASTOS_CAB
																			where ID_Entidad = _EntCons and Status <> 'C' and date_part('Month',Fecha) = _cont and date_part('Year',Fecha) = _Ano ),0);
					INSERT INTO _TMP_GASTOS (Periodo,Total)
					SELECT GetCharMonth(_cont), round(_TotalGastos,0); 
				
					_cont := _cont + 1;
				end loop;
			
			else -- De lo contrario es un comparativo anual
				_cont := _Ano - _Ultimos + 1;
			
				while _cont <= _Ano
				loop
					_TotalGastos := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_GASTOS_CAB
																			where ID_Entidad = _EntCons and Status <> 'C' and date_part('Year',Fecha) = _cont ),0);
					
					INSERT INTO _TMP_GASTOS (Periodo,Total)
					SELECT cast(_cont as varchar), round(_TotalGastos,0); 
					
					_cont := _cont + 1;
				end loop;
			
			end if;
		END IF;

		RETURN QUERY
		select Periodo, Total
		from _TMP_GASTOS
		order by part ASC;
	
	ELSE -- Se maneja por detalles de entidad
	
		IF(_EntCons = -1) -- Se trata de comparativos todas las entidades en todos los meses o a?os
		THEN
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 2
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;
		
					_ENE := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 1 and date_part('Year',Fecha) = _Ano ), 0);
					_FEB := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 2 and date_part('Year',Fecha) = _Ano ), 0);
					_MAR := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 3 and date_part('Year',Fecha) = _Ano ), 0);
					_ABR := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 4 and date_part('Year',Fecha) = _Ano ), 0);
					_MAY := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 5 and date_part('Year',Fecha) = _Ano ), 0);
					_JUN := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 6 and date_part('Year',Fecha) = _Ano ), 0);
					_JUL := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 7 and date_part('Year',Fecha) = _Ano ), 0);
					_AGO := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 8 and date_part('Year',Fecha) = _Ano ), 0);
					_SEP := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 9 and date_part('Year',Fecha) = _Ano ), 0);
					_OCT := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 10 and date_part('Year',Fecha) = _Ano ), 0);
					_NOV := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 11 and date_part('Year',Fecha) = _Ano ), 0);
					_DIC := coalesce( (	select sum(SubTotal * TC)
																from TBL_COMPRAS_GASTOS_CAB
																where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = 12 and date_part('Year',Fecha) = _Ano ), 0);
		
					INSERT INTO _TMP_GASTOS_ENT_MES (Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC)
					SELECT _NombreEnt, round(_ENE,0), round(_FEB,0), round(_MAR,0),  round(_ABR,0),  round(_MAY,0),  round(_JUN,0),  
						round(_JUL,0),  round(_AGO,0),  round(_SEP,0), round(_OCT,0),  round(_NOV,0),  round(_DIC,0);
									
				end loop;

				RETURN QUERY
				select Entidad,ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC
				from _TMP_GASTOS_ENT_MES;
		
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 2
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;
		
					_UltimoAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_GASTOS_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = _Ano ),0);
					_PenultimoAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_GASTOS_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = (_Ano-1) ),0);
					_AnteriorAno := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_GASTOS_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = (_Ano-2) ),0);
		
					INSERT INTO _TMP_GASTOS_ENT_ANO (Entidad,Ultimo,Penultimo,Anterior)
					SELECT _NombreEnt, round(_UltimoAno,0), round(_PenultimoAno,0), round(_AnteriorAno,0);

				end loop;
				
				RETURN QUERY
				select Entidad,Ultimo,Penultimo,Anterior
				from _TMP_GASTOS_ENT_ANO;
		
			end if;
		
		ELSE -- se trata de comparativos de todas las entidades pero en un mes o ano especifico
		
			if(_Ultimos = 1) -- se trata de comparativo mensual
			then
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 2
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;
		
					_TotalGastos := coalesce( (	select sum(SubTotal * TC)
																				from TBL_COMPRAS_GASTOS_CAB
																				where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Month',Fecha) = _Mes and date_part('Year',Fecha) = _Ano ), 0);
		
					INSERT INTO _TMP_GASTOS_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalGastos,0);   
									
				end loop;
				
			else -- De lo contrario es un comparativo anual
			
				for _TVE in	(	SELECT ID_EntidadCompra, Descripcion
								FROM TBL_COMPRAS_ENTIDADES
								WHERE ID_TipoEntidad = 2
								ORDER BY ID_EntidadCompra asc
							)
				loop
					_NumEnt := _TVE.ID_EntidadCompra;
					_NombreEnt := _TVE.Descripcion;
					
					_TotalGastos := coalesce(( select sum(SubTotal * TC) 
																			from TBL_COMPRAS_GASTOS_CAB
																			where Status <> 'C' and ID_Entidad = _NumEnt and date_part('Year',Fecha) = _Ano ),0);
		
					INSERT INTO _TMP_GASTOS_ENT (Entidad,Total)
					SELECT _NombreEnt, round(_TotalGastos,0);  
					
				end loop;
		
			end if;

			RETURN QUERY
			select Entidad,Total
			from _TMP_GASTOS_ENT;

		END IF;

	END IF;
	
	DROP TABLE _TMP_GASTOS;
	DROP TABLE _TMP_GASTOS_ENT;
	DROP TABLE _TMP_GASTOS_ENT_MES;
	DROP TABLE _TMP_GASTOS_ENT_ANO;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_comp_gas_acum(smallint, smallint, smallint, bit, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_client_poriden AS 
 SELECT c.id_clave AS clave, c.nombre AS descripcion, (e.descripcion::text || ':'::text) || c.id_numero AS especial, c.id_entidad
   FROM tbl_client_client c
   JOIN tbl_ventas_entidades e ON c.id_entidad = e.id_entidadventa
  WHERE c.status = 'A'::bpchar;

ALTER TABLE view_catalog_client_poriden
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_lineas_prodserv AS 
 SELECT tbl_invserv_lineas.id_linea AS clave, tbl_invserv_lineas.descripcion, 
        CASE
            WHEN tbl_invserv_lineas.id_invserv = 'P'::bpchar THEN 'Productos'::text
            ELSE 'Servicios'::text
        END AS especial
   FROM tbl_invserv_lineas
  WHERE tbl_invserv_lineas.id_invserv = 'P'::bpchar OR tbl_invserv_lineas.id_invserv = 'S'::bpchar;

ALTER TABLE view_catalog_lineas_prodserv
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_prod4_serv AS 
 SELECT c.id_prod AS clave, c.descripcion, c.id_unidadsalida AS especial
   FROM tbl_invserv_inventarios c
  WHERE (c.id_tipo = 'P'::bpchar OR c.id_tipo = 'S'::bpchar) AND c.nosevende = B'0'::"bit" AND c.status = 'V'::bpchar;

ALTER TABLE view_catalog_prod4_serv
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_entidades_nomina AS 
 SELECT c.id_sucursal AS clave, c.descripcion, c.status AS especial
   FROM tbl_companias c;

ALTER TABLE view_catalog_entidades_nomina
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_empleados_poriden AS 
 SELECT c.id_empleado AS clave, (((c.nombre::text || ' '::text) || c.apellido_paterno::text) || ' '::text) || c.apellido_materno::text AS descripcion, (e.descripcion::text || ':'::text) || c.id_empleado::text AS especial, c.id_sucursal
   FROM tbl_nom_masemp c
   JOIN tbl_companias e ON c.id_compania = e.id_compania AND c.id_sucursal = e.id_sucursal
  WHERE c.status = 0;

ALTER TABLE view_catalog_empleados_poriden
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rep_calculo_aguinaldo(
    _id_sucursal smallint,
    _ano integer)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_EMP RECORD; _NOM RECORD; _fecha_desde timestamp; _fecha_hasta timestamp; _AnosCumplidos smallint; _DiasAg smallint; 
	_ImporteAguinaldo numeric(19,4); _IAE numeric(10,2); _IAG numeric(10,2); _DiasAno smallint; _TOTAL_DAG numeric(10,6); 
	_SalarioMinimo numeric(19,4); _TIAG numeric(10,2); _TIAE numeric(10,2); _TTIAG numeric(10,2); _TTIAE numeric(10,2); 
BEGIN
	_fecha_desde = getfecha(1,1,_ano);
	_fecha_hasta = getfecha(31,12,_ano);
	_DiasAno := getfechadiff('day', getfecha(1,1,_ano), getfecha(1,1,(_ano+1)) );
	_TIAG := 0.00;
	_TIAE := 0.00;
	_TTIAG := 0.00;
	_TTIAE := 0.00;
	_SalarioMinimo := (	select VDecimal from TBL_VARIABLES	
							where ID_Variable = 'SALMIN' );	
	CREATE LOCAL TEMPORARY TABLE _TMP_AGUINALDOS (
		ID_Empleado char(6) NOT NULL ,
		Nombre varchar(254) NOT NULL ,
		Ingreso varchar(12) NOT NULL ,
		Anos smallint NOT NULL ,
		Dias smallint NOT NULL ,
		IAG numeric(10,2) NOT NULL ,
		IAE numeric(10,2) NOT NULL ,
		Total numeric(10,2) NOT NULL
	); 

	IF _ID_Sucursal <> -1
	THEN
		FOR _EMP IN
			( SELECT ID_Empleado, (Nombre || ' ' || Apellido_Paterno || ' ' || Apellido_Materno) as Nombre, Fecha_de_Ingreso as Ingreso,
				CalculoMixto, Salario_Diario, Salario_Mixto
			 FROM TBL_NOM_MASEMP
			 WHERE ID_Sucursal = _ID_Sucursal and Fecha_de_Ingreso <= _Fecha_Hasta and 
				((Status = 0) or (Status = 2 and Fecha_para_Liquidaciones >= _Fecha_Desde))
			 ORDER BY ID_Empleado ASC )
		LOOP
			_AnosCumplidos := getabsfechadiff('year',_EMP.Ingreso,_Fecha_Hasta);
			_TOTAL_DAG := 	case 	when _EMP.Ingreso < _Fecha_Desde
						then getfechadiff('day',_Fecha_Desde, _Fecha_Hasta) + 1
						else getfechadiff('day',_EMP.Ingreso, _Fecha_Hasta) + 1 
					end;
			_DiasAg := case		when -- Selecciona los dias de aguinaldo segun su antigüedad
					( 	select a.Dias 
						from TBL_NOM_AGUINALDO a
						where a.Desde <= _AnosCumplidos and a.Hasta >= _AnosCumplidos 
						limit 1 ) is null then 0 
					else
					   ( 	select a.Dias 
						from TBL_NOM_AGUINALDO a 
						where a.Desde <= _AnosCumplidos and a.Hasta >= _AnosCumplidos
						limit 1  ) 
					end;

			IF _TOTAL_DAG <> 0
			THEN
				_ImporteAguinaldo := ROUND( ((case when _EMP.CalculoMixto = '0' then _EMP.Salario_Diario else _EMP.Salario_Mixto end) * 
									(cast((_TOTAL_DAG * _DiasAg) as numeric) / _DiasAno )), 2);
				_IAE := case when ROUND( ((_TOTAL_DAG / _DiasAno) * (_SalarioMinimo * 30)), 2) < _ImporteAguinaldo 
									then  ROUND( ((_TOTAL_DAG / _DiasAno) * (_SalarioMinimo * 30)), 2) 
									else _ImporteAguinaldo 
								end;
				_IAG := case when _IAE < _ImporteAguinaldo then _ImporteAguinaldo - _IAE else 0.00 end;	
			ELSE
				_IAE := 0.00;
				_IAG := 0.00;		
			END IF;

			insert into _TMP_AGUINALDOS(ID_Empleado, Nombre, Ingreso, Anos, Dias, IAG, IAE, Total)
			values(_EMP.ID_Empleado, _EMP.Nombre, to_char(_EMP.Ingreso,'DD/MM/YYYY')::varchar, _AnosCumplidos, _DiasAg, _IAG, _IAE, (_IAG + _IAE));

			_TIAG := _TIAG + _IAG;
			_TIAE := _TIAE + _IAE;
			
		END LOOP;

		insert into _TMP_AGUINALDOS(ID_Empleado, Nombre, Ingreso, Anos, Dias, IAG, IAE, Total)
		values('      ', '', '', 0, 0, 0.00, 0.00, 0.00);

		insert into _TMP_AGUINALDOS(ID_Empleado, Nombre, Ingreso, Anos, Dias, IAG, IAE, Total)
		values('      ', '<b>TOTAL DE AGUINALDO</b>', '', 0, 0, _TIAG, _TIAE, (_TIAG + _TIAE));

	ELSE --Es de todas las entidades
		FOR _NOM IN (SELECT ID_Sucursal, Descripcion FROM TBL_COMPANIAS ORDER BY ID_Sucursal ASC)
		LOOP
			_TIAG := 0.0;
			_TIAE := 0.0;

			FOR _EMP IN
				( SELECT ID_Empleado, (Nombre || ' ' || Apellido_Paterno || ' ' || Apellido_Materno) as Nombre, Fecha_de_Ingreso as Ingreso,
					CalculoMixto, Salario_Diario, Salario_Mixto
				 FROM TBL_NOM_MASEMP
				 WHERE ID_Sucursal = _NOM.ID_Sucursal and Fecha_de_Ingreso <= _Fecha_Hasta and 
					((Status = 0) or (Status = 2 and Fecha_para_Liquidaciones >= _Fecha_Desde))
				 ORDER BY ID_Empleado ASC )
			LOOP
				_AnosCumplidos := getabsfechadiff('year',_EMP.Ingreso,_Fecha_Hasta);
				_TOTAL_DAG := 	case 	when _EMP.Ingreso < _Fecha_Desde
							then getfechadiff('day',_Fecha_Desde, _Fecha_Hasta) + 1
							else getfechadiff('day',_EMP.Ingreso, _Fecha_Hasta) + 1 
						end;
				_DiasAg := case		when -- Selecciona los dias de aguinaldo segun su antigüedad
						( 	select a.Dias 
							from TBL_NOM_AGUINALDO a
							where a.Desde <= _AnosCumplidos and a.Hasta >= _AnosCumplidos 
							limit 1 ) is null then 0 
						else
						   ( 	select a.Dias 
							from TBL_NOM_AGUINALDO a 
							where a.Desde <= _AnosCumplidos and a.Hasta >= _AnosCumplidos
							limit 1  ) 
						end;

				IF _TOTAL_DAG <> 0
				THEN
					_ImporteAguinaldo := ROUND( ((case when _EMP.CalculoMixto = '0' then _EMP.Salario_Diario else _EMP.Salario_Mixto end) * 
										(cast((_TOTAL_DAG * _DiasAg) as numeric) / _DiasAno )), 2);
					_IAE := case when ROUND( ((_TOTAL_DAG / _DiasAno) * (_SalarioMinimo * 30)), 2) < _ImporteAguinaldo 
										then  ROUND( ((_TOTAL_DAG / _DiasAno) * (_SalarioMinimo * 30)), 2) 
										else _ImporteAguinaldo 
									end;
					_IAG := case when _IAE < _ImporteAguinaldo then _ImporteAguinaldo - _IAE else 0.00 end;	
				ELSE
					_IAE := 0.00;
					_IAG := 0.00;		
				END IF;

				insert into _TMP_AGUINALDOS(ID_Empleado, Nombre, Ingreso, Anos, Dias, IAG, IAE, Total)
				values(_EMP.ID_Empleado, _EMP.Nombre, to_char(_EMP.Ingreso,'DD/MM/YYYY')::varchar, _AnosCumplidos, _DiasAg, 
						_IAG, _IAE, (_IAG + _IAE));

				_TIAG := _TIAG + _IAG;
				_TIAE := _TIAE + _IAE;
				
			END LOOP;

			insert into _TMP_AGUINALDOS(ID_Empleado, Nombre, Ingreso, Anos, Dias, IAG, IAE, Total)
			values('      ', '', '', 0, 0, 0.00, 0.00, 0.00);

			insert into _TMP_AGUINALDOS(ID_Empleado, Nombre, Ingreso, Anos, Dias, IAG, IAE, Total)
			values('      ', '<b>TOTAL DE ' || _NOM.Descripcion || '</b>', '', 0, 0, _TIAG, _TIAE, (_TIAG + _TIAE));

			insert into _TMP_AGUINALDOS(ID_Empleado, Nombre, Ingreso, Anos, Dias, IAG, IAE, Total)
			values('      ', '', '', 0, 0, 0.00, 0.00, 0.00);

			_TTIAG := _TTIAG + _TIAG;
			_TTIAE := _TTIAE + _TIAE;
		END LOOP;
		
		insert into _TMP_AGUINALDOS(ID_Empleado, Nombre, Ingreso, Anos, Dias, IAG, IAE, Total)
		values('      ', '<b>TOTAL DE AGUINALDO</b>', '', 0, 0, _TTIAG, _TTIAE, (_TTIAG + _TTIAE));

	END IF;
	
	RETURN QUERY
	SELECT * FROM _TMP_AGUINALDOS;
	
	DROP TABLE _TMP_AGUINALDOS;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION rep_calculo_aguinaldo(smallint, integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_movimientos_sis AS 
 SELECT m.id_movimiento AS clave, ( SELECT mn.msj1
           FROM tbl_msj mn
          WHERE mn.alc::text = 'CEF'::text AND mn.mod::text = 'NOMINA'::text AND mn.sub::text = 'CAT_MOV'::text AND mn.elm::text = m.id_movimiento::text) AS descripcion, ( SELECT (((
                CASE
                    WHEN tbl_nom_movimientos.diascompletos = B'0'::"bit" THEN 'H'::text
                    ELSE 'D'::text
                END || '/'::text) || 
                CASE
                    WHEN tbl_nom_movimientos.aplicaaltipo = (-1) THEN 'S'::text
                    WHEN tbl_nom_movimientos.aplicaaltipo = 0 THEN 'I'::text
                    WHEN tbl_nom_movimientos.aplicaaltipo = 1 THEN 'E'::text
                    WHEN tbl_nom_movimientos.aplicaaltipo = 2 THEN 'C'::text
                    ELSE '?'::text
                END) || '/'::text) || 
                CASE
                    WHEN tbl_nom_movimientos.porempleado = B'0'::"bit" THEN 'N'::text
                    ELSE 'E'::text
                END
           FROM tbl_nom_movimientos
          WHERE tbl_nom_movimientos.id_movimiento = m.id_movimiento) AS especial
   FROM tbl_nom_movimientos m
  WHERE m.id_movimiento < 0
  ORDER BY m.id_movimiento DESC;

ALTER TABLE view_catalog_movimientos_sis
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE tbl_nom_masemp_ptu
(
  ano smallint NOT NULL,
  id_empleado character(6) NOT NULL,
  salario_diario numeric(19,4) NOT NULL,
  sindicalizado bit(1) NOT NULL,
  dias numeric(5,2) NOT NULL,
  ixa numeric(5,2) NOT NULL,
  ixe numeric(5,2) NOT NULL,
  ixm numeric(5,2) NOT NULL,
  diastotales numeric(5,2) NOT NULL,
  sueldo numeric(19,4) NOT NULL,
  ptue numeric(19,4) NOT NULL,
  ptug numeric(19,4) NOT NULL,
  factordias numeric(10,8),
  factorsueldo numeric(10,8),
  ptudias numeric(19,4),
  ptusueldo numeric(19,4),
  CONSTRAINT pk_tbl_nom_masemp_ptu PRIMARY KEY (ano, id_empleado),
  CONSTRAINT fk_tbl_nom_masemp_ptu_tbl_nom_masemp FOREIGN KEY (id_empleado)
      REFERENCES tbl_nom_masemp (id_empleado) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE tbl_nom_masemp_ptu
  OWNER TO [[owner]];

--@FIN_BLOQUE
INSERT INTO tbl_variables(id_variable, descripcion, ventero, vdecimal, vfecha, valfanumerico, desistema, modulo)
VALUES('TBTC-PTU','DECIMAL|0|-|-|-', null, 0.00, null, '', '1', 'NOM');                                                        

INSERT INTO tbl_variables(id_variable, descripcion, ventero, vdecimal, vfecha, valfanumerico, desistema, modulo)
VALUES('VARPTU','DECIMAL|0|-|-|-', null, 0.00, null, '', '1', 'NOM');                                                        

INSERT INTO tbl_variables(id_variable, descripcion, ventero, vdecimal, vfecha, valfanumerico, desistema, modulo)
VALUES('VARPTU-DIA','DECIMAL|0|-|-|-', null, 0.00, null, '', '1', 'NOM');                                                        

INSERT INTO tbl_variables(id_variable, descripcion, ventero, vdecimal, vfecha, valfanumerico, desistema, modulo)
VALUES('BE-PTU','DECIMAL|0|-|-|-', null, 0.00, null, '', '1', 'NOM');                                                        

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_masemp_calculo_ptu(_ano smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
  _TotalPTU numeric(19,4); _TotalPTUDias numeric(19,4); _BaseExentaPTU numeric(19,4); _PTUG decimal(10,2); _PTUE decimal(10,2);
  _SumaSueldoTodos numeric(19,4); _SumaDiasTodos decimal(10,2); _PTUSalario numeric(19,4); _PTUDias numeric(19,4);
  _SalarioTotal decimal(19,4); _DiasTotales decimal(5,2); _PTUBase numeric(19,4); _TopeTrbCnfPTU numeric(19,4);
  _TTA RECORD;
BEGIN
    DELETE FROM TBL_NOM_MASEMP_PTU
    WHERE Ano = _Ano;
    
		-- Crea tabla de analisis por trabajador
		CREATE LOCAL TEMPORARY TABLE _TMP_ANALISIS (
      ID_Empleado char(6) NOT NULL ,
      Salario_Diario decimal(19, 4) NOT NULL ,
      Sindicalizado bit NOT NULL ,
      Dias decimal(5, 2) NOT NULL , 
      IXA decimal(5, 2) NOT NULL , 
      IXE decimal(5, 2) NOT NULL , 
      IXM decimal(5, 2) NOT NULL , 
      Sueldo decimal(19, 4) NOT NULL
    );
    
		_TopeTrbCnfPTU := (select VDecimal	from TBL_VARIABLES	-- LA CANTIDAD TOPE PARA TRABAJADORES DE CONFIANZA
												where ID_Variable = 'TBTC-PTU' );
    _TotalPTU := (	select VDecimal	from TBL_VARIABLES	-- LA CANTIDAD DE PTU POR SALARIO DE ESTA COMPAÑIA
										where ID_Variable = 'VARPTU' );
		_TotalPTUDias := (	select VDecimal	from TBL_VARIABLES	-- LA CANTIDAD DE PTU POR DIAS DE ESTA COMPAÑIA
												where ID_Variable = 'VARPTU-DIA' );
    _BaseExentaPTU := (	select VDecimal	from TBL_VARIABLES	-- LA CANTIDAD DE BASE EXENTA PARA PTU DE ESTA COMPAÑIA
												where ID_Variable = 'BE-PTU' );

		-- ahora inserta por empleado 
		INSERT INTO _TMP_ANALISIS 
		SELECT cne.ID_Empleado, me.Salario_Diario, me.Sindicalizado, sum(cne.Dias)+1, sum(cne.IXA), sum(cne.IXE), sum(cne.IXM), 
			case when me.Sindicalizado = '0' 
						then	case when (sum(cnd.Gravado) + sum(cnd.Exento)) > _TopeTrbCnfPTU
												then	_TopeTrbCnfPTU
												else	sum(cnd.Gravado) + sum(cnd.Exento)
									end				
						else
              sum(cnd.Gravado) + sum(cnd.Exento)
			end
		FROM TBL_NOM_CALCULO_NOMINA cn INNER JOIN TBL_NOM_CALCULO_NOMINA_ESP cne
			on cn.ID_Nomina = cne.ID_Nomina INNER JOIN TBL_NOM_CALCULO_NOMINA_DET cnd
			on cn.ID_Nomina = cnd.ID_Nomina and cne.ID_Empleado = cnd.ID_Empleado INNER JOIN TBL_NOM_MASEMP me
			on cne.ID_Empleado = me.ID_Empleado
		WHERE cn.Ano = _Ano and (cn.Tipo = 1 or cn.Tipo = 2) and 
			cnd.ID_Movimiento = 110 
		GROUP BY cne.ID_Empleado, me.Salario_Diario, me.Sindicalizado
		HAVING sum(cne.Dias) >= 60; --Trabajadores excluidos con menos de 60 Dias laborados. Puede considerarse para aplicarlo en una variable si es que cambia la ley

    _SumaSueldoTodos = (select sum(Sueldo) from _TMP_ANALISIS);
    _SumaDiasTodos = (select sum(Dias) + sum(IXA) + sum(IXM) from _TMP_ANALISIS);

		FOR _TTA IN ( select * from _TMP_ANALISIS )
    LOOP
				--SET _ID_Empleado = (select ID_Empleado from #TMP_ANALISIS where ID_Num = _contNum)
        _SalarioTotal := _TTA.Sueldo;
        _DiasTotales := _TTA.Dias + _TTA.IXA + _TTA.IXM;
        --///////////////////////////////////////////////////////////////////////////////
				--//		CALCULA EL PTU
        _PTUSalario := (_TotalPTU / 2) * (_SalarioTotal / _SumaSueldoTodos);
        _PTUDias := (_TotalPTU / 2) * (_DiasTotales / _SumaDiasTodos);
        _PTUBase := _PTUSalario + _PTUDias;

				IF _PTUBase is not null
        THEN
					IF _PTUBase > _BaseExentaPTU
					THEN
						_PTUG := _PTUBase - _BaseExentaPTU;
            _PTUE := _BaseExentaPTU;
					ELSE
            _PTUG := 0.0;
            _PTUE := _PTUBase;
          END IF;
        ELSE
          _PTUG := 0.0;
          _PTUE := 0.0;
				END IF;
				--raise notice 'Emp: %, SD: %, Sind: %, Dias: %, ixa: %, ixe: %, ixm: %, DT: %, Sueldo: %', _TTA.ID_Empleado, _TTA.Salario_Diario, _TTA.Sindicalizado, _TTA.Dias, _TTA.IXA, _TTA.IXE, _TTA.IXM, _DiasTotales, _TTA.Sueldo;
        --raise notice 'PTUE: %, PTUG: %, factordias: %, factorsueldo: %, ptudias: %, ptusueldo: %', _PTUE, _PTUG, round(_DiasTotales / _SumaDiasTodos,8), round(_SalarioTotal / _SumaSueldoTodos,8), _PTUDias, _PTUSalario;
        INSERT INTO TBL_NOM_MASEMP_PTU
				VALUES( _Ano, _TTA.ID_Empleado, _TTA.Salario_Diario, _TTA.Sindicalizado, _TTA.Dias, _TTA.IXA, _TTA.IXE, _TTA.IXM, _DiasTotales, _TTA.Sueldo,
                  _PTUE, _PTUG, (_DiasTotales / _SumaDiasTodos), (_SalarioTotal / _SumaSueldoTodos), _PTUDias, _PTUSalario );

          
		END LOOP; -- Ciclo WHILE de Analisis por empleado

    DROP TABLE _TMP_ANALISIS;	

    return query
    select 0, 'El PTU se calculó con éxito para el año mandado'::varchar, _Ano;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_masemp_calculo_ptu(smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP VIEW view_nom_isr;

ALTER TABLE tbl_nom_isr
   ALTER COLUMN porcentaje_exd TYPE numeric(10,6);
ALTER TABLE tbl_nom_isr_anualizado
   ALTER COLUMN porcentaje_exd TYPE numeric(10,6);
   
CREATE OR REPLACE VIEW view_nom_isr AS 
 SELECT n.id_isr, n.limite_inferior, n.limite_superior, n.cuota_fija, n.porcentaje_exd, n.subsidio, n.subsidio_sim, a.limite_inferior AS limite_inferior_anual, a.limite_superior AS limite_superior_anual, a.cuota_fija AS cuota_fija_anual, a.porcentaje_exd AS porcentaje_exd_anual, a.subsidio AS subsidio_anual, a.subsidio_sim AS subsidio_sim_anual
   FROM tbl_nom_isr n
   JOIN tbl_nom_isr_anualizado a ON n.id_isr = a.id_isr;

ALTER TABLE view_nom_isr
  OWNER TO [[owner]];
  
--@FIN_BLOQUE
CREATE TABLE tbl_sat_metodospago_cfdi
(
  clave character(2) NOT NULL,
  nombre character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_sat_metodospago_cfdi PRIMARY KEY (clave)
);
ALTER TABLE tbl_sat_metodospago_cfdi
  OWNER TO [[owner]];

--@FIN_BLOQUE
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('01','Efectivo');
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('02','Cheque nominativo');
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('03','Transferencia electrónica de fondos');
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('04','Tarjeta de crédito');
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('05','Monedero electrónico');
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('06','Dinero electrónico');
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('08','Vales de despensa');
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('28','Tarjeta de débito');
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('29','Tarjeta de servicio');
INSERT INTO tbl_sat_metodospago_cfdi
VALUES('99','Otros');

--@FIN_BLOQUE
UPDATE tbl_client_client
SET MetodoDePago = '01';

--@FIN_BLOQUE
CREATE TABLE tbl_sat_incoterms
(
  clave character(3) NOT NULL,
  descripcion character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_sat_incoterms PRIMARY KEY (clave)
);
ALTER TABLE tbl_sat_incoterms
  OWNER TO [[owner]];

--@FIN_BLOQUE
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('CFR','COSTE Y FLETE (PUERTO DE DESTINO CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('CIF','COSTE, SEGURO Y FLETE (PUERTO DE DESTINO CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('CPT','TRANSPORTE PAGADO HASTA (EL LUGAR DE DESTINO CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('CIP','TRANSPORTE Y SEGURO PAGADOS HASTA (LUGAR DE DESTINO CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('DAF','ENTREGADA EN FRONTERA (LUGAR CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('DAP','ENTREGADA EN LUGAR.');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('DAT','ENTREGADA EN TERMINAL.');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('DES','ENTREGADA SOBRE BUQUE (PUERTO DE DESTINO CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('DEQ','ENTREGADA EN MUELLE (PUERTO DE DESTINO CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('DDU','ENTREGADA DERECHOS NO PAGADOS (LUGAR DE DESTINO CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('DDP','ENTREGADA DERECHOS PAGADOS (LUGAR DE DESTINO CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('EXW','EN FABRICA (LUGAR CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('FCA','FRANCO TRANSPORTISTA (LUGAR DESIGNADO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('FAS','FRANCO AL COSTADO DEL BUQUE (PUERTO DE CARGA CONVENIDO).');
INSERT INTO tbl_sat_incoterms(clave, descripcion)
VALUES('FOB','FRANCO A BORDO (PUERTO DE CARGA CONVENIDO).');

--@FIN_BLOQUE
CREATE TABLE tbl_sat_unidades
(
  clave smallint NOT NULL,
  descripcion character varying(50) NOT NULL,
  CONSTRAINT pk_tbl_sat_unidades PRIMARY KEY (clave)
);
ALTER TABLE tbl_sat_unidades
  OWNER TO [[owner]];

--@FIN_BLOQUE
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('1','KILO');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('2','GRAMO');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('3','METRO LINEAL');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('4','METRO CUADRADO');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('5','METRO CUBICO');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('6','PIEZA');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('7','CABEZA');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('8','LITRO');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('9','PAR');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('10','KILOWATT');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('11','MILLAR');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('12','JUEGO');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('13','KILOWATT/HORA');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('14','TONELADA');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('15','BARRIL');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('16','GRAMO NETO');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('17','DECENAS');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('18','CIENTOS');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('19','DOCENAS');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('20','CAJA');  
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('21','BOTELLA');
INSERT INTO tbl_sat_unidades(clave, descripcion)
VALUES('99','OTROS (SERVICIO)');

--@FIN_BLOQUE
UPDATE tbl_cfd_expediciones
SET cfd_pais = 'MEX';

--@FIN_BLOQUE
UPDATE tbl_provee_provee
SET pais = 'MEX';

--@FIN_BLOQUE
UPDATE tbl_client_client
SET pais = 'MEX';

--@FIN_BLOQUE
ALTER TABLE abstbl_proveeclient
ADD registro_tributario character varying(40);

UPDATE tbl_provee_provee
SET registro_tributario = rfc;

UPDATE tbl_client_client
SET registro_tributario = rfc;

ALTER TABLE abstbl_proveeclient
ALTER COLUMN registro_tributario SET NOT NULL;

--@FIN_BLOQUE
ALTER TABLE abstbl_proveeclient
ADD pedimento character varying(5);

UPDATE tbl_provee_provee
SET pedimento = '--';

UPDATE tbl_client_client
SET pedimento = '--';

ALTER TABLE abstbl_proveeclient
ALTER COLUMN pedimento SET NOT NULL;

--@FIN_BLOQUE
DROP VIEW view_provee_provee_mas;

CREATE OR REPLACE VIEW view_provee_provee_mas AS 
 SELECT p.id_tipo, p.id_clave, p.id_entidad AS id_entidadcompra, ( SELECT tbl_compras_entidades.descripcion
           FROM tbl_compras_entidades
          WHERE tbl_compras_entidades.id_entidadcompra = p.id_entidad) AS entidadnombre, 
        CASE
            WHEN p.pais::text = 'MEX'::text THEN p.rfc
            ELSE p.registro_tributario
        END AS rfc, p.atnpagos, p.colonia, p.cp, p.direccion, p.fax, p.poblacion, p.compraanual, p.descuento, p.limitecredito, p.ultimacompra, p.obs, p.precioespmostr, ( SELECT tbl_cont_catalogo.nombre
           FROM tbl_cont_catalogo
          WHERE tbl_cont_catalogo.cuenta = p.id_cc) AS cuentanombre, p.id_vendedor, ''::character varying(80) AS vendedornombre, p.noext, p.noint, p.municipio, p.estado, p.pais, p.metododepago, p.status, p.id_satbanco, p.pedimento
   FROM tbl_provee_provee p;

ALTER TABLE view_provee_provee_mas
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP VIEW view_client_client_mas;

CREATE OR REPLACE VIEW view_client_client_mas AS 
 SELECT p.id_tipo, p.id_clave, p.id_entidad AS id_entidadventa, ( SELECT tbl_ventas_entidades.descripcion
           FROM tbl_ventas_entidades
          WHERE tbl_ventas_entidades.id_entidadventa = p.id_entidad) AS entidadnombre, 
        CASE
            WHEN p.pais::text = 'MEX'::text THEN p.rfc
            ELSE p.registro_tributario
        END AS rfc, p.atnpagos, p.colonia, p.cp, p.direccion, p.fax, p.poblacion, p.compraanual, p.descuento, p.limitecredito, p.ultimacompra, p.obs, p.precioespmostr, ( SELECT tbl_cont_catalogo.nombre
           FROM tbl_cont_catalogo
          WHERE tbl_cont_catalogo.cuenta = p.id_cc) AS cuentanombre, p.id_vendedor, v.nombre AS vendedornombre, p.noext, p.noint, p.municipio, p.estado, p.pais, p.metododepago, p.status, p.id_satbanco, p.pedimento
   FROM tbl_client_client p
   JOIN tbl_vendedores v ON p.id_vendedor = v.id_vendedor;

ALTER TABLE view_client_client_mas
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_provee_provee_agregar(character, integer, character, character, character varying, numeric, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, numeric, numeric, smallint, numeric, timestamp without time zone, character varying, bit, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character, character, smallint);

CREATE OR REPLACE FUNCTION sp_provee_provee_agregar(
    _id_tipo character,
    _id_numero integer,
    _id_cc character,
    _id_cc_comp character,
    _nombre character varying,
    _saldo numeric,
    _id_entidadcompra smallint,
    _rfc character varying,
    _atncompras character varying,
    _atnpagos character varying,
    _colonia character varying,
    _cp character varying,
    _direccion character varying,
    _email character varying,
    _fax character varying,
    _poblacion character varying,
    _tel character varying,
    _compraanual numeric,
    _descuento numeric,
    _dias smallint,
    _limitecredito numeric,
    _ultimacompra timestamp without time zone,
    _obs character varying,
    _precioespmostr bit,
    _id_vendedor smallint,
    _noext character varying,
    _noint character varying,
    _municipio character varying,
    _estado character varying,
    _pais character varying,
    _metododepago character varying,
    _status character,
    _id_satbanco character,
    _smtp smallint,
    _registro_tributario character varying,
    _pedimento character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _ID_Clave int; _result varchar(255);
BEGIN 
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and 
			m.mod::text = 'COMP_PROVEE' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'El cliente se agreg&oacute; correctamente';
	_ID_Clave := ( select Max(ID_Clave) from TBL_PROVEE_PROVEE where ID_Tipo = _ID_Tipo ) + 1;
	IF _ID_Clave is null THEN _ID_Clave = 1; END IF;
	 
	IF (select count(*) from TBL_PROVEE_PROVEE where ID_Tipo = _ID_Tipo and ID_Entidad = _ID_EntidadCompra and ID_Numero = _ID_Numero) > 0
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and 
			m.mod::text = 'COMP_PROVEE' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La clave del PROVEEe ya existe en otro registro';
	END IF;
	
	IF _err = 0
	THEN	
		INSERT INTO TBL_PROVEE_PROVEE
		VALUES(_ID_Tipo, _ID_Clave, _ID_Numero, _ID_CC, _ID_CC_Comp, _Nombre, _Saldo, _ID_EntidadCompra, _RFC, _AtnCompras, _AtnPagos, _Colonia, _CP, _Direccion, _EMail, 
			_Fax, _Poblacion, _Tel, _CompraAnual, _Descuento, _Dias, _LimiteCredito, _UltimaCompra, _Obs, _PrecioEspMostr, _ID_Vendedor,
			_NoExt, _NoInt, _Municipio, _Estado, _Pais, _MetodoDePago, _Status, _ID_SatBanco, _smtp, _registro_tributario, _pedimento);

		INSERT INTO TBL_PROVEE_SALDOS_MONEDAS
		SELECT Clave, _ID_Tipo, _ID_Clave, 0.00
		FROM TBL_CONT_MONEDAS;
		
		-- inserta en los saldos mensuales de proveedores
		INSERT INTO TBL_PROVEE_SALDOS
		SELECT Mes, Ano, Clave, _ID_Tipo, _ID_Clave, 0.00, 0.00
		FROM TBL_CONT_CATALOGO_PERIODOS, TBL_CONT_MONEDAS
		WHERE Mes <> 13;
			
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_clave;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_provee_provee_agregar(character, integer, character, character, character varying, numeric, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, numeric, numeric, smallint, numeric, timestamp without time zone, character varying, bit, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character, character, smallint, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_provee_provee_cambiar(character, integer, character, character, character varying, numeric, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, numeric, numeric, smallint, numeric, timestamp without time zone, character varying, bit, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character, character, smallint);

CREATE OR REPLACE FUNCTION sp_provee_provee_cambiar(
    _id_tipo character,
    _id_clave integer,
    _id_cc character,
    _id_cc_comp character,
    _nombre character varying,
    _saldo numeric,
    _id_entidadcompra smallint,
    _rfc character varying,
    _atncompras character varying,
    _atnpagos character varying,
    _colonia character varying,
    _cp character varying,
    _direccion character varying,
    _email character varying,
    _fax character varying,
    _poblacion character varying,
    _tel character varying,
    _compraanual numeric,
    _descuento numeric,
    _dias smallint,
    _limitecredito numeric,
    _ultimacompra timestamp without time zone,
    _obs character varying,
    _precioespmostr bit,
    _id_vendedor smallint,
    _noext character varying,
    _noint character varying,
    _municipio character varying,
    _estado character varying,
    _pais character varying,
    _metododepago character varying,
    _status character,
    _id_satbanco character,
    _smtp smallint,
    _registro_tributario character varying,
    _pedimento character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);  
BEGIN 
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and 
			m.mod::text = 'COMP_PROVEE' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); 
	
	IF (select count(*) from TBL_PROVEE_PROVEE where ID_Tipo = _ID_Tipo and ID_Clave = _ID_Clave) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and 
			m.mod::text = 'COMP_PROVEE' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); 
	END IF;
	
	IF _err = 0
	THEN	
		UPDATE TBL_PROVEE_PROVEE
		SET ID_CC = _ID_CC, ID_CC_Comp = _ID_CC_Comp, Nombre = _Nombre, ID_Entidad = _ID_EntidadCompra, RFC = _RFC, AtnCompras = _AtnCompras, 
			AtnPagos = _AtnPagos, Colonia = _Colonia, CP = _CP, Direccion = _Direccion, EMail = _EMail, Fax = _Fax, Poblacion = _Poblacion, Tel = _Tel, 
			CompraAnual = _CompraAnual, Descuento = _Descuento, Dias = _Dias, LimiteCredito = _LimiteCredito, UltimaCompra = _UltimaCompra, Obs = _Obs, PrecioEspMostr = _PrecioEspMostr, ID_Vendedor = _ID_Vendedor,
			NoExt = _NoExt, NoInt = _NoInt, Municipio = _Municipio, Estado = _Estado, Pais = _Pais, MetodoDePago = _MetodoDePago, Status = _Status, ID_SatBanco = _ID_SatBanco, smtp = _smtp, 
			registro_tributario = _registro_tributario, pedimento = _pedimento
		WHERE ID_Tipo = _ID_Tipo and ID_Clave = _ID_Clave;
			
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_clave;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_provee_provee_cambiar(character, integer, character, character, character varying, numeric, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, numeric, numeric, smallint, numeric, timestamp without time zone, character varying, bit, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character, character, smallint, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_client_client_agregar(character, integer, character, character, character varying, numeric, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, numeric, numeric, smallint, numeric, timestamp without time zone, character varying, bit, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character, character, smallint);

CREATE OR REPLACE FUNCTION sp_client_client_agregar(
    _id_tipo character,
    _id_numero integer,
    _id_cc character,
    _id_cc_comp character,
    _nombre character varying,
    _saldo numeric,
    _id_entidadventa smallint,
    _rfc character varying,
    _atncompras character varying,
    _atnpagos character varying,
    _colonia character varying,
    _cp character varying,
    _direccion character varying,
    _email character varying,
    _fax character varying,
    _poblacion character varying,
    _tel character varying,
    _compraanual numeric,
    _descuento numeric,
    _dias smallint,
    _limitecredito numeric,
    _ultimacompra timestamp without time zone,
    _obs character varying,
    _precioespmostr bit,
    _id_vendedor smallint,
    _noext character varying,
    _noint character varying,
    _municipio character varying,
    _estado character varying,
    _pais character varying,
    _metododepago character varying,
    _status character,
    _id_satbanco character,
    _smtp smallint,
    _registro_tributario character varying,
    _pedimento character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _ID_Clave int; _result varchar(255);
BEGIN 
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and 
			m.mod::text = 'VEN_CLIENT' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'El cliente se agreg&oacute; correctamente';
	_ID_Clave := ( select Max(ID_Clave) from TBL_CLIENT_CLIENT where ID_Tipo = _ID_Tipo ) + 1;
	IF _ID_Clave is null THEN _ID_Clave = 1; END IF;
	 
	IF (select count(*) from TBL_CLIENT_CLIENT where ID_Tipo = _ID_Tipo and ID_Entidad = _ID_EntidadVenta and ID_Numero = _ID_Numero) > 0
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and 
			m.mod::text = 'VEN_CLIENT' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La clave del cliente ya existe en otro registro';
	END IF;
	
	IF _err = 0
	THEN	
		INSERT INTO TBL_CLIENT_CLIENT
		VALUES(_ID_Tipo, _ID_Clave, _ID_Numero, _ID_CC, _ID_CC_Comp, _Nombre, _Saldo, _ID_EntidadVenta, _RFC, _AtnCompras, _AtnPagos, _Colonia, _CP, _Direccion, _EMail, 
			_Fax, _Poblacion, _Tel, _CompraAnual, _Descuento, _Dias, _LimiteCredito, _UltimaCompra, _Obs, _PrecioEspMostr, _ID_Vendedor,
			_NoExt, _NoInt, _Municipio, _Estado, _Pais, _MetodoDePago, _Status, _ID_SatBanco, _smtp, _registro_tributario, _pedimento);

		INSERT INTO TBL_CLIENT_SALDOS_MONEDAS
		SELECT Clave, _ID_Tipo, _ID_Clave, 0.00
		FROM TBL_CONT_MONEDAS;
		
		-- inserta en los saldos mensuales de clientes
		INSERT INTO TBL_CLIENT_SALDOS
		SELECT Mes, Ano, Clave, _ID_Tipo, _ID_Clave, 0.00, 0.00
		FROM TBL_CONT_CATALOGO_PERIODOS, TBL_CONT_MONEDAS
		WHERE Mes <> 13;
		
			
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_clave;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_client_client_agregar(character, integer, character, character, character varying, numeric, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, numeric, numeric, smallint, numeric, timestamp without time zone, character varying, bit, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character, character, smallint, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_client_client_cambiar(character, integer, character, character, character varying, numeric, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, numeric, numeric, smallint, numeric, timestamp without time zone, character varying, bit, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character, character, smallint);

CREATE OR REPLACE FUNCTION sp_client_client_cambiar(
    _id_tipo character,
    _id_clave integer,
    _id_cc character,
    _id_cc_comp character,
    _nombre character varying,
    _saldo numeric,
    _id_entidadventa smallint,
    _rfc character varying,
    _atncompras character varying,
    _atnpagos character varying,
    _colonia character varying,
    _cp character varying,
    _direccion character varying,
    _email character varying,
    _fax character varying,
    _poblacion character varying,
    _tel character varying,
    _compraanual numeric,
    _descuento numeric,
    _dias smallint,
    _limitecredito numeric,
    _ultimacompra timestamp without time zone,
    _obs character varying,
    _precioespmostr bit,
    _id_vendedor smallint,
    _noext character varying,
    _noint character varying,
    _municipio character varying,
    _estado character varying,
    _pais character varying,
    _metododepago character varying,
    _status character,
    _id_satbanco character,
    _smtp smallint,
    _registro_tributario character varying,
    _pedimento character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); 
BEGIN 
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and 
			m.mod::text = 'VEN_CLIENT' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'El cliente se agreg&oacute; correctamente';
	
	IF (select count(*) from TBL_CLIENT_CLIENT where ID_Tipo = _ID_Tipo and ID_Clave = _ID_Clave) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and 
			m.mod::text = 'VEN_CLIENT' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La clave del cliente ya existe en otro registro';
	END IF;
	
	IF _err = 0
	THEN	
		UPDATE TBL_CLIENT_CLIENT
		SET ID_CC = _ID_CC, ID_CC_Comp = _ID_CC_Comp, Nombre = _Nombre, ID_Entidad = _ID_EntidadVenta, RFC = _RFC, AtnCompras = _AtnCompras, 
			AtnPagos = _AtnPagos, Colonia = _Colonia, CP = _CP, Direccion = _Direccion, EMail = _EMail, Fax = _Fax, Poblacion = _Poblacion, Tel = _Tel, 
			CompraAnual = _CompraAnual, Descuento = _Descuento, Dias = _Dias, LimiteCredito = _LimiteCredito, UltimaCompra = _UltimaCompra, Obs = _Obs, PrecioEspMostr = _PrecioEspMostr, ID_Vendedor = _ID_Vendedor,
			NoExt = _NoExt, NoInt = _NoInt, Municipio = _Municipio, Estado = _Estado, Pais = _Pais, MetodoDePago = _MetodoDePago, Status = _Status, ID_SatBanco = _ID_SatBanco, smtp = _smtp, 
			registro_tributario = _registro_tributario, pedimento = _pedimento
		WHERE ID_Tipo = _ID_Tipo and ID_Clave = _ID_Clave;
			
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_clave;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_client_client_cambiar(character, integer, character, character, character varying, numeric, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, numeric, numeric, smallint, numeric, timestamp without time zone, character varying, bit, smallint, character varying, character varying, character varying, character varying, character varying, character varying, character, character, smallint, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE abstbl_vencomp_comext_cab
(
  id_vc integer NOT NULL,
  tipooperacion character(1) NOT NULL,
  clavedepedimento character varying(5) NOT NULL,
  certificadoorigen smallint NOT NULL,
  numcertificadoorigen character varying(40) NOT NULL,
  numeroexportadorconfiable character varying(50) NOT NULL,
  incoterm character varying(3) NOT NULL,
  subdivision smallint NOT NULL,
  observaciones character varying(300) NOT NULL,
  tipocambiousd numeric(19,4) NOT NULL,
  totalusd numeric(19,4) NOT NULL,
  emisor_curp character varying(40) NOT NULL,
  receptor_curp character varying(40) NOT NULL,
  receptor_numregidtrib character varying(40) NOT NULL,
  destinatario_numregidtrib character varying(40) NOT NULL,
  destinatario_rfc character varying(15) NOT NULL,
  destinatario_curp character varying(40) NOT NULL,
  destinatario_nombre character varying(300) NOT NULL,
  destinatario_domicilio_calle character varying(100) NOT NULL,
  destinatario_domicilio_numeroexterior character varying(55) NOT NULL,
  destinatario_domicilio_numerointerior character varying(55) NOT NULL,
  destinatario_domicilio_colonia character varying(120) NOT NULL,
  destinatario_domicilio_localidad character varying(120) NOT NULL,
  destinatario_domicilio_referencia character varying(250) NOT NULL,
  destinatario_domicilio_municipio character varying(120) NOT NULL,
  destinatario_domicilio_estado character varying(30) NOT NULL,
  destinatario_domicilio_pais character(3) NOT NULL,
  destinatario_domicilio_codigopostal character varying(12) NOT NULL
);
ALTER TABLE abstbl_vencomp_comext_cab
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE abstbl_vencomp_comext_det
(
  id_vc integer NOT NULL,
  partida smallint NOT NULL,
  noidentificacion character varying(100) NOT NULL,
  fraccionarancelaria character varying(12) NOT NULL,
  cantidadaduana numeric(9,3) NOT NULL,
  unidadaduana smallint NOT NULL,
  valorunitarioaduana numeric(19,2) NOT NULL,
  valordolares numeric(19,2) NOT NULL
);
ALTER TABLE abstbl_vencomp_comext_det
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE abstbl_vencomp_comext_det_descesp
(
  id_vc integer NOT NULL,
  partida smallint NOT NULL,
  descripcion smallint NOT NULL,
  marca character varying(35) NOT NULL,
  modelo character varying(80) NOT NULL,
  submodelo character varying(50) NOT NULL,
  numeroserie character varying(40) NOT NULL
);
ALTER TABLE abstbl_vencomp_comext_det_descesp
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE tbl_compras_facturas_comext_cab
(
  id_vc integer NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  tipooperacion character(1) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  clavedepedimento character varying(5) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  certificadoorigen smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  numcertificadoorigen character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  numeroexportadorconfiable character varying(50) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  incoterm character varying(3) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  subdivision smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  observaciones character varying(300) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  tipocambiousd numeric(19,4) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  totalusd numeric(19,4) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  emisor_curp character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  receptor_curp character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  receptor_numregidtrib character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_numregidtrib character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_rfc character varying(15) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_curp character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_nombre character varying(300) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_calle character varying(100) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_numeroexterior character varying(55) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_numerointerior character varying(55) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_colonia character varying(120) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_localidad character varying(120) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_referencia character varying(250) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_municipio character varying(120) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_estado character varying(30) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_pais character(3) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_codigopostal character varying(12) NOT NULL,
  CONSTRAINT pk_tbl_compras_facturas_comext_cab PRIMARY KEY (id_vc),
  CONSTRAINT fk_tbl_compras_facturas_comext_cab_tbl_compras_facturas_cab FOREIGN KEY (id_vc)
      REFERENCES tbl_compras_facturas_cab (id_vc) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
INHERITS (abstbl_vencomp_comext_cab);
ALTER TABLE tbl_compras_facturas_comext_cab
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE tbl_compras_facturas_comext_det
(
  id_vc integer NOT NULL,
  partida smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  noidentificacion character varying(100) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  fraccionarancelaria character varying(12) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  cantidadaduana numeric(9,3) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  unidadaduana smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  valorunitarioaduana numeric(19,2) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  valordolares numeric(19,2) NOT NULL,
  CONSTRAINT pk_tbl_compras_facturas_comext_det PRIMARY KEY (id_vc, partida),
  CONSTRAINT fk_tbl_compras_facturas_comext_det_tbl_compras_facturas_comext_ FOREIGN KEY (id_vc)
      REFERENCES tbl_compras_facturas_comext_cab (id_vc) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
INHERITS (abstbl_vencomp_comext_det);
ALTER TABLE tbl_compras_facturas_comext_det
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE tbl_compras_facturas_comext_det_descesp
(
  id_vc integer NOT NULL,
  partida smallint NOT NULL,
  descripcion smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det_descesp:  marca character varying(35) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det_descesp:  modelo character varying(80) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det_descesp:  submodelo character varying(50) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det_descesp:  numeroserie character varying(40) NOT NULL,
  CONSTRAINT pk_tbl_compras_facturas_comext_det_descesp PRIMARY KEY (id_vc, partida, descripcion),
  CONSTRAINT fk_tbl_compras_facturas_comext_det_descesp_tbl_compras_facturas FOREIGN KEY (id_vc, partida)
      REFERENCES tbl_compras_facturas_comext_det (id_vc, partida) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
INHERITS (abstbl_vencomp_comext_det_descesp);
ALTER TABLE tbl_compras_facturas_comext_det_descesp
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE tbl_ventas_facturas_comext_cab
(
  id_vc integer NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  tipooperacion character(1) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  clavedepedimento character varying(5) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  certificadoorigen smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  numcertificadoorigen character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  numeroexportadorconfiable character varying(50) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  incoterm character varying(3) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  subdivision smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  observaciones character varying(300) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  tipocambiousd numeric(19,4) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  totalusd numeric(19,4) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  emisor_curp character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  receptor_curp character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  receptor_numregidtrib character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_numregidtrib character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_rfc character varying(15) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_curp character varying(40) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_nombre character varying(300) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_calle character varying(100) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_numeroexterior character varying(55) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_numerointerior character varying(55) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_colonia character varying(120) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_localidad character varying(120) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_referencia character varying(250) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_municipio character varying(120) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_estado character varying(30) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_pais character(3) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_cab:  destinatario_domicilio_codigopostal character varying(12) NOT NULL,
  CONSTRAINT pk_tbl_ventas_facturas_comext_cab PRIMARY KEY (id_vc),
  CONSTRAINT fk_tbl_ventas_facturas_comext_cab_tbl_ventas_facturas_cab FOREIGN KEY (id_vc)
      REFERENCES tbl_ventas_facturas_cab (id_vc) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
INHERITS (abstbl_vencomp_comext_cab);
ALTER TABLE tbl_ventas_facturas_comext_cab
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE tbl_ventas_facturas_comext_det
(
  id_vc integer NOT NULL,
  partida smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  noidentificacion character varying(100) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  fraccionarancelaria character varying(12) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  cantidadaduana numeric(9,3) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  unidadaduana smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  valorunitarioaduana numeric(19,2) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det:  valordolares numeric(19,2) NOT NULL,
  CONSTRAINT pk_tbl_ventas_facturas_comext_det PRIMARY KEY (id_vc, partida),
  CONSTRAINT fk_tbl_ventas_facturas_comext_det_tbl_ventas_facturas_comext_ca FOREIGN KEY (id_vc)
      REFERENCES tbl_ventas_facturas_comext_cab (id_vc) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
INHERITS (abstbl_vencomp_comext_det);
ALTER TABLE tbl_ventas_facturas_comext_det
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE tbl_ventas_facturas_comext_det_descesp
(
  id_vc integer NOT NULL,
  partida smallint NOT NULL,
  descripcion smallint NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det_descesp:  marca character varying(35) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det_descesp:  modelo character varying(80) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det_descesp:  submodelo character varying(50) NOT NULL,
-- Inherited from table abstbl_vencomp_comext_det_descesp:  numeroserie character varying(40) NOT NULL,
  CONSTRAINT pk_tbl_ventas_facturas_comext_det_descesp PRIMARY KEY (id_vc, partida, descripcion),
  CONSTRAINT fk_tbl_ventas_facturas_comext_det_descesp_tbl_ventas_facturas_c FOREIGN KEY (id_vc, partida)
      REFERENCES tbl_ventas_facturas_comext_det (id_vc, partida) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
INHERITS (abstbl_vencomp_comext_det_descesp);
ALTER TABLE tbl_ventas_facturas_comext_det_descesp
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_compras_facturas_agregar(
    _id_entidadcompra smallint,
    _numero integer,
    _id_proveedor integer,
    _fecha timestamp without time zone,
    _referencia character varying,
    _moneda smallint,
    _tc numeric,
    _condicion smallint,
    _obs character varying,
    _importe numeric,
    _descuento numeric,
    _subtotal numeric,
    _iva numeric,
    _total numeric,
    _fsipg_efectivo numeric,
    _fsipg_bancos numeric,
    _fsipg_cambio numeric,
    _id_bodega smallint,
    _id_enlace integer,
    _id_vendedor smallint,
    _tipoenlace character,
    _uuid character,
    _ieps numeric,
    _ivaret numeric,
    _isrret numeric)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_Factura int; _mes smallint; _ano smallint;	_errpart int;  _resultpart varchar(255);  _ID_Movimiento int;  _concepto varchar(80); _conceptocost varchar(80); _Ref varchar(25);  _claseref varchar(25); 
	_FijaCost bit; _costcomp numeric(19,4); _CC_COSTCOMP char(19); _AuditarAlm bit;
	_ID_CXP int; _numpol int; _clase varchar(1024); _bancajmov int;
	_nombrePro varchar(120); _Cantidad numeric(19,4); _ServComp bit; _CC_PRO char(19); 
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_COMPNP char(19); _CC_DCAF char(19);
	_CC_DCEC char(19); _diff numeric(19,4); _TotDebe numeric(19,4); _TotHaber numeric(19,4); _contPart smallint; 
	_totPart smallint; _CC_COMP char(19); _CC_COMCONT char(19); _tipoRetiro varchar(10); _Beneficiario varchar(80); _Pais character(3); _Pedimento varchar(5);
	 _contban smallint; _totalban smallint; _ID_FormaPago smallint; _ID_BanCaj smallint; _RefPago varchar(25); _banCHQ varchar(20); _IdMon smallint; _RecepcionAsociada bit; 
	_id_clasificacion varchar(10); _numPagos smallint; _RefExt int; _ID_CFD int; _TFD smallint;  
	_RFC varchar(15); _RFCBeneficiario varchar(15); _moneda_ce character(3); /*  _CuentaBeneficiario varchar(80); _BancoBeneficiario character(3); _CuentaOrigen varchar(50); _BancoOrigen character(3);  */
	--Iteracion
	_REC_TMP_PAGOS RECORD; _REC_TMP_COMPRAS_FACTURAS_DET RECORD; _TotalPesosMult numeric(19,4); _ParcialPesosMult numeric(19,4); _BanCaj smallint; _CC_BAN char(19);
BEGIN	
	_err := 0;
	_result := 'La factura de compra se ha registrado satisfactoriamente';
	--_ServComp := '1';
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAAC');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAACPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSC');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSCPN');
	_CC_IVARet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVARETC');
	_CC_ISRRet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_ISRREC');
	_CC_Desc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSC');
	_moneda_ce := (select id_satmoneda from TBL_CONT_MONEDAS where Clave = _moneda);
	_Cantidad := round((_Total * _TC), 2);
	_ImporteTotalPesos := ROUND((_Importe * _TC), 2);
	_IVAPesos :=  ROUND((_IVA * _TC), 2);
	_IEPSPesos :=  ROUND((_IEPS * _TC), 2);
	_IVARetPesos :=  ROUND((_IVARet * _TC), 2);
	_ISRRetPesos :=  ROUND((_ISRRet * _TC), 2);
	_DescPesos := ROUND((_Descuento * _TC), 2);
	_TotalPesos := ROUND((_Total * _TC), 2);	
	_concepto := 'Compra ' || (select Descripcion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_conceptocost := 'Costo compra ' || (select Descripcion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_Beneficiario := case when _ID_Proveedor = 0 then 'Al Portador' else ( select Nombre from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
  _Pais := case when _ID_Proveedor = 0 then 'MEX' else ( select Pais from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
  _Pedimento := case when _ID_Proveedor = 0 then '--' else ( select Pedimento from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end; 
  _RFCBeneficiario := case when _ID_Proveedor = 0 then 'XAXX010101000' else ( select RFC from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	--_CuentaBeneficiario := case when _ID_Proveedor = 0 then '' else ( select MetodoDePago from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	--_BancoBeneficiario := case when _ID_Proveedor = 0 then '000' else ( select ID_SatBanco from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) end;
	_CC_PRO := (select ID_CC from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor);
	_Fija := (select Fija from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	_FijaCost := (select FijaCost from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	--_FijaBAN := case when _ID_FormaPago = 0 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
		--				else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_AuditarAlm := ( select AuditarAlm from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega );
	_RecepcionAsociada := ( case when _ID_Enlace is not null and _TipoEnlace = 'CREC' then '1' else '0' end );
	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_id_clasificacion := (select ID_Clasificacion from TBL_COMPRAS_ENTIDADES where ID_EntidadCompra = _ID_EntidadCompra);
	_numPagos = (select count(*) from _TMP_PAGOS);

	IF _ID_Enlace is not null
	THEN
		_claseref := _TipoEnlace || '|' || cast(_ID_Enlace as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';
	END IF;
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'COMP_CXP' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;

	IF (select count(*) from TBL_COMPRAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadCompra and Numero = _Numero) > 0
	THEN
		_err := 3;
		_result := 'ERROR: La clave de la factura ya existe, No se puede duplicar';
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF _Condicion = 0 
	THEN
		FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
		LOOP
			_ID_FormaPago := _REC_TMP_PAGOS.ID_FormaPago;
			_ID_BanCaj := _REC_TMP_PAGOS.ID_BanCaj;
			_FijaBAN := (case when _ID_FormaPago = 1 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			_IdMon := (case when _ID_FormaPago = 1 then (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			
			IF _Fija <> _FijaBAN
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra porque al pagarse de contado requiere que los bancos o cajas manejen los mismos trazos contables que la compra';
				EXIT;
			END IF;

			IF _Moneda <> _IdMon 
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra porque al pagarse de contado requiere que los bancos o cajas manejen la misma moneda que la compra';
				EXIT;
			END IF;
			/*
			IF _ID_Proveedor = 0 and _Fija = '0' and _ID_FormaPago > 0
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar una compra directa de mostrador pagada de contado con cheque o transferencia, porque no existe soporte para contabilidad electrónica, selecciona un proveedor o una entidad Fija';
				EXIT;
			END IF; */
		END LOOP;
	ELSIF _Condicion = 1 -- Es a crédito, por lo tanto revisa si el total de esta factura mas el total de la deuda con el proveedor rebasa el límite de crédito, de ser asi, rechazará la compra
	THEN
		IF(select Dias from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) > 0 -- tenemos credito con este proveedor
		THEN
			IF (select LimiteCredito from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor) > 0 AND 
				((_ImporteTotalPesos + getcliprosldtotmn('PR', _ID_Proveedor)) > (select LimiteCredito from TBL_PROVEE_PROVEE where ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor))
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la compra a crédito porque la suma de nuestras deudas con este proveedor, más esta compra, sobrepasa nuestro límite de crédito';
			END IF;
		ELSE -- no tenemos crédito
			_err := 3;
			_result := 'ERROR: No se puede agregar la compra porque al pagarse a crédito requiere que el proveedor nos brinde por lo menosu un día de crédito';
		END IF;
	END IF;
	
	IF _IVA > 0.0 AND ( _CC_IVA = '' OR _CC_IVAPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva acreditable efectivamente pagado o la de iva acreditable pendiente de pagar no existe o no se ha enlazado';
	END IF;
	IF _IEPS > 0.0 AND ( _CC_IEPS = '' OR _CC_IEPSPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente pagado o la de IEPS pendiente de pagar no existe o no se ha enlazado';
	END IF;
	IF _IVARet > 0.0 AND ( _CC_IVARet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de IVA Retenido no existe o no se ha enlazado';
	END IF;
	IF _ISRRet > 0.0 AND ( _CC_ISRRet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de ISR Retenido no existe o no se ha enlazado';
	END IF;
	IF _Descuento > 0.0 AND _CC_Desc = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de descuento sobre compras no existe o no se ha enlazado';
	END IF;
	
	-- procede a realizar verificaci?n sobre los trazos
	IF _FijaCost = '0'
	THEN
		_CC_COSTCOMP := (select cc from TBL_INVSERV_COSTOS_CONCEPTOS where ID_Concepto = 1);
		IF _CC_COSTCOMP is null
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para el costo de compras no existe o no se ha enlazado';	
		END IF;
	END IF;

	IF _Condicion = 0
	THEN
		_CC_COMCONT := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMCONT');
		IF _CC_COMCONT = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para compras de contado no existe o no se ha enlazado';	
		END IF;
	ELSE 
		_CC_COMP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMP');
		IF _CC_COMP = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para compras a crédito no existe o no se ha enlazado';	
		END IF;
	END IF;

	IF _Condicion = 3
	THEN
		_CC_COMPNP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_COMPNP');
		IF _CC_COMPNP = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para registro de partida doble cuando no se establece ningun metodo de pago, no existe o no se ha enlazado';
		END IF;
	END IF;
	
	--VERIFICA SI ES CFDI Asociada
	IF _uuid <> ''
	THEN
		IF (select count(*) from TBL_CFDCOMP where UUID = _uuid) > 0
		THEN
			_ID_CFD := (select ID_CFD from TBL_CFDCOMP where UUID = _uuid) ;
			_TFD := 3;
		ELSE
			_err := 3;
			_result := 'ERROR: No se puede enlazar el CFDI al registro, porque no existe el UUID proporcionado';
		END IF;
	END IF;
	
	IF _err = 0
	THEN
		INSERT INTO TBL_COMPRAS_FACTURAS_CAB(  id_entidad, numero, id_clipro, fecha, referencia, status, moneda, tc, fechaenvio, condicion, obs, 
			importe, descuento, subtotal, iva, total, ref, id_pol, id_polcost, id_bodega, mimporte, mdescuento, msubtotal, miva, mtotal, efectivo, bancos, cambio, id_vendedor, id_cfd, tfd, 
			ieps, ivaret, isrret )
		VALUES(_ID_EntidadCompra, _Numero, _ID_Proveedor, _Fecha, _Referencia, (case when _RecepcionAsociada = '0' then (case when _AuditarAlm = '1' then 'G' else 'E' end) else 'E' end), _Moneda, _TC, _Fecha, _Condicion, _Obs,
				_Importe, _Descuento, _SubTotal, _IVA, _Total, _claseref, null, null, _ID_Bodega, _Importe, _Descuento, _SubTotal, _IVA,  _Total, _FSIPG_Efectivo, _FSIPG_Bancos, _FSIPG_Cambio, _ID_Vendedor, _ID_CFD, _TFD,
				_ieps, _ivaret, _isrret)
		 RETURNING currval(pg_get_serial_sequence('TBL_COMPRAS_FACTURAS_CAB', 'id_vc')) INTO _id_factura;
		 
		-- actualiza el numero de FACTURA
		UPDATE TBL_COMPRAS_ENTIDADES
		SET Doc = _Numero + 1
		WHERE ID_EntidadCompra = _ID_EntidadCompra;
		
		-- inserta el detalle
		INSERT INTO TBL_COMPRAS_FACTURAS_DET
		SELECT _ID_Factura, Partida, Cantidad, ID_Prod, Precio, Descuento, IVA, Obs, Importe, ImporteDesc, ImporteIVA, TotalPart, IEPS, ImporteIEPS, IVARet, ImporteIVARet, ISRRet, ImporteISRRet
		FROM _TMP_COMPRAS_FACTURAS_DET;

		_Ref := 'CFAC|' || cast(_ID_Factura as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

		-- Actualiza el status de la orden o recepcion ligando el ID de la FACTURA
		IF _ID_Enlace is not null
		THEN
			IF _TipoEnlace = 'CORD'
			THEN
				UPDATE TBL_COMPRAS_ORDENES_CAB
				SET Status = 'F', ID_Factura = _ID_Factura, TipoEnlace = 'CFAC'
				WHERE ID_VC = _ID_Enlace;
			ELSE
				UPDATE TBL_COMPRAS_RECEPCIONES_CAB
				SET ID_Factura = _ID_Factura
				WHERE ID_VC = _ID_Enlace;
			END IF;
		END IF;

		IF _uuid <> ''
		THEN
			UPDATE TBL_CFDCOMP
			SET FSI_Tipo = 'FAC', FSI_ID = _ID_Factura
			WHERE ID_CFD = _ID_CFD;

			_RFC := (select RFC from TBL_CFDCOMP where ID_CFD = _ID_CFD);
			
			FOR _REC_TMP_COMPRAS_FACTURAS_DET IN ( select * from _TMP_COMPRAS_FACTURAS_DET order by Partida asc ) 
			LOOP
				-- Procede a agregar el enlace del producto del proveedor al id_prod en forseti
				if(select count(*) from TBL_INVSERV_PROVEE_CODIGOS where ID_RFC = _RFC and ID_Descripcion =  _REC_TMP_COMPRAS_FACTURAS_DET.Obs and ID_Prod = _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda) = 0
				then
					insert into TBL_INVSERV_PROVEE_CODIGOS
					values(_RFC, _REC_TMP_COMPRAS_FACTURAS_DET.Obs,  _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod, _Moneda, _REC_TMP_COMPRAS_FACTURAS_DET.Precio, _Fecha);
				else
					update TBL_INVSERV_PROVEE_CODIGOS
					set Precio =  _REC_TMP_COMPRAS_FACTURAS_DET.Precio, Fecha = _Fecha
					where ID_RFC = _RFC and ID_Descripcion = _REC_TMP_COMPRAS_FACTURAS_DET.Obs and ID_Prod =   _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda;
				end if;
			END LOOP; 
		END IF;

		--Si no es de mostrador, actualiza los ultimos precios de proveedores
		IF _ID_Proveedor > 0
		THEN
			FOR _REC_TMP_COMPRAS_FACTURAS_DET IN ( select * from _TMP_COMPRAS_FACTURAS_DET order by Partida asc ) 
			LOOP
				-- Procede a agregar el ultimo costo del producto o servicio correspondiente al Proveedor ( si existe lo actualiza de lo contrario lo inserta )
				if(select count(*) from TBL_COMPRAS_VS_INVENTARIO where ID_Tipo = 'PR' and ID_Proveedor = _ID_Proveedor and ID_Prod = _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda) = 0
				then
					insert into TBL_COMPRAS_VS_INVENTARIO
					values('PR', _ID_Proveedor,  _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod, _Moneda, _Fecha,  _REC_TMP_COMPRAS_FACTURAS_DET.Precio,  _REC_TMP_COMPRAS_FACTURAS_DET.Descuento);
				else
					update TBL_COMPRAS_VS_INVENTARIO
					set Fecha = _Fecha, Precio =  _REC_TMP_COMPRAS_FACTURAS_DET.Precio, Descuento =  _REC_TMP_COMPRAS_FACTURAS_DET.Descuento
					where ID_Tipo = 'PR' and ID_Proveedor = _ID_Proveedor and ID_Prod =   _REC_TMP_COMPRAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda;
				end if;
			END LOOP; 
		END IF; 

		-- Actualiza el precio de compra en el inventario
		UPDATE TBL_INVSERV_INVENTARIOS
		SET PrecioComp = tm.Precio, ID_Moneda = _Moneda
		FROM TBL_INVSERV_INVENTARIOS e, _TMP_COMPRAS_FACTURAS_DET tm 
		WHERE e.ID_Prod = tm.ID_Prod
			AND e.ID_Prod = TBL_INVSERV_INVENTARIOS.ID_Prod;
		
		--Crea tablas temporales indispensables... para contable. y contabilidad electronica
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_TMP (
			Part smallint NOT NULL ,
			Cuenta char(19) NOT NULL ,
			Concepto varchar(80) NOT NULL ,
			Parcial numeric(19,4) NOT NULL ,
			Moneda smallint NOT NULL ,
			TC numeric(19,4) NOT NULL ,
			Debe numeric(19,4) NOT NULL ,
			Haber numeric(19,4) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP( 
			id_part smallint NOT NULL,
			num character varying(20) NOT NULL,
			banco character(3) NOT NULL,
			ctaori character varying(50) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			monto numeric(19,4) NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			banemisext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		); 
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP ( 
			id_part smallint NOT NULL,
			ctaori character varying(50) NOT NULL,
			bancoori character(3) NOT NULL,
			monto numeric(19,4) NOT NULL,
			ctadest character varying(50) NOT NULL,
			bancodest character(3) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			bancooriext character varying(150) NOT NULL,
			bancodestext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP (
			id_part smallint NOT NULL,
			metpagopol character(2) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			monto numeric(19,4) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP ( 
			id_part smallint NOT NULL, 
			uuid_cfdi character(36) NOT NULL, 
			monto numeric(19,4) NOT NULL, 
			rfc character varying(13) NOT NULL,
			id_tipo character varying(10) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL,
			cfd_cbb_serie character varying(10) NOT NULL,
			cfd_cbb_numfol integer NOT NULL,
			numfactext character varying(36) NOT NULL,
			taxid character varying(30) NOT NULL
		); 
		
		--fin polizas temporales para contable			
		-- Agrega a la tabla de temporal para: polizas final o detalles de bancos cajas final
		_contPart := 1;
		IF _Condicion = 0 
		THEN
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_COMCONT, 'Importe de la compra', _Importe, _Moneda, _TC, _ImporteTotalPesos, 0.0);
		ELSE
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_COMP, 'Importe de la compra', _Importe, _Moneda, _TC, _ImporteTotalPesos, 0.0);
		END IF;
		
		IF _uuid <> ''
		THEN
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP
			VALUES(_contPart, _uuid, _Total, _RFCBeneficiario, 'CompNal', _moneda_ce, _TC, '', 0, '', '');
		END IF;
		
		-- procede a registrar los otros conceptos de la poliza ( EL IVA , Deuda con Proveedor ETC)
		IF _IVA > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a iva efectivamente pagado
			THEN	
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVA, 'Impuesto de la compra', _IVA, _Moneda, _TC, _IVAPesos, 0.0);
			ELSE -- de credito. o ningun método de pago. envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVAPN, 'Impuesto de la compra', _IVA, _Moneda, _TC, _IVAPesos, 0.0);
			END IF;
		END IF;

		IF _IEPS > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a ieps a efectivamente pagado
			THEN	
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);	
			ELSE -- de credito. o ningun método de pago. envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPSPN, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);	
			END IF;
		END IF;

		IF _IVARet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IVARet, 'Retención de IVA', _IVARet, _Moneda, _TC, 0.0, _IVARetPesos);	
		END IF;

		IF _ISRRet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_ISRRet, 'Retención de ISR', _ISRRet, _Moneda, _TC, 0.0, _ISRRetPesos);	
		END IF;

		IF _Descuento > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_Desc, 'Descuento explicito', _Descuento, _Moneda, _TC, 0.0, _DescPesos);
		END IF;
			
		
		-- procede a registrar los movimiento en la caja, banco, Cuenta por cobrar, y la poliza en caso necesario
		IF _ID_Proveedor > 0 and _Condicion = 1 -- si es a credito y no de mostrador
		THEN
			SELECT * INTO _errpart, _resultpart, _id_cxp 
			FROM sp_provee_cxp_alta(_ID_EntidadCompra, _Fecha, 'PR', _ID_Proveedor, _Concepto, _Moneda, _TC, _Total, _Cantidad, '0', 'CFAC', _ID_Factura, _Ref) as ( err integer, res varchar, clave integer ); --este movimiento no registrara poliza porque el concepto 0 de cxp es de sistema 
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			ELSE
				_clase = 'CCXP|' || cast(_ID_CXP as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

				UPDATE TBL_COMPRAS_FACTURAS_CAB
				SET ID_Pol = _ID_CXP
				WHERE ID_VC = _ID_Factura;
				
				-- Procede a registrar la poliza si y solo si es una entidad dinamica
				-- Primero registra la deuda total del proveedor para la partida doble en tmp
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_PRO, 'Deuda total del Proveedor', _Total, _Moneda, _TC, 0.0, _TotalPesos);
					
				IF _Fija = '0'
				THEN
					-- Primero registra y crea la tabla temporal de detalle de la poliza
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
						Part smallint NOT NULL ,
						Cuenta char(19) NOT NULL ,
						Concepto varchar(80) NOT NULL ,
						Parcial numeric(19,4) NOT NULL ,
						Moneda smallint NOT NULL ,
						TC numeric(19,4) NOT NULL ,
						Debe numeric(19,4) NOT NULL ,
						Haber numeric(19,4) NOT NULL
					);
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
						id_part smallint NOT NULL, 
						uuid_cfdi character(36) NOT NULL, 
						monto numeric(19,4) NOT NULL, 
						rfc character varying(13) NOT NULL,
						id_tipo character varying(10) NOT NULL,
						moneda character(3) NOT NULL,
						tipcamb numeric(19,5) NOT NULL,
						cfd_cbb_serie character varying(10) NOT NULL,
						cfd_cbb_numfol integer NOT NULL,
						numfactext character varying(36) NOT NULL,
						taxid character varying(30) NOT NULL
 					); 
					-- Fin de la tabla temporal

					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
					_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					IF _TotDebe > _TotHaber
					THEN
						_diff := _TotDebe - _TotHaber;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
					ELSIF _TotDebe < _TotHaber
					THEN
						_diff := _TotHaber - _TotDebe;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
					END IF;
					
					-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
					-- Agrega ahora la poliza principal
					--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
					SELECT * INTO _errpart, _resultpart, _numpol 
					FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
					ELSE
						UPDATE  TBL_PROVEE_CXP
						SET ID_Pol = _numpol
						WHERE ID_CP = _ID_CXP;
					END IF;

					DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
					DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
				END IF;
			END IF; 
		ELSIF _Condicion = 0 -- es de contado
		THEN
			-- Termina la poliza dividiendo los pagos en sus cuentas de banco o caja
			_TotalPesosMult = 0.0;
			_ParcialPesosMult = 0.0;
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_ParcialPesosMult := round((_REC_TMP_PAGOS.Total * _TC), 2);
				_TotalPesosMult := _TotalPesosMult + _ParcialPesosMult;
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				_CC_BAN := (select CC from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_CuentaOrigen := (select Descripcion from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_BancoOrigen := (select ID_SatBanco from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				_banCHQ := (select SigCheque from TBL_BANCOS_CUENTAS where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj)::varchar(20);
				_contPart := _contPart + 1;
				
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_BAN, _REC_TMP_PAGOS.RefPago, _REC_TMP_PAGOS.Total, _Moneda, _TC, 0.0, _ParcialPesosMult);
				--Ingresa contablilidad electronica del pago...
							-- Actualiza la contabilidad electronica de este BANCO o CAJA a esta partida, ya sea por cheque, transferencia u otro metodo de pago
				IF _BanCaj = 0 -- Es banco
				THEN
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _banCHQ, ID_SatBanco, Descripcion, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCBeneficiario, BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSIF _REC_TMP_PAGOS.ID_SatMetodosPago = '03' --Es una transferencia
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP
						SELECT _contPart, Descripcion, ID_SatBanco, _REC_TMP_PAGOS.Total, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.ID_SatBanco, _Fecha,  _Beneficiario, _RFCBeneficiario, BancoExt, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				ELSE -- BanCaj = 1 "Caja"
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque (Deposito o Retiro es lo mismo, toma los datos directos capturados en el dialogo)
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCBeneficiario, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES ( 
					id_part smallint NOT NULL,
					num character varying(20) NOT NULL,
					banco character(3) NOT NULL,
					ctaori character varying(50) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					monto numeric(19,4) NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					banemisext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				); 
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS ( 
					id_part smallint NOT NULL,
					ctaori character varying(50) NOT NULL,
					bancoori character(3) NOT NULL,
					monto numeric(19,4) NOT NULL,
					ctadest character varying(50) NOT NULL,
					bancodest character(3) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					bancooriext character varying(150) NOT NULL,
					bancodestext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO (
					id_part smallint NOT NULL,
					metpagopol character(2) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					monto numeric(19,4) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL
				); 
				-- Fin de la tabla temporal

				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;

				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP y DEMAS
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
				-- Agrega ahora la poliza principal
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('EG', _Fecha, _Concepto,'0', '', _TotalPesos, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				END IF;
				
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES; 
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
			END IF;

			--Ahora ejecuta los movimientos de caja y bancos
			_clase := '';
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				--_tipoRetiro := (select case when _REC_TMP_PAGOS.ID_FormaPago = 1 Then 'CHQ' else 'RET' end);
				SELECT * INTO _errpart, _resultpart, _bancajmov 
				FROM  sp_bancos_movs_agregar( _BanCaj, _REC_TMP_PAGOS.ID_BanCaj, _Fecha, _Concepto, _Beneficiario, 0.00, _REC_TMP_PAGOS.Total, _REC_TMP_PAGOS.TipoMov, 
						'G', _Moneda, _TC, _Ref, _REC_TMP_PAGOS.RefPago, _id_clasificacion, _numpol, _REC_TMP_PAGOS.ID_SatBanco, _RFCBeneficiario, _REC_TMP_PAGOS.ID_SatMetodosPago, 
																		_REC_TMP_PAGOS.BancoExt, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.Cheque) as ( err integer, res varchar, clave integer);
				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
					EXIT;
				END IF;

				INSERT INTO TBL_COMPRAS_FACTURAS_PAGOS
				VALUES(_ID_Factura, _bancajmov);
				
				IF _numPagos > 1
				THEN
					IF _BanCaj = 0
					THEN
						_clase := _clase || 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					ELSE
						_clase := _clase || 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
					END IF;
				ELSE
					IF _BanCaj = 0
					THEN
						_clase := 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					ELSE
						_clase := 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0' -- Aqui registra la referencia de las polizas de ingreso a sus movimientos de caja o bancos
			THEN
				IF _numPagos > 1
				THEN
					INSERT INTO TBL_REFERENCIAS_EXT
					VALUES(default,_clase)
					RETURNING currval(pg_get_serial_sequence(' TBL_REFERENCIAS_EXT', 'id_ref')) INTO _RefExt;
					UPDATE TBL_CONT_POLIZAS
					SET Ref = 'REXT|' || cast(_RefExt as varchar) || '|||'
					WHERE ID = _numpol;
		 		ELSE
					UPDATE TBL_CONT_POLIZAS
					SET Ref = _clase
					WHERE ID = _numpol;
				END IF;
			END IF; 
		ELSIF _Condicion = 3 -- Es de ningun pago
		THEN
			_clase = 'CFAC|' || cast(_ID_Factura as varchar) || '|' || cast(_ID_EntidadCompra as varchar) || '||';

			-- Procede a registrar la poliza si y solo si es una entidad dinamica
			-- Primero registra la deuda total del proveedor para la partida doble en tmp
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_COMPNP, 'Documento por pagar', _Total, _Moneda, _TC, 0.0, _TotalPesos);
					
			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL
 				); 
				-- Fin de la tabla temporal
					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;
				
				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;

				-- Agrega ahora la poliza principal
				--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
 				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					UPDATE TBL_COMPRAS_FACTURAS_CAB
					SET ID_Pol = _numpol
					WHERE ID_VC = _ID_Factura;
				END IF;

				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
			END IF;
		END IF;
		
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP; 
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;

		--Procede a agregar el movimiento al almacén
		IF _err = 0 AND _FijaCost = '0' AND _RecepcionAsociada = '0' and (select count(*) from _TMP_COMPRAS_FACTURAS_DET where Tipo = 'P') > 0
		THEN
			CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (
				ID_Bodega smallint NOT NULL ,
				ID_Prod varchar(20) NOT NULL ,
				Partida smallint NOT NULL ,
				Cantidad numeric(9, 3) NOT NULL ,
				Costo numeric(19,4) NULL 
			); 

			insert into _TMP_INVSERV_ALMACEN_MOVIM_DET
			select _ID_Bodega, ID_Prod, Partida, Cantidad, round(Precio * _TC,4)  --Precio es el costo de la compra
			from _TMP_COMPRAS_FACTURAS_DET
			where Tipo = 'P'
			order by Partida ASC;
		
			SELECT * INTO _errpart, _resultpart, _ID_Movimiento 
			FROM sp_invserv_alm_movs_agregar(_Fecha, _ID_Bodega, (case when _AuditarAlm = '1' then 'G' else 'U' end), '1', _ConceptoCost, '',/*1 ENT 2 SAL*/ '1', _Ref, 'CFAC', _ID_Factura) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				UPDATE TBL_COMPRAS_FACTURAS_CAB
				SET ID_PolCost = _ID_Movimiento
				WHERE ID_VC = _ID_Factura;
			END IF;
	
			DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET; 
		END IF;
		-- Fin del movimiento al almacen

		--Procede a agregar la información para comercio exterior en caso de ser importación
		IF _err = 0 AND _Moneda <> 1 AND _Pais <> 'MEX' and _Pedimento <> '--'
		THEN
      INSERT INTO TBL_COMPRAS_FACTURAS_COMEXT_CAB(id_vc,tipooperacion,clavedepedimento,certificadoorigen,numcertificadoorigen,numeroexportadorconfiable,
        incoterm,subdivision,observaciones,tipocambiousd,totalusd,emisor_curp,receptor_curp,receptor_numregidtrib,destinatario_numregidtrib,destinatario_rfc,
        destinatario_curp,destinatario_nombre,destinatario_domicilio_calle,destinatario_domicilio_numeroexterior,destinatario_domicilio_numerointerior,
        destinatario_domicilio_colonia,destinatario_domicilio_localidad,destinatario_domicilio_referencia,destinatario_domicilio_municipio,
        destinatario_domicilio_estado,destinatario_domicilio_pais,destinatario_domicilio_codigopostal)
      SELECT _ID_Factura, '-', _Pedimento, -1, '', '', 
        '', -1, _Obs, _TC, _Importe, '', '', registro_tributario, registro_tributario, '', 
        '', nombre, direccion, noext, noint, 
        colonia, poblacion, '', municipio,
        estado, pais, cp
      FROM TBL_PROVEE_PROVEE
      WHERE ID_Tipo = 'PR' and ID_Clave = _ID_Proveedor;
      
      -- inserta el detalle
      INSERT INTO TBL_COMPRAS_FACTURAS_COMEXT_DET(id_vc,partida,noidentificacion,fraccionarancelaria,cantidadaduana,unidadaduana,valorunitarioaduana,valordolares) 
      SELECT _ID_Factura, det.Partida, det.ID_Prod, '', det.Cantidad, (select coalesce(id_satunidad,99) from tbl_invserv_unidades where id_unidad = i.id_unidadsalida), round(det.Precio - det.Descuento,2), round(det.Importe - det.ImporteDesc,2)
      FROM _TMP_COMPRAS_FACTURAS_DET det INNER JOIN TBL_INVSERV_INVENTARIOS i 
        ON det.ID_Prod = i.ID_Prod
      WHERE det.Tipo = 'P';
      
      -- inserta el detalle
      INSERT INTO TBL_COMPRAS_FACTURAS_COMEXT_DET_DESCESP(id_vc,partida,descripcion,marca,modelo,submodelo,numeroserie)
      SELECT _ID_Factura, Partida, 1, '', '', '', ''
      FROM _TMP_COMPRAS_FACTURAS_DET
      WHERE Tipo = 'P';
		END IF;
		--Fin de comercio exterior
	END IF; 
	
	RETURN QUERY SELECT _err, _result, _id_factura;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_facturas_agregar(smallint, integer, integer, timestamp without time zone, character varying, smallint, numeric, smallint, character varying, numeric, numeric, numeric, numeric, numeric, numeric, numeric, numeric, smallint, integer, smallint, character, character, numeric, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_compras_facturas_comext(
    _id_vc integer,
    _tipooperacion character,
    _certificadoorigen smallint,
    _numcertificadoorigen character varying,
    _numeroexportadorconfiable character varying,
    _incoterm character varying,
    _subdivision smallint,
    _observaciones character varying,
    _tipocambiousd numeric,
    _totalusd numeric,
    _emisor_curp character varying,
    _receptor_curp character varying,
    _receptor_numregidtrib character varying,
    _destinatario_numregidtrib character varying,
    _destinatario_rfc character varying,
    _destinatario_curp character varying,
    _destinatario_nombre character varying,
    _destinatario_domicilio_calle character varying,
    _destinatario_domicilio_numeroexterior character varying,
    _destinatario_domicilio_numerointerior character varying,
    _destinatario_domicilio_colonia character varying,
    _destinatario_domicilio_localidad character varying,
    _destinatario_domicilio_referencia character varying,
    _destinatario_domicilio_municipio character varying,
    _destinatario_domicilio_estado character varying,
    _destinatario_domicilio_pais character,
    _destinatario_domicilio_codigopostal character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _Fecha timestamp; _Mes smallint; _Ano smallint;
BEGIN
	_err := 0;
	_result := 'El complemento de importación para esta factura se actualizó con éxito';

  _Fecha := (select Fecha from tbl_compras_facturas_cab where id_vc = _id_vc);
  _Mes := date_part('month',_Fecha);
	_Ano := date_part('year',_Fecha);
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

  IF _err = 0
	THEN
    delete from tbl_compras_facturas_comext_det_descesp
    where id_vc = _id_vc;

    delete from tbl_compras_facturas_comext_det
    where id_vc = _id_vc;

    update tbl_compras_facturas_comext_cab
    set tipooperacion = _tipooperacion,
        certificadoorigen = _certificadoorigen,
        numcertificadoorigen = _numcertificadoorigen,
        numeroexportadorconfiable = _numeroexportadorconfiable,
        incoterm = _incoterm,
        subdivision = _subdivision,
        observaciones = _observaciones,
        tipocambiousd = _tipocambiousd,
        totalusd = _totalusd,
        emisor_curp = _emisor_curp,
        receptor_curp = _receptor_curp,
        receptor_numregidtrib = _receptor_numregidtrib,
        destinatario_numregidtrib = _destinatario_numregidtrib,
        destinatario_rfc = _destinatario_rfc,
        destinatario_curp = _destinatario_curp,
        destinatario_nombre = _destinatario_nombre,
        destinatario_domicilio_calle = _destinatario_domicilio_calle,
        destinatario_domicilio_numeroexterior = _destinatario_domicilio_numeroexterior,
        destinatario_domicilio_numerointerior = _destinatario_domicilio_numerointerior,
        destinatario_domicilio_colonia = _destinatario_domicilio_colonia,
        destinatario_domicilio_localidad = _destinatario_domicilio_localidad,
        destinatario_domicilio_referencia = _destinatario_domicilio_referencia,
        destinatario_domicilio_municipio = _destinatario_domicilio_municipio,
        destinatario_domicilio_estado = _destinatario_domicilio_estado,
        destinatario_domicilio_pais = _destinatario_domicilio_pais,
        destinatario_domicilio_codigopostal = _destinatario_domicilio_codigopostal
    where id_vc = _id_vc;

    insert into tbl_compras_facturas_comext_det(id_vc,partida,noidentificacion,fraccionarancelaria,cantidadaduana,unidadaduana,valorunitarioaduana,valordolares) 
    select _id_vc, partida, noidentificacion, fraccionarancelaria, cantidadaduana, unidadaduana, valorunitarioaduana, valordolares
    from _tmp_compras_facturas_comext_det;

    insert into tbl_compras_facturas_comext_det_descesp(id_vc,partida,descripcion,marca,modelo,submodelo,numeroserie)
    select _id_vc, partida, descripcion, marca, modelo, submodelo, numeroserie
    from _tmp_compras_facturas_comext_det_descesp;

  END IF;
  
  RETURN QUERY SELECT _err, _result, _id_vc;
  
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_compras_facturas_comext(integer, character, smallint, character varying, character varying, character varying, smallint, character varying, numeric, numeric, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ventas_facturas_agregar(
    _id_entidadventa smallint,
    _numero integer,
    _id_cliente integer,
    _fecha timestamp without time zone,
    _referencia character varying,
    _moneda smallint,
    _tc numeric,
    _condicion smallint,
    _obs character varying,
    _importe numeric,
    _descuento numeric,
    _subtotal numeric,
    _iva numeric,
    _total numeric,
    _fsipg_efectivo numeric,
    _fsipg_bancos numeric,
    _fsipg_cambio numeric,
    _id_bodega smallint,
    _id_enlace integer,
    _id_vendedor smallint,
    _tipoenlace character,
    _uuid character,
    _ieps numeric,
    _ivaret numeric,
    _isrret numeric)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_Factura int; _mes smallint; _ano smallint;	_errpart int;  _resultpart varchar(255);  _ID_Movimiento int;  _concepto varchar(80); _conceptocost varchar(80); _Ref varchar(25);  _claseref varchar(25); 
	_FijaCost bit; _costven numeric(19,4); _CC_COSTVEN char(19); _AuditarAlm bit;
	_ID_CXC int; _numpol int; _clase varchar(1024); _bancajmov int;
	_nombreCli varchar(120); _Cantidad numeric(19,4); _ServComp bit; _CC_CLI char(19); 
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IEPSPN char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_VENNP char(19); _CC_DCAF char(19); 
	_CC_DCEC char(19); _diff numeric(19,4); _TotDebe numeric(19,4); _TotHaber numeric(19,4); _contPart smallint; 
	_totPart smallint; _CC_VEN char(19); _CC_VENCONT char(19); _DesgloseCLIENT bit;  _Pais character(3); _Pedimento varchar(5);
	 _contban smallint; _totalban smallint; _ID_FormaPago smallint; _ID_BanCaj smallint; _tipoDeposito character(3); _RefPago varchar(50); _IdMon smallint; _RemisionAsociada bit; 
	_id_clasificacion varchar(10); _numPagos smallint; _RefExt int; _ID_CFD int; _TFD smallint;  
	_Beneficiario varchar(80); _RFCOrigen varchar(15); _moneda_ce character(3); /*_CuentaOrigen varchar(80); _BancoOrigen character(3); _CuentaDestino varchar(50); _BancoDestino character(3);*/
	_DocFinal varchar(50); _ID_SatBanco character(3); _RFC varchar(15);	
	--Iteracion
	_REC_TMP_PAGOS RECORD; _REC_TMP_VENTAS_FACTURAS_DET RECORD; _TotalPesosMult numeric(19,4); _ParcialPesosMult numeric(19,4); _BanCaj smallint; _CC_BAN char(19);
BEGIN	
	_err := 0;
	_result := 'La factura se ha registrado satisfactoriamente';
	--_ServComp := '1';
	_CC_IVA := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPP');
	_CC_IVAPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVAPPPN');
	_CC_IEPS := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSV');
	_CC_IEPSPN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IEPSVPN');
	_CC_IVARet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVARETV');
	_CC_ISRRet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_ISRRETV');
	_CC_Desc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSV');
	_moneda_ce := (select id_satmoneda from TBL_CONT_MONEDAS where Clave = _moneda);
	_Cantidad := round((_Total * _TC), 2);
	_ImporteTotalPesos := ROUND((_Importe * _TC), 2);
	_IVAPesos :=  ROUND((_IVA * _TC), 2);
	_IEPSPesos :=  ROUND((_IEPS * _TC), 2);
	_IVARetPesos :=  ROUND((_IVARet * _TC), 2);
	_ISRRetPesos :=  ROUND((_ISRRet * _TC), 2);
	_DescPesos := ROUND((_Descuento * _TC), 2);
	_TotalPesos := ROUND((_Total * _TC), 2);	
	_concepto := 'Venta ' || (select Descripcion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);
	_conceptocost := 'Costo ' || (select Descripcion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta) || ', numero ' || cast(_Numero as varchar) || 
				(case when _Referencia <> '' then ' y referencia ' || _Referencia || '.' else '.' end);

	_Beneficiario := case when _ID_Cliente = 0 then 'Al Portador' else ( select VAlfanumerico from TBL_VARIABLES where ID_Variable = 'EMPRESA') end;
  _Pais := case when _ID_Cliente = 0 then 'MEX' else ( select Pais from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) end;
  _Pedimento := case when _ID_Cliente = 0 then '--' else ( select Pedimento from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) end; 
  _RFCOrigen := case when _ID_Cliente = 0 then 'XAXX010101000' else ( select RFC from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) end;
	--_CuentaOrigen := case when _ID_Cliente = 0 then '' else ( select MetodoDePago from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) end;
	--_BancoOrigen := case when _ID_Cliente = 0 then '000' else ( select ID_SatBanco from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) end;

	_CC_CLI := (select ID_CC from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente);
	_DesgloseCLIENT := (select DesgloseCLIENT from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_Fija := (select Fija from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_FijaCost := (select FijaCost from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_AuditarAlm := ( select AuditarAlm from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega );
	_RemisionAsociada := ( case when _ID_Enlace is not null and _TipoEnlace = 'VREM' then '1' else '0' end );
	_CC_DCAF := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCAF');
	_CC_DCEC := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DCEC');
	_id_clasificacion := (select ID_Clasificacion from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta);
	_numPagos = (select count(*) from _TMP_PAGOS);

	IF _ID_Enlace is not null
	THEN
		_claseref := _TipoEnlace || '|' || cast(_ID_Enlace as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';
	END IF;
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _CC_DCAF = '' or _CC_DCEC = ''
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'VEN_CXC' and 
			m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Las cuentas de diferencias cambiarias no se han registrado aun';
	END IF;

	IF (select count(*) from TBL_VENTAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadVenta and Numero = _Numero) > 0
	THEN
		_err := 3;
		_result := 'ERROR: La clave de la factura ya existe, No se puede duplicar';
	END IF;

	IF ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' ) = 1
	THEN
		IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_CHFIS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: No se puede generar el movimiento al almacen porque ya existe un chequeo f?sico cerrado en esta bodega, con fecha posterior a este movimiento';
		END IF;
	END IF;

	IF _Condicion = 0
	THEN
		FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
		LOOP
			_ID_FormaPago := _REC_TMP_PAGOS.ID_FormaPago;
			_ID_BanCaj := _REC_TMP_PAGOS.ID_BanCaj;
			_FijaBAN := (case when _ID_FormaPago = 1 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			_IdMon := (case when _ID_FormaPago = 1 then (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
							else (select ID_Moneda from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end);
			
			IF _Fija <> _FijaBAN
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la venta porque al pagarse de contado requiere que los bancos o cajas manejen los mismos trazos contables que la venta';
				EXIT;
			END IF;

			IF _Moneda <> _IdMon 
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la venta porque al pagarse de contado requiere que los bancos o cajas manejen la misma moneda que la venta';
				EXIT;
			END IF;

			/*IF _ID_Cliente = 0 and _Fija = '0' and _ID_FormaPago > 1
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar una venta directa de mostrador pagada de contado con transferencia, porque no existe soporte para contabilidad electrónica, selecciona un cliente o una entidad Fija';
				EXIT;
			END IF;*/
		END LOOP;
	ELSIF _Condicion = 1 -- Es a crédito, por lo tanto revisa si el total de esta factura mas el total de la deuda del cliente rebasa el límite de crédito, de ser asi, rechazará la venta
	THEN
		IF(select Dias from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) > 0 -- este cliente tiene crédito
		THEN
			IF (select LimiteCredito from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente) > 0 AND 
				((_ImporteTotalPesos + getcliprosldtotmn('CL', _ID_Cliente)) > (select LimiteCredito from TBL_CLIENT_CLIENT where ID_Tipo = 'CL' and ID_Clave = _ID_Cliente))
			THEN
				_err := 3;
				_result := 'ERROR: No se puede agregar la factura a crédito porque la suma de las deudas de este cliente, más esta factura, sobrepasa su límite de crédito';
			END IF;
		ELSE -- no tiene crédito
			_err := 3;
			_result := 'ERROR: No se puede agregar la factura porque al cobrarse a crédito requiere que se le brinde al cliente por lo menos un día de crédito';
		END IF;	
	END IF;
	
	IF _IVA > 0.0 AND ( _CC_IVA = '' OR _CC_IVAPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de iva por pagar efectivamente cobrado o la de iva por pagar pendiente de cobrar, no existe o no se ha enlazado';
	END IF;
	IF _IEPS > 0.0 AND ( _CC_IEPS = '' OR _CC_IEPSPN = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS efectivamente cobrado o la de IEPS pendiente de cobrar no existe o no se ha enlazado';
	END IF;
	IF _IVARet > 0.0 AND ( _CC_IVARet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de IVA Retenido no existe o no se ha enlazado';
	END IF;
	IF _ISRRet > 0.0 AND ( _CC_ISRRet = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de ISR Retenido no existe o no se ha enlazado';
	END IF;
	IF _Descuento > 0.0 AND _CC_Desc = ''
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta de descuento sobre ventas no existe o no se ha enlazado';
	END IF;
	
	-- procede a realizar verificaci?n sobre los trazos
	IF _FijaCost = '0'
	THEN
		_CC_COSTVEN := (select cc from TBL_INVSERV_COSTOS_CONCEPTOS where ID_Concepto = 50);
		IF _CC_COSTVEN is null
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para el costo de ventas no existe o no se ha enlazado';	
		END IF;
	END IF;

	IF _Condicion = 0
	THEN
		_CC_VENCONT := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VENCONT');
		IF _CC_VENCONT = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para ventas de contado no existe o no se ha enlazado';	
		END IF;
	ELSE
		_CC_VEN := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VEN');
		IF _CC_VEN = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para ventas a crédito no existe o no se ha enlazado';	
		END IF;
	END IF;

	IF _Condicion = 3
	THEN
		_CC_VENNP := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_VENNP');
		IF _CC_VENNP = ''
		THEN
			_err := 3;
			_result := 'ERROR: La cuenta para registro de partida doble cuando no se establece ningun metodo de pago, no existe o no se ha enlazado';
		END IF;
	END IF;

	--VERIFICA SI ES CFD
	IF (select CFD from TBL_VENTAS_ENTIDADES where ID_EntidadVenta = _ID_EntidadVenta) <> '00'
	THEN
		IF _uuid <> '' -- Es emisor directo de CFDI, por lo tanto no puede enlazar un CFDI Externo
		THEN
			_err := 3;
			_result := 'ERROR: No se puede enlazar ningun CFDI porque esta entidad de venta genera sus propios CFDIs';
		ELSE
			IF (select count(*) from TBL_CFD_REPORTE_MENSUAL where Mes = _mes and Ano = _ano and Cerrado = '0') > 0
			THEN
				IF (select count(*) from TBL_VENTAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadVenta and Numero = (_Numero -1)) is not null
				THEN
					IF (select TFD from TBL_VENTAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadVenta and Numero = (_Numero -1)) is null
							or (select TFD from TBL_VENTAS_FACTURAS_CAB where ID_Entidad = _ID_EntidadVenta and Numero = (_Numero -1)) <> 3
					THEN 
						_err := 3;
						_result := 'ERROR: No se puede agregar la factura porque la factura anterior no est&aacute; sellada. Primero debes sellar la factura anterior para poder agregar esta factura';
					END IF;
				END IF;
			ELSE
				_err := 3;
				_result := 'ERROR: No se puede agregar la factura porque el mes de comprobantes fiscales digitaya';
			END IF;
		END IF;
	ELSE -- Si no es Emisor directo de CFDI comprueba si es carga con CFDI externo
		IF _uuid <> ''
		THEN
			IF (select count(*) from TBL_CFDVEN where UUID = _uuid) > 0
			THEN
				_ID_CFD := (select ID_CFD from TBL_CFDVEN where UUID = _uuid) ;
				_TFD := 3;
			ELSE
				_err := 3;
				_result := 'ERROR: No se puede enlazar el CFDI al registro, porque no existe el UUID proporcionado';
			END IF;
		END IF;
	END IF;

	IF _err = 0
	THEN
		INSERT INTO TBL_VENTAS_FACTURAS_CAB (  id_entidad, numero, id_clipro, fecha, referencia, status, moneda, tc, fechaenvio, condicion, obs, 
			importe, descuento, subtotal, iva, total, ref, id_pol, id_polcost, id_bodega, mimporte, mdescuento, msubtotal, miva, mtotal, efectivo, bancos, cambio, id_vendedor, id_cfd, tfd, 
			ieps, ivaret, isrret )
		VALUES(_ID_EntidadVenta, _Numero, _ID_Cliente, _Fecha, _Referencia, (case when _RemisionAsociada = '0' then (case when _AuditarAlm = '1' then 'G' else 'E' end) else 'E' end), _Moneda, _TC, _Fecha, _Condicion, _Obs,
				_Importe, _Descuento, _SubTotal, _IVA, _Total, _claseref, null, null, _ID_Bodega, _Importe, _Descuento, _SubTotal, _IVA,  _Total, _FSIPG_Efectivo, _FSIPG_Bancos, _FSIPG_Cambio, _ID_Vendedor, _ID_CFD, _TFD,
				_ieps, _ivaret, _isrret)
		 RETURNING currval(pg_get_serial_sequence('TBL_VENTAS_FACTURAS_CAB', 'id_vc')) INTO _id_factura;
		 
		-- actualiza el numero de FACTURA
		UPDATE TBL_VENTAS_ENTIDADES
		SET Doc = _Numero + 1
		WHERE ID_EntidadVenta = _ID_EntidadVenta;
		
		-- inserta el detalle
		INSERT INTO TBL_VENTAS_FACTURAS_DET
		SELECT _ID_Factura, Partida, Cantidad, ID_Prod, Precio, Descuento, IVA, Obs, Importe, ImporteDesc, ImporteIVA, TotalPart, IEPS, ImporteIEPS, IVARet, ImporteIVARet, ISRRet, ImporteISRRet
		FROM _TMP_VENTAS_FACTURAS_DET;

		_Ref := 'VFAC|' || cast(_ID_Factura as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';

		-- Actualiza el status de la cotizacion, pedido o remision ligando el ID de la FACTURA
		IF _ID_Enlace is not null
		THEN
			IF _TipoEnlace = 'VPED'
			THEN
				UPDATE TBL_VENTAS_PEDIDOS_CAB
				SET Status = 'F', ID_Factura = _ID_Factura, TipoEnlace = 'VFAC'
				WHERE ID_VC = _ID_Enlace;
			ELSIF _TipoEnlace = 'VCOT'
			THEN
				UPDATE TBL_VENTAS_COTIZACIONES_CAB
				SET Status = 'F', ID_Factura = _ID_Factura, TipoEnlace = 'VFAC'
				WHERE ID_VC = _ID_Enlace;
			ELSE
				UPDATE TBL_VENTAS_REMISIONES_CAB
				SET ID_Factura = _ID_Factura
				WHERE ID_VC = _ID_Enlace;
			END IF;
		END IF;

		IF _uuid <> ''
		THEN
			UPDATE TBL_CFDVEN
			SET FSI_Tipo = 'FAC', FSI_ID = _ID_Factura
			WHERE ID_CFD = _ID_CFD;
			
			UPDATE TBL_CFD
			SET FSI_Tipo = 'FAC', FSI_ID = _ID_Factura
			WHERE ID_CFD = _ID_CFD;
		END IF;
		
		--Si no es de mostrador, actualiza los ultimos precios de clientes
		IF _ID_Cliente > 0
		THEN
			FOR _REC_TMP_VENTAS_FACTURAS_DET IN ( select * from _TMP_VENTAS_FACTURAS_DET order by Partida asc ) 
			LOOP
				-- Procede a agregar el ultimo costo del producto o servicio correspondiente al Cliente ( si existe lo actualiza de lo contrario lo inserta )
				if(select count(*) from TBL_VENTAS_VS_INVENTARIO where ID_Tipo = 'CL' and ID_Cliente = _ID_Cliente and ID_Prod = _REC_TMP_VENTAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda) = 0
				then
					insert into TBL_VENTAS_VS_INVENTARIO
					values('CL', _ID_Cliente,  _REC_TMP_VENTAS_FACTURAS_DET.ID_Prod, _Moneda, _Fecha,  _REC_TMP_VENTAS_FACTURAS_DET.Precio,  _REC_TMP_VENTAS_FACTURAS_DET.Descuento);
				else
					update TBL_VENTAS_VS_INVENTARIO
					set Fecha = _Fecha, Precio =  _REC_TMP_VENTAS_FACTURAS_DET.Precio, Descuento =  _REC_TMP_VENTAS_FACTURAS_DET.Descuento
					where ID_Tipo = 'CL' and ID_Cliente = _ID_Cliente and ID_Prod =   _REC_TMP_VENTAS_FACTURAS_DET.ID_Prod and ID_Moneda = _Moneda;
				end if;
			END LOOP; 
		END IF; 

		--Crea tabla temporal indispensable... para contable.
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_TMP (
			Part smallint NOT NULL ,
			Cuenta char(19) NOT NULL ,
			Concepto varchar(80) NOT NULL ,
			Parcial numeric(19,4) NOT NULL ,
			Moneda smallint NOT NULL ,
			TC numeric(19,4) NOT NULL ,
			Debe numeric(19,4) NOT NULL ,
			Haber numeric(19,4) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP( 
			id_part smallint NOT NULL,
			num character varying(20) NOT NULL,
			banco character(3) NOT NULL,
			ctaori character varying(50) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			monto numeric(19,4) NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			banemisext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		); 
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP ( 
			id_part smallint NOT NULL,
			ctaori character varying(50) NOT NULL,
			bancoori character(3) NOT NULL,
			monto numeric(19,4) NOT NULL,
			ctadest character varying(50) NOT NULL,
			bancodest character(3) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			bancooriext character varying(150) NOT NULL,
			bancodestext character varying(150) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP (
			id_part smallint NOT NULL,
			metpagopol character(2) NOT NULL,
			fecha timestamp without time zone NOT NULL,
			benef character varying(300) NOT NULL,
			rfc character varying(13) NOT NULL,
			monto numeric(19,4) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL
		);
		CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP ( 
			id_part smallint NOT NULL, 
			uuid_cfdi character(36) NOT NULL, 
			monto numeric(19,4) NOT NULL, 
			rfc character varying(13) NOT NULL,
			id_tipo character varying(10) NOT NULL,
			moneda character(3) NOT NULL,
			tipcamb numeric(19,5) NOT NULL,
			cfd_cbb_serie character varying(10) NOT NULL,
			cfd_cbb_numfol integer NOT NULL,
			numfactext character varying(36) NOT NULL,
			taxid character varying(30) NOT NULL
		); 
		-- Agrega a la tabla de temporal para: polizas final o detalles de bancos cajas final
		_contPart := 1;
		IF _Condicion = 0 
		THEN
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_VENCONT, 'Importe de la venta', _Importe, _Moneda, _TC, 0.0, _ImporteTotalPesos);
		ELSE
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_VEN, 'Importe de la venta', _Importe, _Moneda, _TC, 0.0, _ImporteTotalPesos);
		END IF;

		IF _uuid <> ''
		THEN
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP
			VALUES(_contPart, _uuid, _Total, _RFCOrigen, 'CompNal', _moneda_ce, _TC, '', 0, '', '');
		END IF;

		-- procede a registrar los otros conceptos de la poliza ( EL IVA , Deuda con Cliente ETC)
		IF _IVA > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a iva efectivamente cobrado
			THEN
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVA, 'Impuesto de la venta', _IVA, _Moneda, _TC, 0.0, _IVAPesos);	
			ELSE -- de credito o sin pago... envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVAPN, 'Impuesto de la venta', _IVA, _Moneda, _TC, 0.0, _IVAPesos);	
			END IF;
		END IF;

		IF _IEPS > 0.0
		THEN
			_contPart := _contPart + 1;
			IF _Condicion = 0 -- es de contado, envia todo a ieps efectivamente cobrado
			THEN
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, 0.0, _IEPSPesos);	
			ELSE -- de credito o sin pago... envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IEPSPN, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, 0.0, _IEPSPesos);	
			END IF;
		END IF;

		IF _IVARet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IVARet, 'Retención de IVA', _IVARet, _Moneda, _TC, _IVARetPesos, 0.0);	
		END IF;

		IF _ISRRet > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_ISRRet, 'Retención de ISR', _ISRRet, _Moneda, _TC, _ISRRetPesos, 0.0);	
		END IF;

		IF _Descuento > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_Desc, 'Descuento explicito', _Descuento, _Moneda, _TC, _DescPesos, 0.0);
		END IF;
		
		-- procede a registrar los movimiento en la caja, banco, Cuenta por cobrar, y la poliza en caso necesario
		IF _ID_Cliente > 0 and _Condicion = 1 -- si es a credito y no de mostrador
		THEN
			SELECT * INTO _errpart, _resultpart, _id_cxc 
			FROM sp_client_cxc_alta(_ID_EntidadVenta, _Fecha, 'CL', _ID_Cliente, _Concepto, _Moneda, _TC, _Total, _Cantidad, '0', 'VFAC', _ID_Factura, _Ref) as ( err integer, res varchar, clave integer ); --este movimiento no registrara poliza porque el concepto 0 de cxc es de sistema 
			IF _errpart <> 0
			THEN
				_err := 3;
				_result := _resultpart;
			ELSE
				_clase = 'VCXC|' || cast(_ID_CXC as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';

				UPDATE TBL_VENTAS_FACTURAS_CAB
				SET ID_Pol = _ID_CXC
				WHERE ID_VC = _ID_Factura;
				
				-- Procede a registrar la poliza si y solo si es una entidad dinamica
				-- Primero registra la deuda total del cliente para la partida doble en tmp
				_contPart := _contPart + 1;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_CLI, 'Deuda total del Cliente', _Total, _Moneda, _TC, _TotalPesos, 0.0);
					
				IF _Fija = '0'
				THEN
					-- Primero registra y crea la tabla temporal de detalle de la poliza
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
						Part smallint NOT NULL ,
						Cuenta char(19) NOT NULL ,
						Concepto varchar(80) NOT NULL ,
						Parcial numeric(19,4) NOT NULL ,
						Moneda smallint NOT NULL ,
						TC numeric(19,4) NOT NULL ,
						Debe numeric(19,4) NOT NULL ,
						Haber numeric(19,4) NOT NULL
					);
					CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
						id_part smallint NOT NULL, 
						uuid_cfdi character(36) NOT NULL, 
						monto numeric(19,4) NOT NULL, 
						rfc character varying(13) NOT NULL,
						id_tipo character varying(10) NOT NULL,
						moneda character(3) NOT NULL,
						tipcamb numeric(19,5) NOT NULL,
						cfd_cbb_serie character varying(10) NOT NULL,
						cfd_cbb_numfol integer NOT NULL,
						numfactext character varying(36) NOT NULL,
						taxid character varying(30) NOT NULL
 					); 
					-- Fin de la tabla temporal

					-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
					_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
					IF _TotDebe > _TotHaber
					THEN
						_diff := _TotDebe - _TotHaber;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
					ELSIF _TotDebe < _TotHaber
					THEN
						_diff := _TotHaber - _TotDebe;
						_contPart := _contPart + 1;
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
						VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
					END IF;
					
					-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
					SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
					-- Agrega ahora la poliza principal
					--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
					SELECT * INTO _errpart, _resultpart, _numpol 
					FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
					ELSE
						UPDATE  TBL_CLIENT_CXC
						SET ID_Pol = _numpol
						WHERE ID_CP = _ID_CXC;
					END IF;

					DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
					DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
				END IF;
			END IF; 
		ELSIF _Condicion = 0 -- es de contado
		THEN
			-- Termina la poliza dividiendo los pagos en sus cuentas de banco o caja
			_TotalPesosMult = 0.0;
			_ParcialPesosMult = 0.0;
			FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
			LOOP
				_ParcialPesosMult := round((_REC_TMP_PAGOS.Total * _TC), 2);
				_TotalPesosMult := _TotalPesosMult + _ParcialPesosMult;
				_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos
				_CC_BAN := (select CC from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_CuentaDestino := (select Descripcion from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				--_BancoDestino := (select ID_SatBanco from TBL_BANCOS_CUENTAS  where Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj);
				_contPart := _contPart + 1;
								
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_BAN, _REC_TMP_PAGOS.RefPago, _REC_TMP_PAGOS.Total, _Moneda, _TC, _ParcialPesosMult, 0.0);

				--Ingresa contablilidad electronica del pago
				IF _BanCaj = 0 -- Es banco
				THEN
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSIF _REC_TMP_PAGOS.ID_SatMetodosPago = '03' --Es una transferencia
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP
						SELECT _contPart, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.Total, Descripcion, ID_SatBanco, _Fecha,  _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.BancoExt, BancoExt, _moneda_ce, _TC
						FROM TBL_BANCOS_CUENTAS 
						WHERE Tipo = _BanCaj and Clave = _REC_TMP_PAGOS.ID_BanCaj;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				ELSE -- BanCaj = 1 "Caja"
					IF _REC_TMP_PAGOS.ID_SatMetodosPago = '02' -- se trata de un cheque (Deposito o Retiro es lo mismo, toma los datos directos capturados en el dialogo)
					THEN
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP
						SELECT _contPart, _REC_TMP_PAGOS.Cheque, _REC_TMP_PAGOS.ID_SatBanco, _REC_TMP_PAGOS.CuentaBanco, _Fecha, _REC_TMP_PAGOS.Total, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.BancoExt, _moneda_ce, _TC;
					ELSE --Es otro metodo de pago (Deposito o Retiro es lo mismo)
						INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP
						SELECT _contPart, _REC_TMP_PAGOS.ID_SatMetodosPago, _Fecha, _Beneficiario, _RFCOrigen, _REC_TMP_PAGOS.Total, _moneda_ce, _TC;
					END IF;
				END IF;
			END LOOP;

			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES ( 
					id_part smallint NOT NULL,
					num character varying(20) NOT NULL,
					banco character(3) NOT NULL,
					ctaori character varying(50) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					monto numeric(19,4) NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					banemisext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				); 
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS ( 
					id_part smallint NOT NULL,
					ctaori character varying(50) NOT NULL,
					bancoori character(3) NOT NULL,
					monto numeric(19,4) NOT NULL,
					ctadest character varying(50) NOT NULL,
					bancodest character(3) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					bancooriext character varying(150) NOT NULL,
					bancodestext character varying(150) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO (
					id_part smallint NOT NULL,
					metpagopol character(2) NOT NULL,
					fecha timestamp without time zone NOT NULL,
					benef character varying(300) NOT NULL,
					rfc character varying(13) NOT NULL,
					monto numeric(19,4) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL 
				); 
				-- Fin de la tabla temporal

				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;

				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;
	
				-- Agrega ahora la poliza principal
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('IG', _Fecha, _Concepto,'0', '', _TotalPesos, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				END IF;
				
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES; 
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
			END IF;

			--Ahora ejecuta los movimientos de caja y bancos
			_clase := '';
			IF _err = 0
			THEN
				FOR _REC_TMP_PAGOS IN ( select * from _TMP_PAGOS order by Partida asc ) 
				LOOP
					_BanCaj := _REC_TMP_PAGOS.ID_FormaPago; -- 1 cajas 0 bancos

					_Beneficiario := ( select VAlfanumerico from TBL_VARIABLES where ID_Variable = 'EMPRESA');
					_RFC := _RFCOrigen;
					
					SELECT * INTO _errpart, _resultpart, _bancajmov 
					FROM  sp_bancos_movs_agregar( _BanCaj, _REC_TMP_PAGOS.ID_BanCaj, _Fecha, _Concepto, _Beneficiario, _REC_TMP_PAGOS.Total, 0.00, _REC_TMP_PAGOS.TipoMov, 
						'G', _Moneda, _TC, _Ref, _REC_TMP_PAGOS.RefPago, _id_clasificacion, _numpol, _REC_TMP_PAGOS.ID_SatBanco, _RFC, _REC_TMP_PAGOS.ID_SatMetodosPago, 
																		_REC_TMP_PAGOS.BancoExt, _REC_TMP_PAGOS.CuentaBanco, _REC_TMP_PAGOS.Cheque) as ( err integer, res varchar, clave integer);
					IF _errpart <> 0
					THEN
						_err := _errpart;
						_result := _resultpart;
						EXIT;
					END IF;

					INSERT INTO TBL_VENTAS_FACTURAS_PAGOS
					VALUES(_ID_Factura, _bancajmov);
					
					IF _numPagos > 1
					THEN
						IF _BanCaj = 0
						THEN
							_clase := _clase || 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
						ELSE
							_clase := _clase || 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||;';
						END IF;
					ELSE
						IF _BanCaj = 0
						THEN
							_clase := 'MBAN|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
						ELSE
							_clase := 'MCAJ|' || cast(_bancajmov as varchar) || '|' || cast(_REC_TMP_PAGOS.ID_BanCaj as varchar) || '||';
						END IF;
					END IF;
				END LOOP;
			END IF;
			
			IF _Fija = '0' -- Aqui registra la referencia de las polizas de ingreso a sus movimientos de caja o bancos
			THEN
				IF _numPagos > 1
				THEN
					INSERT INTO TBL_REFERENCIAS_EXT
					VALUES(default,_clase)
					RETURNING currval(pg_get_serial_sequence(' TBL_REFERENCIAS_EXT', 'id_ref')) INTO _RefExt;
					UPDATE TBL_CONT_POLIZAS
					SET Ref = 'REXT|' || cast(_RefExt as varchar) || '|||'
					WHERE ID = _numpol;
		 		ELSE
					UPDATE TBL_CONT_POLIZAS
					SET Ref = _clase
					WHERE ID = _numpol;
				END IF;
			END IF; 
		ELSIF _Condicion = 3 -- Es de ningun pago
		THEN
			_clase = 'VFAC|' || cast(_id_factura as varchar) || '|' || cast(_ID_EntidadVenta as varchar) || '||';

			-- Procede a registrar la poliza si y solo si es una entidad dinamica
			-- Primero registra la deuda total en CC_VENNP
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_VENNP, 'Documento por cobrar', _Total, _Moneda, _TC, _TotalPesos, 0.0);
					
			IF _Fija = '0'
			THEN
				-- Primero registra y crea la tabla temporal de detalle de la poliza
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (
					Part smallint NOT NULL ,
					Cuenta char(19) NOT NULL ,
					Concepto varchar(80) NOT NULL ,
					Parcial numeric(19,4) NOT NULL ,
					Moneda smallint NOT NULL ,
					TC numeric(19,4) NOT NULL ,
					Debe numeric(19,4) NOT NULL ,
					Haber numeric(19,4) NOT NULL
				);
				CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES ( 
					id_part smallint NOT NULL, 
					uuid_cfdi character(36) NOT NULL, 
					monto numeric(19,4) NOT NULL, 
					rfc character varying(13) NOT NULL,
					id_tipo character varying(10) NOT NULL,
					moneda character(3) NOT NULL,
					tipcamb numeric(19,5) NOT NULL,
					cfd_cbb_serie character varying(10) NOT NULL,
					cfd_cbb_numfol integer NOT NULL,
					numfactext character varying(36) NOT NULL,
					taxid character varying(30) NOT NULL
 				); 
				-- Fin de la tabla temporal

				-- si existen diferencias de centavos al convertir en moneda nacional los tipos de cambio, los registra
				_TotHaber := (select sum(Haber) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				_TotDebe := (select sum(Debe) from _TMP_CONT_POLIZAS_DETALLE_TMP);
				IF _TotDebe > _TotHaber
				THEN
					_diff := _TotDebe - _TotHaber;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCAF, 'Diferencia en decimales', _diff, 1, 1.0, 0.0, _diff);
				ELSIF _TotDebe < _TotHaber
				THEN
					_diff := _TotHaber - _TotDebe;
					_contPart := _contPart + 1;
					INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
					VALUES(_contPart, _CC_DCEC, 'Diferencia en decimales', _diff, 1, 1.0, _diff, 0.0);
				END IF;
					
				-- Inserta desde _TMP_CONT_POLIZAS_DETALLE_TMP
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_TMP;
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES
				SELECT * FROM  _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;

				-- Agrega ahora la poliza principal
				--EXEC dbo.sp_cont_polizas_agregar_ext 'DR', @Fecha, @concepto, 0, @clase, @errpart OUTPUT, @numpol OUTPUT
				SELECT * INTO _errpart, _resultpart, _numpol 
				FROM sp_cont_polizas_agregar('DR', _Fecha, _Concepto,'0', _clase, _Cantidad, _id_clasificacion ) as ( err integer, res varchar, clave integer );
      				IF _errpart <> 0
				THEN
					_err := _errpart;
					_result := _resultpart;
				ELSE
					UPDATE TBL_VENTAS_FACTURAS_CAB
					SET ID_Pol = _numpol
					WHERE ID_VC = _ID_Factura;
				END IF;
				
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
				DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES;
				
			END IF;
			
		END IF;
		
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_CHEQUES_TMP; 
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_TRANSFERENCIAS_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_OTRMETODOPAGO_TMP;
		DROP TABLE _TMP_CONT_POLIZAS_DETALLE_CE_COMPROBANTES_TMP;

		--Procede a agregar el movimiento al almacén
		IF _err = 0 AND _FijaCost = '0' AND _RemisionAsociada = '0' and (select count(*) from _TMP_VENTAS_FACTURAS_DET where Tipo = 'P') > 0
		THEN
			CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (
				ID_Bodega smallint NOT NULL ,
				ID_Prod varchar(20) NOT NULL ,
				Partida smallint NOT NULL ,
				Cantidad numeric(9, 3) NOT NULL ,
				Costo numeric(19,4) NULL 
			); 

			insert into _TMP_INVSERV_ALMACEN_MOVIM_DET
			select _ID_Bodega, ID_Prod, Partida, Cantidad, Precio --Precio no es el costo, sin embargo lo ignorará en la tarjeta de almacen y se calculará el costo porque el concepto de venta 50, indica recalcular costo
			from _TMP_VENTAS_FACTURAS_DET
			where Tipo = 'P'
			order by Partida ASC;
		
			SELECT * INTO _errpart, _resultpart, _ID_Movimiento 
			FROM sp_invserv_alm_movs_agregar(_Fecha, _ID_Bodega, (case when _AuditarAlm = '1' then 'P' else 'U' end), '50', _ConceptoCost, '',/*1 ENT 2 SAL*/ '2', _Ref, 'VFAC', _ID_Factura) as ( err integer, res varchar, clave integer );
			IF _errpart <> 0
			THEN
				_err := _errpart;
				_result := _resultpart;
			ELSE
				UPDATE TBL_VENTAS_FACTURAS_CAB
				SET ID_PolCost = _ID_Movimiento
				WHERE ID_VC = _ID_Factura;
			END IF;
	
			DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET; 
		END IF;
		-- Fin del movimiento al almacen
		
    --Procede a agregar la información para comercio exterior en caso de ser importación
		IF _err = 0 AND _Moneda <> 1 AND _Pais <> 'MEX' and _Pedimento <> '--'
		THEN
      INSERT INTO TBL_VENTAS_FACTURAS_COMEXT_CAB(id_vc,tipooperacion,clavedepedimento,certificadoorigen,numcertificadoorigen,numeroexportadorconfiable,
        incoterm,subdivision,observaciones,tipocambiousd,totalusd,emisor_curp,receptor_curp,receptor_numregidtrib,destinatario_numregidtrib,destinatario_rfc,
        destinatario_curp,destinatario_nombre,destinatario_domicilio_calle,destinatario_domicilio_numeroexterior,destinatario_domicilio_numerointerior,
        destinatario_domicilio_colonia,destinatario_domicilio_localidad,destinatario_domicilio_referencia,destinatario_domicilio_municipio,
        destinatario_domicilio_estado,destinatario_domicilio_pais,destinatario_domicilio_codigopostal)
      SELECT _ID_Factura, '-', _Pedimento, -1, '', '', 
        '', -1, _Obs, _TC, _Importe, '', '', registro_tributario, registro_tributario, '', 
        '', nombre, direccion, noext, noint, 
        colonia, poblacion, '', municipio,
        estado, pais, cp
      FROM TBL_CLIENT_CLIENT
      WHERE ID_Tipo = 'CL' and ID_Clave = _ID_Cliente;
      
      -- inserta el detalle
      INSERT INTO TBL_VENTAS_FACTURAS_COMEXT_DET(id_vc,partida,noidentificacion,fraccionarancelaria,cantidadaduana,unidadaduana,valorunitarioaduana,valordolares) 
      SELECT _ID_Factura, det.Partida, det.ID_Prod, '', det.Cantidad, (select coalesce(id_satunidad,99) from tbl_invserv_unidades where id_unidad = i.id_unidadsalida), round(det.Precio - det.Descuento,2), round(det.Importe - det.ImporteDesc,2)
      FROM _TMP_VENTAS_FACTURAS_DET det INNER JOIN TBL_INVSERV_INVENTARIOS i 
        ON det.ID_Prod = i.ID_Prod
      WHERE det.Tipo = 'P';
        
      -- inserta el detalle
      INSERT INTO TBL_VENTAS_FACTURAS_COMEXT_DET_DESCESP(id_vc,partida,descripcion,marca,modelo,submodelo,numeroserie)
      SELECT _ID_Factura, Partida, 1, '', '', '', ''
      FROM _TMP_VENTAS_FACTURAS_DET
      WHERE Tipo = 'P';
		END IF;
		--Fin de comercio exterior
	END IF; 
	
	RETURN QUERY SELECT _err, _result, _id_factura;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_ventas_facturas_agregar(smallint, integer, integer, timestamp without time zone, character varying, smallint, numeric, smallint, character varying, numeric, numeric, numeric, numeric, numeric, numeric, numeric, numeric, smallint, integer, smallint, character, character, numeric, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ventas_facturas_comext(
    _id_vc integer,
    _tipooperacion character,
    _certificadoorigen smallint,
    _numcertificadoorigen character varying,
    _numeroexportadorconfiable character varying,
    _incoterm character varying,
    _subdivision smallint,
    _observaciones character varying,
    _tipocambiousd numeric,
    _totalusd numeric,
    _emisor_curp character varying,
    _receptor_curp character varying,
    _receptor_numregidtrib character varying,
    _destinatario_numregidtrib character varying,
    _destinatario_rfc character varying,
    _destinatario_curp character varying,
    _destinatario_nombre character varying,
    _destinatario_domicilio_calle character varying,
    _destinatario_domicilio_numeroexterior character varying,
    _destinatario_domicilio_numerointerior character varying,
    _destinatario_domicilio_colonia character varying,
    _destinatario_domicilio_localidad character varying,
    _destinatario_domicilio_referencia character varying,
    _destinatario_domicilio_municipio character varying,
    _destinatario_domicilio_estado character varying,
    _destinatario_domicilio_pais character,
    _destinatario_domicilio_codigopostal character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _Fecha timestamp; _Mes smallint; _Ano smallint;
BEGIN
	_err := 0;
	_result := 'El complemento de exportación para esta factura se actualizó con éxito';

  _Fecha := (select Fecha from tbl_ventas_facturas_cab where id_vc = _id_vc);
  _Mes := date_part('month',_Fecha);
	_Ano := date_part('year',_Fecha);
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

  IF _err = 0
	THEN
    delete from tbl_ventas_facturas_comext_det_descesp
    where id_vc = _id_vc;

    delete from tbl_ventas_facturas_comext_det
    where id_vc = _id_vc;

    update tbl_ventas_facturas_comext_cab
    set tipooperacion = _tipooperacion,
        certificadoorigen = _certificadoorigen,
        numcertificadoorigen = _numcertificadoorigen,
        numeroexportadorconfiable = _numeroexportadorconfiable,
        incoterm = _incoterm,
        subdivision = _subdivision,
        observaciones = _observaciones,
        tipocambiousd = _tipocambiousd,
        totalusd = _totalusd,
        emisor_curp = _emisor_curp,
        receptor_curp = _receptor_curp,
        receptor_numregidtrib = _receptor_numregidtrib,
        destinatario_numregidtrib = _destinatario_numregidtrib,
        destinatario_rfc = _destinatario_rfc,
        destinatario_curp = _destinatario_curp,
        destinatario_nombre = _destinatario_nombre,
        destinatario_domicilio_calle = _destinatario_domicilio_calle,
        destinatario_domicilio_numeroexterior = _destinatario_domicilio_numeroexterior,
        destinatario_domicilio_numerointerior = _destinatario_domicilio_numerointerior,
        destinatario_domicilio_colonia = _destinatario_domicilio_colonia,
        destinatario_domicilio_localidad = _destinatario_domicilio_localidad,
        destinatario_domicilio_referencia = _destinatario_domicilio_referencia,
        destinatario_domicilio_municipio = _destinatario_domicilio_municipio,
        destinatario_domicilio_estado = _destinatario_domicilio_estado,
        destinatario_domicilio_pais = _destinatario_domicilio_pais,
        destinatario_domicilio_codigopostal = _destinatario_domicilio_codigopostal
    where id_vc = _id_vc;

    insert into tbl_ventas_facturas_comext_det(id_vc,partida,noidentificacion,fraccionarancelaria,cantidadaduana,unidadaduana,valorunitarioaduana,valordolares) 
    select _id_vc, partida, noidentificacion, fraccionarancelaria, cantidadaduana, unidadaduana, valorunitarioaduana, valordolares
    from _tmp_ventas_facturas_comext_det;

    insert into tbl_ventas_facturas_comext_det_descesp(id_vc,partida,descripcion,marca,modelo,submodelo,numeroserie)
    select _id_vc, partida, descripcion, marca, modelo, submodelo, numeroserie
    from _tmp_ventas_facturas_comext_det_descesp;

  END IF;
  
  RETURN QUERY SELECT _err, _result, _id_vc;
  
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_ventas_facturas_comext(integer, character, smallint, character varying, character varying, character varying, smallint, character varying, numeric, numeric, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
ALTER TABLE tbl_invserv_unidades
ADD id_satunidad smallint;

UPDATE tbl_invserv_unidades
SET id_satunidad = 99;

ALTER TABLE tbl_invserv_unidades
ALTER COLUMN id_satunidad SET not null;

--@FIN_BLOQUE
DROP FUNCTION sp_invserv_linuni_agregar(character varying, character, character varying, character varying);

CREATE OR REPLACE FUNCTION sp_invserv_linuni_agregar(
    _entidad character varying,
    _tipo character varying,
    _clave character varying,
    _descripcion character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	IF _Entidad = 'LINEAS'
	THEN
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'INVSERV_LINEAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');
	
		IF(select count(*) from TBL_INVSERV_LINEAS where ID_Linea = _Clave) > 0
		THEN
			_err := 3;
			_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'INVSERV_LINEAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
		END IF;

		IF _err = 0
		THEN
			INSERT INTO TBL_INVSERV_LINEAS
			VALUES(_Clave, _Tipo, _Descripcion);
		END IF;
	ELSE -- UNIDADES
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'INVSERV_LINEAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');
	
		IF(select count(*) from TBL_INVSERV_UNIDADES where ID_Unidad = _Clave) > 0
		THEN
			_err := 3;
			_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'INVSERV_LINEAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
		END IF;

		IF _err = 0
		THEN
			INSERT INTO TBL_INVSERV_UNIDADES
			VALUES(_Clave, 'P', _Descripcion, _Tipo::smallint);
		END IF;
	END IF;
	
	RETURN QUERY SELECT _err, _result, _clave;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_invserv_linuni_agregar(character varying, character varying, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_invserv_linuni_cambiar(character varying, character, character varying, character varying);

CREATE OR REPLACE FUNCTION sp_invserv_linuni_cambiar(
    _entidad character varying,
    _tipo character varying,
    _clave character varying,
    _descripcion character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	IF _Entidad = 'LINEAS'
	THEN
		_result := (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'INVSERV_LINEAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');
	
		IF(select count(*) from TBL_INVSERV_LINEAS where ID_Linea = _Clave) < 1
		THEN
			_err := 3;
			_result := (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'INVSERV_LINEAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
		END IF;

		IF _err = 0
		THEN
			UPDATE TBL_INVSERV_LINEAS
			SET Descripcion = _Descripcion
			WHERE ID_Linea = _Clave;
		END IF;
	ELSE -- UNIDADES
		_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'INVSERV_LINEAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');
	
		IF(select count(*) from TBL_INVSERV_UNIDADES where ID_Unidad = _Clave) < 1
		THEN
			_err := 3;
			_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'INVSERV_LINEAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
		END IF;

		IF _err = 0
		THEN
			UPDATE TBL_INVSERV_UNIDADES
			SET Descripcion = _Descripcion, ID_SatUnidad = _Tipo::smallint
			WHERE ID_Unidad = _Clave;
		END IF;
	END IF;
	
	RETURN QUERY SELECT _err, _result, _clave;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_invserv_linuni_cambiar(character varying, character varying, character varying, character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cfd_ventas_facturas_cab_generar AS 
 SELECT ve.id_entidadventa, ve.descripcion, ve.fija, ve.iva AS iva_entidad, ve.cfd, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_serie
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE ''::character varying
        END AS cfd_serie, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_folio
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_folio, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_folioini
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_folioini, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_foliofin
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_foliofin, ve.cfd_noaprobacion, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN (( SELECT tbl_cfd_folios.cfd_anoaprobacion
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion))::integer
            ELSE 2000
        END AS cfd_anoaprobacion, ve.cfd_nocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivocertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN (( SELECT tbl_cfd_certificados.cfd_caducidadcertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text))::timestamp with time zone
            ELSE now()
        END AS cfd_caducidadcertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivollave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivollave, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_clavellave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_clavellave, ve.cfd_id_expedicion, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_estado
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_pais
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_cp, fc.id_vc AS id_factura, fc.numero, fc.id_clipro AS id_cliente, fc.fecha, fc.referencia, fc.status, fc.moneda, mon.moneda AS monedasim, fc.tc, fc.fechaenvio, fc.condicion, fc.obs, fc.importe, fc.descuento, fc.subtotal, fc.iva, fc.ieps, fc.ivaret, fc.isrret, fc.total, fc.ref, fc.id_pol, fc.id_polcost, fc.id_bodega, fc.mimporte, fc.mdescuento, fc.msubtotal, fc.miva, fc.mtotal, fc.efectivo, fc.bancos, fc.cambio, fc.id_vendedor, 
        CASE
            WHEN fc.id_clipro = 0 THEN 'Cliente de Mostrador'::character varying
            ELSE ( SELECT p.nombre
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS nombre, 
        CASE
            WHEN fc.id_clipro = 0 THEN 'XAXX010101000'::character varying
            ELSE ( SELECT p.rfc
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS rfc, 
        CASE
            WHEN fc.id_clipro = 0 THEN 0
            ELSE (( SELECT p.dias
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar))::integer
        END AS diascredito, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.direccion
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS calle, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.noext
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS noext, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.noint
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS noint, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.colonia
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS colonia, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying(80)
            ELSE ( SELECT p.poblacion
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS localidad, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.municipio
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS municipio, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.estado
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS estado, 
        CASE
            WHEN fc.id_clipro = 0 THEN 'Mexico'::character varying
            ELSE ( SELECT p.pais
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS pais, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.cp
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS cp, 
        CASE
            WHEN fc.id_clipro = 0 THEN getcfdimetodopago('I'::character varying, fc.id_vc, fc.condicion, fc.efectivo, fc.bancos, ''::character varying)
            ELSE getcfdimetodopago('I'::character varying, fc.id_vc, fc.condicion, fc.efectivo, fc.bancos, ( SELECT p.metododepago
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar))
        END AS metododepago, mon.id_satmoneda AS monedasat
   FROM tbl_ventas_facturas_cab fc
   JOIN tbl_ventas_entidades ve ON fc.id_entidad = ve.id_entidadventa
   JOIN tbl_cont_monedas mon ON fc.moneda = mon.clave;

ALTER TABLE view_cfd_ventas_facturas_cab_generar
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cfd_invserv_traspasos_cab_generar AS 
 SELECT ve.id_bodega AS id_entidadventa, ve.descripcion, 0 AS fija, 0.0 AS iva_entidad, ve.cfd, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_serie
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE ''::character varying
        END AS cfd_serie, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_folio
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_folio, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_folioini
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_folioini, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_foliofin
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_foliofin, ve.cfd_noaprobacion, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN (( SELECT tbl_cfd_folios.cfd_anoaprobacion
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion))::integer
            ELSE 2000
        END AS cfd_anoaprobacion, ve.cfd_nocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivocertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN (( SELECT tbl_cfd_certificados.cfd_caducidadcertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text))::timestamp with time zone
            ELSE now()
        END AS cfd_caducidadcertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivollave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivollave, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_clavellave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_clavellave, ve.cfd_id_expedicion, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_estado
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_pais
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_cp, fc.id_movimiento AS id_factura, fc.salida AS numero, fc.id_bodegadest AS id_cliente, fc.fecha, fc.referencia, fc.status, 1 AS moneda, 'Pesos'::character varying AS monedasim, 1 AS tc, fc.fecha AS fechaenvio, 0 AS condicion, fc.concepto AS obs, 0.0 AS importe, 0.0 AS descuento, 0.0 AS subtotal, 0.0 AS iva, 0.0 AS ieps, 0.0 AS ivaret, 0.0 AS isrret, 0.0 AS total, fc.referencia AS ref, (-1) AS id_pol, (-1) AS id_polcost, fc.id_bodega, 0.0 AS mimporte, 0.0 AS mdescuento, 0.0 AS msubtotal, 0.0 AS miva, 0.0 AS mtotal, 0.0 AS efectivo, 0.0 AS bancos, 0.0 AS cambio, 0 AS id_vendedor, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_nombre
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS nombre, ''::character varying AS rfc, 0 AS diascredito, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS calle, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS noext, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS noint, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS colonia, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS localidad, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS municipio, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_estado
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS estado, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_pais
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS pais, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = fc.id_bodegadest)))
            ELSE ''::character varying
        END AS cp, getcfdimetodopago('T'::character varying, fc.id_movimiento, 1::smallint, 0.0, 0.0, ''::character varying) AS metododepago, 'MXN'::character(3) AS monedasat
   FROM tbl_invserv_almacen_bod_mov_cab fc
   JOIN tbl_invserv_bodegas ve ON fc.id_bodega = ve.id_bodega;

ALTER TABLE view_cfd_invserv_traspasos_cab_generar
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP VIEW view_cfd_nomina_recibos_cab_generar;

CREATE OR REPLACE VIEW view_cfd_nomina_recibos_cab_generar AS 
 SELECT ve.id_sucursal, ve.descripcion, ve.cfd, ''::character varying AS cfd_serie, 0 AS cfd_folio, 0 AS cfd_folioini, 0 AS cfd_foliofin, 0 AS cfd_noaprobacion, 2000 AS cfd_anoaprobacion, ve.cfd_nocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivocertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN (( SELECT tbl_cfd_certificados.cfd_caducidadcertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text))::timestamp with time zone
            ELSE now()
        END AS cfd_caducidadcertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivollave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivollave, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_clavellave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_clavellave, ve.cfd_id_expedicion, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_estado
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_pais
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_cp, fc.id_nomina, fc.numero_nomina AS numero, fc.ano, fc.tipo, fc.fecha_desde, fc.fecha_hasta, fc.dias, fc.cerrado, fc.mes, fc.status, fc.formapago, fc.id_mov, fc.id_pol, 1 AS moneda, 'Pesos'::character varying AS monedasim, 1 AS tc, 0 AS condicion, esp.id_empleado, esp.gravado + esp.exento AS importe, esp.deduccion - esp.isr AS descuento, esp.gravado + esp.exento AS subtotal, esp.isr, esp.gravado + esp.exento + esp.deduccion AS total, (((emp.nombre::text || ' '::text) || emp.apellido_paterno::text) || ' '::text) || emp.apellido_materno::text AS nombre, ((emp.rfc_letras::text || emp.rfc_fecha::text) || emp.rfc_homoclave::text)::character varying AS rfc, emp.calle, emp.numero AS noext, emp.noint, emp.colonia, emp.localidad, emp.delegacion AS municipio, emp.estado, emp.pais, emp.codigo_postal AS cp, 
        CASE
            WHEN emp.cuenta_bancaria::text = ''::text THEN '01'::text
            ELSE '03'::text
        END AS metododepago, 'MXN'::character(3) AS monedasat, emp.curp, emp.regimen AS tiporegimen, emp.num_registro_imss AS numseguridadsocial, esp.dias - esp.faltas AS numdiaspagados, dep.nombre AS departamento, emp.cuenta_bancaria AS clabe, emp.id_satbanco AS banco, emp.fecha_de_ingreso AS fechainiciorellaboral, emp.puesto, 
        CASE
            WHEN ve.periodo = 'sem'::bpchar THEN 'semanal'::text
            WHEN ve.periodo = 'qui'::bpchar THEN 'quincenal'::text
            ELSE 'mensual'::text
        END AS periodicidadpago, esp.gravado AS totalgravado, esp.exento AS totalexento, esp.deduccion AS totaldeducciones, esp.deduccion AS totaldedgravadas, 0.0 AS totaldedexentas, esp.he AS horasextras, esp.ht AS horastriples, esp.hd AS horasdomingo, esp.ixa, esp.ixe, esp.ixm, esp.diashorasextras
   FROM tbl_nom_calculo_nomina fc
   JOIN tbl_companias ve ON fc.id_compania = ve.id_compania AND fc.id_sucursal = ve.id_sucursal
   JOIN tbl_nom_calculo_nomina_esp esp ON fc.id_nomina = esp.id_nomina
   JOIN tbl_nom_masemp emp ON esp.id_empleado = emp.id_empleado
   JOIN tbl_nom_departamentos dep ON emp.id_departamento = dep.id_departamento;

ALTER TABLE view_cfd_nomina_recibos_cab_generar
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cfd_ventas_remisiones_cab_generar AS 
 SELECT ve.id_entidadventa, ve.descripcion, ve.fija, ve.iva AS iva_entidad, ve.cfd, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_serie
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE ''::character varying
        END AS cfd_serie, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_folio
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_folio, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_folioini
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_folioini, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_foliofin
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_foliofin, ve.cfd_noaprobacion, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN (( SELECT tbl_cfd_folios.cfd_anoaprobacion
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion))::integer
            ELSE 2000
        END AS cfd_anoaprobacion, ve.cfd_nocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivocertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN (( SELECT tbl_cfd_certificados.cfd_caducidadcertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text))::timestamp with time zone
            ELSE now()
        END AS cfd_caducidadcertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivollave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivollave, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_clavellave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_clavellave, ve.cfd_id_expedicion, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_estado
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_pais
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_cp, fc.id_vc AS id_factura, fc.numero, fc.id_clipro AS id_cliente, fc.fecha, fc.referencia, fc.status, fc.moneda, mon.moneda AS monedasim, fc.tc, fc.fechaenvio, fc.condicion, fc.obs, fc.importe, fc.descuento, fc.subtotal, fc.iva, fc.ieps, fc.ivaret, fc.isrret, fc.total, fc.ref, fc.id_pol, fc.id_polcost, fc.id_bodega, fc.mimporte, fc.mdescuento, fc.msubtotal, fc.miva, fc.mtotal, fc.efectivo, fc.bancos, fc.cambio, fc.id_vendedor, 
        CASE
            WHEN fc.id_clipro = 0 THEN 'Cliente de Mostrador'::character varying
            ELSE ( SELECT p.nombre
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS nombre, 
        CASE
            WHEN fc.id_clipro = 0 THEN 'XAXX010101000'::character varying
            ELSE ( SELECT p.rfc
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS rfc, 
        CASE
            WHEN fc.id_clipro = 0 THEN 0
            ELSE (( SELECT p.dias
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar))::integer
        END AS diascredito, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.direccion
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS calle, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.noext
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS noext, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.noint
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS noint, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.colonia
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS colonia, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying(80)
            ELSE ( SELECT p.poblacion
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS localidad, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.municipio
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS municipio, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.estado
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS estado, 
        CASE
            WHEN fc.id_clipro = 0 THEN 'Mexico'::character varying
            ELSE ( SELECT p.pais
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS pais, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.cp
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS cp, 
        CASE
            WHEN fc.id_clipro = 0 THEN getcfdimetodopago('T'::character varying, fc.id_vc, fc.condicion, fc.efectivo, fc.bancos, ''::character varying)
            ELSE getcfdimetodopago('T'::character varying, fc.id_vc, fc.condicion, fc.efectivo, fc.bancos, ( SELECT p.metododepago
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar))
        END AS metododepago, mon.id_satmoneda AS monedasat
   FROM tbl_ventas_remisiones_cab fc
   JOIN tbl_ventas_entidades ve ON fc.id_entidad = ve.id_entidadventa
   JOIN tbl_cont_monedas mon ON fc.moneda = mon.clave;

ALTER TABLE view_cfd_ventas_remisiones_cab_generar
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_cfd_ventas_devoluciones_cab_generar AS 
 SELECT ve.id_entidadventa, ve.descripcion, ve.fija, ve.iva AS iva_entidad, ve.cfd, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_serie
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE ''::character varying
        END AS cfd_serie, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_folio
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_folio, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_folioini
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_folioini, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN ( SELECT tbl_cfd_folios.cfd_foliofin
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion)
            ELSE 0
        END AS cfd_foliofin, ve.cfd_noaprobacion, 
        CASE
            WHEN ve.cfd_noaprobacion <> 0 THEN (( SELECT tbl_cfd_folios.cfd_anoaprobacion
               FROM tbl_cfd_folios
              WHERE tbl_cfd_folios.cfd_noaprobacion = ve.cfd_noaprobacion))::integer
            ELSE 2000
        END AS cfd_anoaprobacion, ve.cfd_nocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivocertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivocertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN (( SELECT tbl_cfd_certificados.cfd_caducidadcertificado
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text))::timestamp with time zone
            ELSE now()
        END AS cfd_caducidadcertificado, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_archivollave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_archivollave, 
        CASE
            WHEN ve.cfd_nocertificado::text <> ''::text THEN ( SELECT tbl_cfd_certificados.cfd_clavellave
               FROM tbl_cfd_certificados
              WHERE tbl_cfd_certificados.cfd_nocertificado::text = ve.cfd_nocertificado::text)
            ELSE ''::character varying
        END AS cfd_clavellave, ve.cfd_id_expedicion, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_estado
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_pais
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_cp, fc.id_vc AS id_factura, fc.numero, fc.id_clipro AS id_cliente, fc.fecha, fc.referencia, fc.status, fc.moneda, mon.moneda AS monedasim, fc.tc, fc.fechaenvio, fc.condicion, fc.obs, fc.importe, fc.descuento, fc.subtotal, fc.iva, fc.ieps, fc.ivaret, fc.isrret, fc.total, fc.ref, fc.id_pol, fc.id_polcost, fc.id_bodega, fc.mimporte, fc.mdescuento, fc.msubtotal, fc.miva, fc.mtotal, fc.efectivo, fc.bancos, fc.cambio, fc.id_vendedor, 
        CASE
            WHEN fc.id_clipro = 0 THEN 'Cliente de Mostrador'::character varying
            ELSE ( SELECT p.nombre
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS nombre, 
        CASE
            WHEN fc.id_clipro = 0 THEN 'XAXX010101000'::character varying
            ELSE ( SELECT p.rfc
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS rfc, 
        CASE
            WHEN fc.id_clipro = 0 THEN 0
            ELSE (( SELECT p.dias
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar))::integer
        END AS diascredito, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.direccion
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS calle, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.noext
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS noext, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.noint
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS noint, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.colonia
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS colonia, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying(80)
            ELSE ( SELECT p.poblacion
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS localidad, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.municipio
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS municipio, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.estado
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS estado, 
        CASE
            WHEN fc.id_clipro = 0 THEN 'Mexico'::character varying
            ELSE ( SELECT p.pais
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS pais, 
        CASE
            WHEN fc.id_clipro = 0 THEN ''::character varying
            ELSE ( SELECT p.cp
               FROM tbl_client_client p
              WHERE fc.id_clipro = p.id_clave AND p.id_tipo = 'CL'::bpchar)
        END AS cp, 
        CASE
            WHEN fc.id_clipro = 0 THEN getcfdimetodopago('E'::character varying, fc.id_vc, fc.condicion, fc.efectivo, fc.bancos, ''::character varying)
            ELSE getcfdimetodopago('E'::character varying, fc.id_vc, fc.condicion, fc.efectivo, fc.bancos, ( SELECT tbl_variables.valfanumerico
               FROM tbl_variables
              WHERE tbl_variables.id_variable::text = 'DSV-METPAG'::text))
        END AS metododepago, mon.id_satmoneda AS monedasat
   FROM tbl_ventas_devoluciones_cab fc
   JOIN tbl_ventas_entidades ve ON fc.id_entidad = ve.id_entidadventa
   JOIN tbl_cont_monedas mon ON fc.moneda = mon.clave;

ALTER TABLE view_cfd_ventas_devoluciones_cab_generar
  OWNER TO [[owner]];

--@FIN_BLOQUE
INSERT INTO tbl_variables(id_variable, descripcion, ventero, vdecimal, vfecha, valfanumerico, desistema, modulo)
VALUES('DSV-METPAG','ALFA|2|2|-|-', null, null, null, '03', '1','VEN');

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getcfdimetodopago(
    _igegtr character varying,
    _id_vc integer,
    _condicion smallint,
    _efectivo numeric,
    _bancos numeric,
    _cl_metododepago character varying)
  RETURNS text AS
$BODY$
DECLARE
	_resultado text; _referencia text;
BEGIN
	_resultado := '';

	IF _igegtr <> 'T' --No es traslado
	THEN
		IF _condicion = 0 --Es CFDI de contado... Ya sea Ingreso o Egreso
		THEN 
			IF _igegtr = 'I'
			THEN
				FOR _referencia
				IN 
					select coalesce(s.clave,'99') as referencia
          from tbl_ventas_facturas_pagos f
            join tbl_bancos_movimientos b on f.id_mov = b.id
            left outer join tbl_sat_metodospago_cfdi s on b.id_satmetodospago = s.clave
          where f.id_factura = _id_vc
          order by b.deposito desc
				LOOP
					_resultado := _resultado || ',' || _referencia; 
				END LOOP;
			ELSIF _igegtr = 'E'
			THEN
				FOR _referencia
				IN 
					select coalesce(s.clave,'99') as referencia
          from tbl_ventas_devoluciones_pagos f
						join tbl_bancos_movimientos b on f.id_mov = b.id
						left outer join tbl_sat_metodospago_cfdi s on b.id_satmetodospago = s.clave
					where f.id_devolucion = _id_vc
					order by b.retiro desc
				LOOP
					_resultado := _resultado || ',' || _referencia; 
				END LOOP;
			END IF;
		ELSE --Si es un ingreso o egreso de credito, obtendrá el método de pago mandado desde el cabecero de factura o devolucion
			_resultado := _cl_metododepago;
		END IF;
	END IF;

	IF _resultado is not null and _resultado <> ''
	THEN
		_resultado := TRIM(_resultado);
		IF substring(_resultado from 1 for 1) = ','
		THEN
      _resultado := overlay(_resultado placing '' from 1 for 1);
		END IF;
	END IF;

	IF _resultado is null or _resultado = '' --En traslados será cadena vacía
	THEN
		_resultado := '99'; --99 es el metodo de pago de cfdi que no aplica. Se ajustara por lo regular a traslados
	END IF;

	return _resultado;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getcfdimetodopago(character varying, integer, smallint, numeric, numeric, character varying)
  OWNER TO [[owner]];
  
--@FIN_BLOQUE
CREATE TABLE tbl_sat_paises
(
  alfa2 character(2) NOT NULL,
  alfa3 character(3) NOT NULL,
  numerico smallint NOT NULL,
  nombre character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_sat_paises PRIMARY KEY (alfa2)
);

--@FIN_BLOQUE
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Afganistán','AF','AFG','004');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Aland','AX','ALA','248');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Albania','AL','ALB','008');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Alemania','DE','DEU','276');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Andorra','AD','AND','020');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Angola','AO','AGO','024');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Anguila','AI','AIA','660');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Antártida','AQ','ATA','010');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Antigua y Barbuda','AG','ATG','028');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Arabia Saudita','SA','SAU','682');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Argelia','DZ','DZA','012');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Argentina','AR','ARG','032');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Armenia','AM','ARM','051');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Aruba','AW','ABW','533');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Australia','AU','AUS','036');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Austria','AT','AUT','040');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Azerbaiyán','AZ','AZE','031');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Las Bahamas','BS','BHS','044');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bangladesh','BD','BGD','050');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Barbados','BB','BRB','052');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bahrein','BH','BHR','048');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bélgica','BE','BEL','056');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Belice','BZ','BLZ','084');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Benin','BJ','BEN','204');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bermudas','BM','BMU','060');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Belarús','BY','BLR','112');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bolivia','BO','BOL','068');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Eustaquio y Saba','BQ','BES','535');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bosnia y Herzegovina','BA','BIH','070');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Botswana','BW','BWA','072');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Brasil','BR','BRA','076');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Brunei Darussalam','BN','BRN','096');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bulgaria','BG','BGR','100');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Burkina Faso','BF','BFA','854');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Burundi','BI','BDI','108');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bhután','BT','BTN','064');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Cabo Verde','CV','CPV','132');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Camboya','KH','KHM','116');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Camerún','CM','CMR','120');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Canadá','CA','CAN','124');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Qatar','QA','QAT','634');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Chad','TD','TCD','148');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Chile','CL','CHL','152');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('China','CN','CHN','156');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Chipre','CY','CYP','196');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Colombia','CO','COL','170');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Las Comoras','KM','COM','174');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Corea del Norte','KP','PRK','408');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Corea del Sur','KR','KOR','410');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Costa de Marfil','CI','CIV','384');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Costa Rica','CR','CRI','188');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Croacia','HR','HRV','191');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Cuba','CU','CUB','192');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Curaçao','CW','CUW','531');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Dinamarca','DK','DNK','208');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Dominica','DM','DMA','212');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Ecuador','EC','ECU','218');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Egipto','EG','EGY','818');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('El Salvador','SV','SLV','222');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Emiratos Árabes Unidos','AE','ARE','784');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Eritrea','ER','ERI','232');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Eslovaquia','SK','SVK','703');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Eslovenia','SI','SVN','705');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('España','ES','ESP','724');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Estados Unidos de América','US','USA','840');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Estonia','EE','EST','233');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Etiopía','ET','ETH','231');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Filipinas','PH','PHL','608');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Finlandia','FI','FIN','246');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Fiji','FJ','FJI','242');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Francia','FR','FRA','250');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Gabón','GA','GAB','266');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Gambia','GM','GMB','270');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Georgia','GE','GEO','268');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Ghana','GH','GHA','288');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Gibraltar','GI','GIB','292');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Granada','GD','GRD','308');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Grecia','GR','GRC','300');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Groenlandia','GL','GRL','304');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guadeloupe','GP','GLP','312');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guam','GU','GUM','316');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guatemala','GT','GTM','320');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guayana Francesa','GF','GUF','254');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guernsey','GG','GGY','831');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guinea','GN','GIN','324');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guinea Bissau','GW','GNB','624');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guinea Ecuatorial','GQ','GNQ','226');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guyana','GY','GUY','328');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Haití','HT','HTI','332');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Honduras','HN','HND','340');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Hong Kong','HK','HKG','344');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Hungría','HU','HUN','348');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('India','IN','IND','356');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Indonesia','ID','IDN','360');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Iraq','IQ','IRQ','368');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Irán','IR','IRN','364');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Irlanda','IE','IRL','372');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Isla Bouvet','BV','BVT','074');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Isla de Man','IM','IMN','833');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Isla de Navidad','CX','CXR','162');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islandia','IS','ISL','352');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Caimán','KY','CYM','136');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Cocos','CC','CCK','166');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Cook','CK','COK','184');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Feroe','FO','FRO','234');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Georgias del Sur y Sandwich del Sur','GS','SGS','239');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Heard (Isla) e Islas McDonald','HM','HMD','334');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Malvinas','FK','FLK','238');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Marianas del Norte','MP','MNP','580');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Marshall','MH','MHL','584');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Pitcairn','PN','PCN','612');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Salomón','SB','SLB','090');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Turcas y Caicos','TC','TCA','796');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas ultramarinas de Estados Unidos','UM','UMI','581');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Vírgenes Británicas','VG','VGB','092');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Vírgenes de los Estados Unidos','VI','VIR','850');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Israel','IL','ISR','376');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Italia','IT','ITA','380');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Jamaica','JM','JAM','388');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Japón','JP','JPN','392');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Jersey','JE','JEY','832');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Jordania','JO','JOR','400');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kazajstán','KZ','KAZ','398');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kenya','KE','KEN','404');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kirguistán','KG','KGZ','417');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kiribati','KI','KIR','296');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kuwait','KW','KWT','414');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Democrática Popular','LA','LAO','418');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Lesotho','LS','LSO','426');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Letonia','LV','LVA','428');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Líbano','LB','LBN','422');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Liberia','LR','LBR','430');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Libia','LY','LBY','434');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Liechtenstein','LI','LIE','438');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Lituania','LT','LTU','440');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Luxemburgo','LU','LUX','442');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Macao','MO','MAC','446');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Macedonia','MK','MKD','807');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Madagascar','MG','MDG','450');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Malasia','MY','MYS','458');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Malawi','MW','MWI','454');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Maldivas','MV','MDV','462');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Malí','ML','MLI','466');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Malta','MT','MLT','470');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Marruecos','MA','MAR','504');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Martinique','MQ','MTQ','474');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mauricio','MU','MUS','480');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mauritania','MR','MRT','478');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mayotte','YT','MYT','175');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('México','MX','MEX','484');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Micronesia','FM','FSM','583');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Moldova','MD','MDA','498');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mónaco','MC','MCO','492');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mongolia','MN','MNG','496');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Montenegro','ME','MNE','499');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Montserrat','MS','MSR','500');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mozambique','MZ','MOZ','508');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Myanmar','MM','MMR','104');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Namibia','NA','NAM','516');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nauru','NR','NRU','520');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nepal','NP','NPL','524');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nicaragua','NI','NIC','558');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Níger','NE','NER','562');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nigeria','NG','NGA','566');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Niue','NU','NIU','570');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Norfolk, Isla','NF','NFK','574');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Noruega','NO','NOR','578');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nueva Caledonia','NC','NCL','540');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nueva Zelandia','NZ','NZL','554');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Omán','OM','OMN','512');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Países Bajos','NL','NLD','528');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Pakistán','PK','PAK','586');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Palau','PW','PLW','585');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Palestina','PS','PSE','275');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Panamá','PA','PAN','591');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nueva Guinea','PG','PNG','598');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Paraguay','PY','PRY','600');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Perú','PE','PER','604');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Polinesia Francesa','PF','PYF','258');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Polonia','PL','POL','616');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Portugal','PT','PRT','620');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Puerto Rico','PR','PRI','630');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Reino Unido','GB','GBR','826');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sahara Occidental','EH','ESH','732');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Centroafricana','CF','CAF','140');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Checa','CZ','CZE','203');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República del Congo','CG','COG','178');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Democrática del Congo','CD','COD','180');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Dominicana','DO','DOM','214');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Reunión','RE','REU','638');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Rwanda','RW','RWA','646');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Rumania','RO','ROU','642');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Rusia','RU','RUS','643');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Samoa','WS','WSM','882');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Samoa Americana','AS','ASM','016');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Bartolomé','BL','BLM','652');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Cristóbal y Nieves','KN','KNA','659');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Marino','SM','SMR','674');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Martín','MF','MAF','663');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Pedro y Miquelón','PM','SPM','666');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Vicente y las Granadinas','VC','VCT','670');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Santa Elena, Ascensión y Tristán de Acuña','SH','SHN','654');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Santa Lucía','LC','LCA','662');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Santo Tomé y Príncipe','ST','STP','678');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Senegal','SN','SEN','686');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Serbia','RS','SRB','688');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Seychelles','SC','SYC','690');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sierra leona','SL','SLE','694');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Singapur','SG','SGP','702');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sint Maarten','SX','SXM','534');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Siria','SY','SYR','760');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Somalia','SO','SOM','706');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sri Lanka','LK','LKA','144');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Swazilandia','SZ','SWZ','748');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sudáfrica','ZA','ZAF','710');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sudán','SD','SDN','729');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sudán del Sur','SS','SSD','728');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Suecia','SE','SWE','752');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Suiza','CH','CHE','756');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Surinam','SR','SUR','740');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Svalbard y Jan Mayen','SJ','SJM','744');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tailandia','TH','THA','764');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Taiwán','TW','TWN','158');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tanzania','TZ','TZA','834');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tayikistán','TJ','TJK','762');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Territorio Británico del Océano Índico','IO','IOT','086');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tierras Australes Francesas','TF','ATF','260');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Timor-Leste','TL','TLS','626');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Togo','TG','TGO','768');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tokelau','TK','TKL','772');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tonga','TO','TON','776');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Trinidad y Tabago','TT','TTO','780');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Túnez','TN','TUN','788');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Turkmenistán','TM','TKM','795');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Turquía','TR','TUR','792');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tuvalu','TV','TUV','798');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Ucrania','UA','UKR','804');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Uganda','UG','UGA','800');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Uruguay','UY','URY','858');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Uzbekistán','UZ','UZB','860');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Vanuatu','VU','VUT','548');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Vaticano','VA','VAT','336');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Venezuela','VE','VEN','862');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Vietnam','VN','VNM','704');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Wallis y Futuna','WF','WLF','876');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Yemen','YE','YEM','887');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Djibouti','DJ','DJI','262');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Zambia','ZM','ZMB','894');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Zimbabwe','ZW','ZWE','716');

--@FIN_BLOQUE
CREATE TABLE tbl_sat_estados
(
  codpais2 character(2) NOT NULL,
  codpais3 character(3) NOT NULL,
  codpaisnum smallint NOT NULL,
  codestado character varying(10) NOT NULL,
  nombre character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_sat_estados PRIMARY KEY (codpais2, codestado)
);

--@FIN_BLOQUE
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AGU','MX','MEX','484','Aguascalientes');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('BCN','MX','MEX','484','Baja California');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('BCS','MX','MEX','484','Baja California Sur');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CAM','MX','MEX','484','Campeche');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CHP','MX','MEX','484','Chiapas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CHH','MX','MEX','484','Chihuahua');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('COA','MX','MEX','484','Coahuila');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('COL','MX','MEX','484','Colima');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('DIF','MX','MEX','484','Distrito Federal');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('DUR','MX','MEX','484','Durango');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('GUA','MX','MEX','484','Guanajuato');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('GRO','MX','MEX','484','Guerrero');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('HID','MX','MEX','484','Hidalgo');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('JAL','MX','MEX','484','Jalisco');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MEX','MX','MEX','484','Estado de México');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MIC','MX','MEX','484','Michoacán');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MOR','MX','MEX','484','Morelos');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NAY','MX','MEX','484','Nayarit');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NLE','MX','MEX','484','Nuevo León');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('OAX','MX','MEX','484','Oaxaca');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('PUE','MX','MEX','484','Puebla');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('QTO','MX','MEX','484','Querétaro');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ROO','MX','MEX','484','Quintana Roo');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SLP','MX','MEX','484','San Luis Potosí');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SIN','MX','MEX','484','Sinaloa');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SON','MX','MEX','484','Sonora');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TAB','MX','MEX','484','Tabasco');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TAM','MX','MEX','484','Tamaulipas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TLA','MX','MEX','484','Tlaxcala');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('VER','MX','MEX','484','Veracruz');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('UC','MX','MEX','484','Yucatán');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ZAC','MX','MEX','484','Zacatecas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AL','US','USA','840','Alabama');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AK','US','USA','840','Alaska');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AZ','US','USA','840','Arizona');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AR','US','USA','840','Arkansas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CA','US','USA','840','California');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NC','US','USA','840','Carolina del Norte');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SC','US','USA','840','Carolina del Sur');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CO','US','USA','840','Colorado');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CT','US','USA','840','Connecticut');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ND','US','USA','840','Dakota del Norte');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SD','US','USA','840','Dakota del Sur');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('DE','US','USA','840','Delaware');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('FL','US','USA','840','Florida');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('GA','US','USA','840','Georgia');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('HI','US','USA','840','Hawái');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ID','US','USA','840','Idaho');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('IL','US','USA','840','Illinois');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('IN','US','USA','840','Indiana');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('IA','US','USA','840','Iowa');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('KS','US','USA','840','Kansas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('KY','US','USA','840','Kentucky');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('LA','US','USA','840','Luisiana');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ME','US','USA','840','Maine');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MD','US','USA','840','Maryland');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MA','US','USA','840','Massachusetts');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MI','US','USA','840','Míchigan');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MN','US','USA','840','Minnesota');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MS','US','USA','840','Misisipi');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MO','US','USA','840','Misuri');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MT','US','USA','840','Montana');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NE','US','USA','840','Nebraska');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NV','US','USA','840','Nevada');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NJ','US','USA','840','Nueva Jersey');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NY','US','USA','840','Nueva York');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NH','US','USA','840','Nuevo Hampshire');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NM','US','USA','840','Nuevo México');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('OH','US','USA','840','Ohio');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('OK','US','USA','840','Oklahoma');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('OR','US','USA','840','Oregón');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('PA','US','USA','840','Pensilvania');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('RI','US','USA','840','Rhode Island');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TN','US','USA','840','Tennessee');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TX','US','USA','840','Texas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('UT','US','USA','840','Utah');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('VT','US','USA','840','Vermont');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('VA','US','USA','840','Virginia');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('WV','US','USA','840','Virginia Occidental');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('WA','US','USA','840','Washington');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('WI','US','USA','840','Wisconsin');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('WY','US','USA','840','Wyoming');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ON','CA','CAN','124','Ontario ');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('QC','CA','CAN','124','Quebec ');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NS','CA','CAN','124','Nueva Escocia');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NB','CA','CAN','124','Nuevo Brunswick ');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MB','CA','CAN','124','Manitoba');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('BC','CA','CAN','124','Columbia Británica');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('PE','CA','CAN','124','Isla del Príncipe Eduardo');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SK','CA','CAN','124','Saskatchewan');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AB','CA','CAN','124','Alberta');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NL','CA','CAN','124','Terranova y Labrador');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NT','CA','CAN','124','Territorios del Noroeste');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('YT','CA','CAN','124','Yukón');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('UN','CA','CAN','124','Nunavut');

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ventas_facturas_impcab AS 
 SELECT rc.id_vc AS id_factura, rc.id_entidad, rc.numero, rc.fecha, rc.referencia, rc.status, rc.moneda AS id_moneda, m.moneda, rc.tc, rc.id_clipro AS id_cliente, COALESCE(cl.id_numero, 0) AS num_cliente, COALESCE(cl.nombre, 'Cliente de Mostrador'::character varying) AS cliente, ''::character varying AS pol, rc.id_bodega, b.nombre, rc.condicion, rc.obs, rc.mimporte AS importe, rc.mdescuento AS descuento, rc.msubtotal AS subtotal, rc.miva AS iva, rc.ieps, rc.ivaret, rc.isrret, rc.mtotal AS total, COALESCE(cl.rfc, 'XAXX010101000'::character varying) AS rfc, COALESCE(cl.direccion, ''::character varying(80)) AS direccion, COALESCE(cl.direccion, ''::character varying) AS calle, COALESCE(cl.noext, ''::character varying) AS noext, COALESCE(cl.noint, ''::character varying) AS noint, COALESCE(cl.colonia, ''::character varying) AS colonia, COALESCE(cl.poblacion, ''::character varying(80)) AS poblacion, COALESCE(cl.poblacion, ''::character varying) AS localidad, COALESCE(cl.municipio, ''::character varying) AS municipio, 
        CASE
            WHEN cl.estado IS NULL THEN ''::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)) IS NULL THEN cl.estado
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)
            END
        END AS estado, 
        CASE
            WHEN cl.pais IS NULL THEN 'México'::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)) IS NULL THEN cl.pais
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)
            END
        END AS pais, COALESCE(cl.cp, ''::character varying) AS cp, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.serie
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_serie, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.folio
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE 0
        END AS cfd_folio, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.noaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE 0
        END AS cfd_noaprobacion, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN (( SELECT tbl_cfd.anoaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd))::integer
            ELSE 0
        END AS cfd_anoaprobacion, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.nocertificado
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_nocertificado, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN (( SELECT tbl_cfd.fecha
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd))::timestamp with time zone
            ELSE now()
        END AS cfd_fecha, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.cadenaoriginal
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_cadenaoriginal, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.sello
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_sello, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.lugarexpedicion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_lugarexpedicion, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.metododepago
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_metododepago, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_nombre
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_nombre, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_estado
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_pais
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_cp, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.uuid
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::bpchar
        END AS tfd_uuid, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN (( SELECT tbl_tfd.fechatimbre
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd))::timestamp with time zone
            ELSE now()
        END AS tfd_fechatimbre, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.cadenaoriginal
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS tfd_cadenaoriginal, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.nocertificadosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS tfd_nocertificadosat, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.sellosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS tfd_sellosat
   FROM tbl_ventas_facturas_cab rc
   LEFT JOIN tbl_client_client cl ON cl.id_tipo = 'CL'::bpchar AND rc.id_clipro = cl.id_clave
   JOIN tbl_ventas_entidades ve ON rc.id_entidad = ve.id_entidadventa
   JOIN tbl_invserv_bodegas b ON rc.id_bodega = b.id_bodega
   JOIN tbl_cont_monedas m ON rc.moneda = m.clave
   JOIN tbl_vendedores vn ON rc.id_vendedor = vn.id_vendedor;

ALTER TABLE view_ventas_facturas_impcab
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ventas_cotizaciones_impcab AS 
 SELECT rc.id_vc AS id_cotizacion, rc.id_entidad, rc.numero, rc.fecha, rc.referencia, rc.status, rc.moneda AS id_moneda, m.moneda, rc.tc, rc.id_clipro AS id_cliente, COALESCE(cl.id_numero, 0) AS num_cliente, COALESCE(cl.nombre, 'Cliente de Mostrador'::character varying) AS cliente, ''::character varying AS pol, rc.id_bodega, b.nombre, rc.condicion, rc.obs, rc.mimporte AS importe, rc.mdescuento AS descuento, rc.msubtotal AS subtotal, rc.miva AS iva, rc.ieps, rc.ivaret, rc.isrret, rc.mtotal AS total, COALESCE(cl.rfc, 'XAXX010101000'::character varying) AS rfc, COALESCE(cl.direccion, ''::character varying(80)) AS direccion, COALESCE(cl.direccion, ''::character varying) AS calle, COALESCE(cl.noext, ''::character varying) AS noext, COALESCE(cl.noint, ''::character varying) AS noint, COALESCE(cl.colonia, ''::character varying) AS colonia, COALESCE(cl.poblacion, ''::character varying(80)) AS poblacion, COALESCE(cl.poblacion, ''::character varying) AS localidad, COALESCE(cl.municipio, ''::character varying) AS municipio, 
        CASE
            WHEN cl.estado IS NULL THEN ''::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)) IS NULL THEN cl.estado
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)
            END
        END AS estado, 
        CASE
            WHEN cl.pais IS NULL THEN 'México'::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)) IS NULL THEN cl.pais
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)
            END
        END AS pais, COALESCE(cl.cp, ''::character varying) AS cp
   FROM tbl_ventas_cotizaciones_cab rc
   LEFT JOIN tbl_client_client cl ON cl.id_tipo = 'CL'::bpchar AND rc.id_clipro = cl.id_clave
   JOIN tbl_ventas_entidades ve ON rc.id_entidad = ve.id_entidadventa
   JOIN tbl_invserv_bodegas b ON rc.id_bodega = b.id_bodega
   JOIN tbl_cont_monedas m ON rc.moneda = m.clave
   JOIN tbl_vendedores vn ON rc.id_vendedor = vn.id_vendedor;

ALTER TABLE view_ventas_cotizaciones_impcab
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ventas_devoluciones_impcab AS 
 SELECT rc.id_vc AS id_devolucion, rc.id_entidad, rc.numero, rc.fecha, rc.referencia, rc.status, rc.moneda AS id_moneda, m.moneda, rc.tc, rc.id_clipro AS id_cliente, COALESCE(cl.id_numero, 0) AS num_cliente, COALESCE(cl.nombre, 'Cliente de Mostrador'::character varying) AS cliente, ''::character varying AS pol, rc.id_bodega, b.nombre, rc.condicion, rc.obs, rc.mimporte AS importe, rc.mdescuento AS descuento, rc.msubtotal AS subtotal, rc.miva AS iva, rc.ieps, rc.ivaret, rc.isrret, rc.mtotal AS total, COALESCE(cl.rfc, 'XAXX010101000'::character varying) AS rfc, COALESCE(cl.direccion, ''::character varying(80)) AS direccion, COALESCE(cl.direccion, ''::character varying) AS calle, COALESCE(cl.noext, ''::character varying) AS noext, COALESCE(cl.noint, ''::character varying) AS noint, COALESCE(cl.colonia, ''::character varying) AS colonia, COALESCE(cl.poblacion, ''::character varying(80)) AS poblacion, COALESCE(cl.poblacion, ''::character varying) AS localidad, COALESCE(cl.municipio, ''::character varying) AS municipio, 
        CASE
            WHEN cl.estado IS NULL THEN ''::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)) IS NULL THEN cl.estado
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)
            END
        END AS estado, 
        CASE
            WHEN cl.pais IS NULL THEN 'México'::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)) IS NULL THEN cl.pais
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)
            END
        END AS pais, COALESCE(cl.cp, ''::character varying) AS cp, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.serie
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_serie, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.folio
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE 0
        END AS cfd_folio, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.noaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE 0
        END AS cfd_noaprobacion, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN (( SELECT tbl_cfd.anoaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd))::integer
            ELSE 0
        END AS cfd_anoaprobacion, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.nocertificado
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_nocertificado, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN (( SELECT tbl_cfd.fecha
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd))::timestamp with time zone
            ELSE now()
        END AS cfd_fecha, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.cadenaoriginal
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_cadenaoriginal, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.sello
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_sello, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.lugarexpedicion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_lugarexpedicion, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.metododepago
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_metododepago, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_nombre
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_nombre, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_estado
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_pais
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_cp, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.uuid
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::bpchar
        END AS tfd_uuid, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN (( SELECT tbl_tfd.fechatimbre
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd))::timestamp with time zone
            ELSE now()
        END AS tfd_fechatimbre, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.cadenaoriginal
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS tfd_cadenaoriginal, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.nocertificadosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS tfd_nocertificadosat, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.sellosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS tfd_sellosat
   FROM tbl_ventas_devoluciones_cab rc
   LEFT JOIN tbl_client_client cl ON cl.id_tipo = 'CL'::bpchar AND rc.id_clipro = cl.id_clave
   JOIN tbl_ventas_entidades ve ON rc.id_entidad = ve.id_entidadventa
   JOIN tbl_invserv_bodegas b ON rc.id_bodega = b.id_bodega
   JOIN tbl_cont_monedas m ON rc.moneda = m.clave
   JOIN tbl_vendedores vn ON rc.id_vendedor = vn.id_vendedor;

ALTER TABLE view_ventas_devoluciones_impcab
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ventas_pedidos_impcab AS 
 SELECT rc.id_vc AS id_pedido, rc.id_entidad, rc.numero, rc.fecha, rc.referencia, rc.status, rc.moneda AS id_moneda, m.moneda, rc.tc, rc.id_clipro AS id_cliente, COALESCE(cl.id_numero, 0) AS num_cliente, COALESCE(cl.nombre, 'Cliente de Mostrador'::character varying) AS cliente, ''::character varying AS pol, rc.id_bodega, b.nombre, rc.condicion, rc.obs, rc.mimporte AS importe, rc.mdescuento AS descuento, rc.msubtotal AS subtotal, rc.miva AS iva, rc.ieps, rc.ivaret, rc.isrret, rc.mtotal AS total, COALESCE(cl.rfc, 'XAXX010101000'::character varying) AS rfc, COALESCE(cl.direccion, ''::character varying(80)) AS direccion, COALESCE(cl.direccion, ''::character varying) AS calle, COALESCE(cl.noext, ''::character varying) AS noext, COALESCE(cl.noint, ''::character varying) AS noint, COALESCE(cl.colonia, ''::character varying) AS colonia, COALESCE(cl.poblacion, ''::character varying(80)) AS poblacion, COALESCE(cl.poblacion, ''::character varying) AS localidad, COALESCE(cl.municipio, ''::character varying) AS municipio, 
        CASE
            WHEN cl.estado IS NULL THEN ''::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)) IS NULL THEN cl.estado
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)
            END
        END AS estado, 
        CASE
            WHEN cl.pais IS NULL THEN 'México'::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)) IS NULL THEN cl.pais
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)
            END
        END AS pais, COALESCE(cl.cp, ''::character varying) AS cp
   FROM tbl_ventas_pedidos_cab rc
   LEFT JOIN tbl_client_client cl ON cl.id_tipo = 'CL'::bpchar AND rc.id_clipro = cl.id_clave
   JOIN tbl_ventas_entidades ve ON rc.id_entidad = ve.id_entidadventa
   JOIN tbl_invserv_bodegas b ON rc.id_bodega = b.id_bodega
   JOIN tbl_cont_monedas m ON rc.moneda = m.clave
   JOIN tbl_vendedores vn ON rc.id_vendedor = vn.id_vendedor;

ALTER TABLE view_ventas_pedidos_impcab
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ventas_remisiones_impcab AS 
 SELECT rc.id_vc AS id_remision, rc.id_entidad, rc.numero, rc.fecha, rc.referencia, rc.status, rc.moneda AS id_moneda, m.moneda, rc.tc, rc.id_clipro AS id_cliente, COALESCE(cl.id_numero, 0) AS num_cliente, COALESCE(cl.nombre, 'Cliente de Mostrador'::character varying) AS cliente, ''::character varying AS pol, rc.id_bodega, b.nombre, rc.condicion, rc.obs, rc.mimporte AS importe, rc.mdescuento AS descuento, rc.msubtotal AS subtotal, rc.miva AS iva, rc.ieps, rc.ivaret, rc.isrret, rc.mtotal AS total, COALESCE(cl.rfc, 'XAXX010101000'::character varying) AS rfc, COALESCE(cl.direccion, ''::character varying(80)) AS direccion, COALESCE(cl.direccion, ''::character varying) AS calle, COALESCE(cl.noext, ''::character varying) AS noext, COALESCE(cl.noint, ''::character varying) AS noint, COALESCE(cl.colonia, ''::character varying) AS colonia, COALESCE(cl.poblacion, ''::character varying(80)) AS poblacion, COALESCE(cl.poblacion, ''::character varying) AS localidad, COALESCE(cl.municipio, ''::character varying) AS municipio, 
        CASE
            WHEN cl.estado IS NULL THEN ''::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)) IS NULL THEN cl.estado
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = cl.estado::text AND tbl_sat_estados.codpais3 = cl.pais::bpchar)
            END
        END AS estado, 
        CASE
            WHEN cl.pais IS NULL THEN 'México'::character varying
            ELSE 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)) IS NULL THEN cl.pais
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = cl.pais::bpchar)
            END
        END AS pais, COALESCE(cl.cp, ''::character varying) AS cp, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.serie
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_serie, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.folio
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE 0
        END AS cfd_folio, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.noaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE 0
        END AS cfd_noaprobacion, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN (( SELECT tbl_cfd.anoaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd))::integer
            ELSE 0
        END AS cfd_anoaprobacion, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.nocertificado
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_nocertificado, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN (( SELECT tbl_cfd.fecha
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd))::timestamp with time zone
            ELSE now()
        END AS cfd_fecha, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.cadenaoriginal
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_cadenaoriginal, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.sello
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_sello, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.lugarexpedicion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_lugarexpedicion, 
        CASE
            WHEN rc.id_cfd IS NOT NULL AND rc.id_cfd <> 0 THEN ( SELECT tbl_cfd.metododepago
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS cfd_metododepago, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_nombre
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_nombre, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_estado
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_pais
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_cp, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.uuid
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::bpchar
        END AS tfd_uuid, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN (( SELECT tbl_tfd.fechatimbre
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd))::timestamp with time zone
            ELSE now()
        END AS tfd_fechatimbre, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.cadenaoriginal
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS tfd_cadenaoriginal, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.nocertificadosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS tfd_nocertificadosat, 
        CASE
            WHEN rc.tfd IS NOT NULL AND rc.tfd > 1 THEN ( SELECT tbl_tfd.sellosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = rc.id_cfd)
            ELSE ''::character varying
        END AS tfd_sellosat
   FROM tbl_ventas_remisiones_cab rc
   LEFT JOIN tbl_client_client cl ON cl.id_tipo = 'CL'::bpchar AND rc.id_clipro = cl.id_clave
   JOIN tbl_ventas_entidades ve ON rc.id_entidad = ve.id_entidadventa
   JOIN tbl_invserv_bodegas b ON rc.id_bodega = b.id_bodega
   JOIN tbl_cont_monedas m ON rc.moneda = m.clave
   JOIN tbl_vendedores vn ON rc.id_vendedor = vn.id_vendedor;

ALTER TABLE view_ventas_remisiones_impcab
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_invserv_traspasos_impcab AS 
 SELECT m.id_movimiento, m.salida, m.status, m.fecha, m.fechaentrega AS entrega, m.id_bodega, ( SELECT tbl_invserv_bodegas.nombre
           FROM tbl_invserv_bodegas
          WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega) AS bodega, m.id_bodegadest, c.nombre AS bodegadest, m.concepto, m.referencia, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_nombre
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
            ELSE ''::character varying
        END AS nombre, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
            ELSE ''::character varying
        END AS calle, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
            ELSE ''::character varying
        END AS noext, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
            ELSE ''::character varying
        END AS noint, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
            ELSE ''::character varying
        END AS colonia, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
            ELSE ''::character varying
        END AS localidad, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
            ELSE ''::character varying
        END AS municipio, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest))))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest))))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_estado
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                           FROM tbl_invserv_bodegas
                          WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest))))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest))))::bpchar))
            END
            ELSE ''::character varying
        END AS estado, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest))))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_pais
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                           FROM tbl_invserv_bodegas
                          WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest))))::bpchar))
            END
            ELSE ''::character varying
        END AS pais, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodegadest)))
            ELSE ''::character varying
        END AS cp, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN ( SELECT tbl_cfd.serie
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd)
            ELSE ''::character varying
        END AS cfd_serie, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN ( SELECT tbl_cfd.folio
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd)
            ELSE 0
        END AS cfd_folio, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN ( SELECT tbl_cfd.noaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd)
            ELSE 0
        END AS cfd_noaprobacion, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN (( SELECT tbl_cfd.anoaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd))::integer
            ELSE 0
        END AS cfd_anoaprobacion, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN ( SELECT tbl_cfd.nocertificado
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd)
            ELSE ''::character varying
        END AS cfd_nocertificado, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN (( SELECT tbl_cfd.fecha
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd))::timestamp with time zone
            ELSE now()
        END AS cfd_fecha, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN ( SELECT tbl_cfd.cadenaoriginal
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd)
            ELSE ''::character varying
        END AS cfd_cadenaoriginal, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN ( SELECT tbl_cfd.sello
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd)
            ELSE ''::character varying
        END AS cfd_sello, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN ( SELECT tbl_cfd.lugarexpedicion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd)
            ELSE ''::character varying
        END AS cfd_lugarexpedicion, 
        CASE
            WHEN m.id_cfd IS NOT NULL AND m.id_cfd <> 0 THEN ( SELECT tbl_cfd.metododepago
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = m.id_cfd)
            ELSE ''::character varying
        END AS cfd_metododepago, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_nombre
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
            ELSE ''::character varying
        END AS cfd_nombre, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega))))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega))))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_estado
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                           FROM tbl_invserv_bodegas
                          WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega))))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega))))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega))))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_pais
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                           FROM tbl_invserv_bodegas
                          WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                                   FROM tbl_invserv_bodegas
                                  WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega))))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
               FROM tbl_invserv_bodegas
              WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)) <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = (( SELECT tbl_invserv_bodegas.cfd_id_expedicion
                       FROM tbl_invserv_bodegas
                      WHERE tbl_invserv_bodegas.id_bodega = m.id_bodega)))
            ELSE ''::character varying
        END AS cfd_cp, 
        CASE
            WHEN m.tfd IS NOT NULL AND m.tfd > 1 THEN ( SELECT tbl_tfd.uuid
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = m.id_cfd)
            ELSE ''::bpchar
        END AS tfd_uuid, 
        CASE
            WHEN m.tfd IS NOT NULL AND m.tfd > 1 THEN (( SELECT tbl_tfd.fechatimbre
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = m.id_cfd))::timestamp with time zone
            ELSE now()
        END AS tfd_fechatimbre, 
        CASE
            WHEN m.tfd IS NOT NULL AND m.tfd > 1 THEN ( SELECT tbl_tfd.cadenaoriginal
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = m.id_cfd)
            ELSE ''::character varying
        END AS tfd_cadenaoriginal, 
        CASE
            WHEN m.tfd IS NOT NULL AND m.tfd > 1 THEN ( SELECT tbl_tfd.nocertificadosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = m.id_cfd)
            ELSE ''::character varying
        END AS tfd_nocertificadosat, 
        CASE
            WHEN m.tfd IS NOT NULL AND m.tfd > 1 THEN ( SELECT tbl_tfd.sellosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = m.id_cfd)
            ELSE ''::character varying
        END AS tfd_sellosat
   FROM tbl_invserv_almacen_bod_mov_cab m
   JOIN tbl_invserv_bodegas c ON m.id_bodegadest = c.id_bodega;

ALTER TABLE view_invserv_traspasos_impcab
  OWNER TO [[owner]];
  
--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_nomina_recibos_impcab AS 
 SELECT ve.id_sucursal, ve.descripcion, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN ( SELECT tbl_cfd.serie
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd)
            ELSE ''::character varying
        END AS cfd_serie, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN ( SELECT tbl_cfd.folio
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd)
            ELSE 0
        END AS cfd_folio, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN ( SELECT tbl_cfd.noaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd)
            ELSE 0
        END AS cfd_noaprobacion, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN (( SELECT tbl_cfd.anoaprobacion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd))::integer
            ELSE 0
        END AS cfd_anoaprobacion, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN ( SELECT tbl_cfd.nocertificado
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd)
            ELSE ''::character varying
        END AS cfd_nocertificado, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN (( SELECT tbl_cfd.fecha
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd))::timestamp with time zone
            ELSE now()
        END AS cfd_fecha, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN ( SELECT tbl_cfd.cadenaoriginal
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd)
            ELSE ''::character varying
        END AS cfd_cadenaoriginal, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN ( SELECT tbl_cfd.sello
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd)
            ELSE ''::character varying
        END AS cfd_sello, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN ( SELECT tbl_cfd.lugarexpedicion
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd)
            ELSE ''::character varying
        END AS cfd_lugarexpedicion, 
        CASE
            WHEN esp.id_cfd IS NOT NULL AND esp.id_cfd <> 0 THEN ( SELECT tbl_cfd.metododepago
               FROM tbl_cfd
              WHERE tbl_cfd.id_cfd = esp.id_cfd)
            ELSE ''::character varying
        END AS cfd_metododepago, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_nombre
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_nombre, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_calle
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_calle, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noext
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noext, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_noint
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_noint, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_colonia
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_colonia, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_localidad
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_localidad, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_municipio
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_municipio, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_estado
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
                ELSE ( SELECT tbl_sat_estados.nombre
                   FROM tbl_sat_estados
                  WHERE tbl_sat_estados.codestado::text = ((( SELECT tbl_cfd_expediciones.cfd_estado
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::text) AND tbl_sat_estados.codpais3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_estado, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN 
            CASE
                WHEN (( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))) IS NULL THEN ( SELECT tbl_cfd_expediciones.cfd_pais
                   FROM tbl_cfd_expediciones
                  WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
                ELSE ( SELECT tbl_sat_paises.nombre
                   FROM tbl_sat_paises
                  WHERE tbl_sat_paises.alfa3 = ((( SELECT tbl_cfd_expediciones.cfd_pais
                           FROM tbl_cfd_expediciones
                          WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion))::bpchar))
            END
            ELSE ''::character varying
        END AS cfd_pais, 
        CASE
            WHEN ve.cfd_id_expedicion <> 0 THEN ( SELECT tbl_cfd_expediciones.cfd_cp
               FROM tbl_cfd_expediciones
              WHERE tbl_cfd_expediciones.cfd_id_expedicion = ve.cfd_id_expedicion)
            ELSE ''::character varying
        END AS cfd_cp, fc.id_nomina, fc.numero_nomina AS numero, fc.ano, fc.tipo, fc.fecha_desde, fc.fecha_hasta, fc.dias, fc.cerrado, fc.mes, fc.status, fc.formapago, fc.id_mov, fc.id_pol, 1 AS moneda, '$'::character varying AS monedasim, 1 AS tc, 0 AS condicion, esp.id_empleado, esp.gravado + esp.exento AS importe, esp.deduccion - esp.isr AS descuento, esp.gravado + esp.exento AS subtotal, esp.isr, esp.gravado + esp.exento + esp.deduccion AS total, (((emp.nombre::text || ' '::text) || emp.apellido_paterno::text) || ' '::text) || emp.apellido_materno::text AS nombre, ((emp.rfc_letras::text || emp.rfc_fecha::text) || emp.rfc_homoclave::text)::character varying AS rfc, emp.calle, emp.numero AS noext, emp.noint, emp.colonia, emp.localidad, emp.delegacion AS municipio, emp.estado, emp.pais, emp.codigo_postal AS cp, 
        CASE
            WHEN emp.cuenta_bancaria::text = ''::text THEN 'efectivo'::text
            ELSE 'transferencia bancaria'::text
        END AS metododepago, emp.curp, emp.regimen AS tiporegimen, emp.num_registro_imss AS numseguridadsocial, esp.dias - esp.faltas AS numdiaspagados, dep.nombre AS departamento, emp.cuenta_bancaria AS clabe, emp.id_satbanco AS banco, emp.fecha_de_ingreso AS fechainiciorellaboral, emp.puesto, 
        CASE
            WHEN ve.periodo = 'sem'::bpchar THEN 'semanal'::text
            WHEN ve.periodo = 'qui'::bpchar THEN 'quincenal'::text
            ELSE 'mensual'::text
        END AS periodicidadpago, esp.gravado AS totalgravado, esp.exento AS totalexento, esp.deduccion AS totaldeducciones, esp.deduccion AS totaldedgravadas, 0.0 AS totaldedexentas, esp.he AS horasextras, esp.ht AS horastriples, esp.hd AS horasdomingo, esp.ixa, esp.ixe, esp.ixm, esp.diashorasextras, 
        CASE
            WHEN esp.tfd IS NOT NULL AND esp.tfd > 1 THEN ( SELECT tbl_tfd.uuid
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = esp.id_cfd
              ORDER BY tbl_tfd.fechatimbre DESC
             LIMIT 1)
            ELSE ''::bpchar
        END AS tfd_uuid, 
        CASE
            WHEN esp.tfd IS NOT NULL AND esp.tfd > 1 THEN (( SELECT tbl_tfd.fechatimbre
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = esp.id_cfd
              ORDER BY tbl_tfd.fechatimbre DESC
             LIMIT 1))::timestamp with time zone
            ELSE now()
        END AS tfd_fechatimbre, 
        CASE
            WHEN esp.tfd IS NOT NULL AND esp.tfd > 1 THEN ( SELECT tbl_tfd.cadenaoriginal
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = esp.id_cfd
              ORDER BY tbl_tfd.fechatimbre DESC
             LIMIT 1)
            ELSE ''::character varying
        END AS tfd_cadenaoriginal, 
        CASE
            WHEN esp.tfd IS NOT NULL AND esp.tfd > 1 THEN ( SELECT tbl_tfd.nocertificadosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = esp.id_cfd
              ORDER BY tbl_tfd.fechatimbre DESC
             LIMIT 1)
            ELSE ''::character varying
        END AS tfd_nocertificadosat, 
        CASE
            WHEN esp.tfd IS NOT NULL AND esp.tfd > 1 THEN ( SELECT tbl_tfd.sellosat
               FROM tbl_tfd
              WHERE tbl_tfd.id_cfd = esp.id_cfd
              ORDER BY tbl_tfd.fechatimbre DESC
             LIMIT 1)
            ELSE ''::character varying
        END AS tfd_sellosat
   FROM tbl_nom_calculo_nomina fc
   JOIN tbl_companias ve ON fc.id_compania = ve.id_compania AND fc.id_sucursal = ve.id_sucursal
   JOIN tbl_nom_calculo_nomina_esp esp ON fc.id_nomina = esp.id_nomina
   JOIN tbl_nom_masemp emp ON esp.id_empleado = emp.id_empleado
   JOIN tbl_nom_departamentos dep ON emp.id_departamento = dep.id_departamento;

ALTER TABLE view_nomina_recibos_impcab
  OWNER TO [[owner]];

--@FIN_BLOQUE

--Actualizacion de usuarios vs permisos nuevos
insert into tbl_usuarios_permisos
select u.id_usuario, upc.id_permiso, '0'
from tbl_usuarios_permisos_catalogo upc, tbl_usuarios u 
where upc.id_permiso not in ( select tp.id_permiso from tbl_usuarios_permisos tp where tp.id_usuario = u.id_usuario )
order by u.id_usuario, upc.id_permiso;

--@FIN_BLOQUE


/*
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

-------------------------- PREVIO A ESTA ACTUALIZACION --------------------------------------------------------

*.- Establecer boton de carga de asistencias ya que es idéntico al de agregar asistencia. Ademas, verificar que tenga permiso exclusivo este proceso, independiente al de agregar asistencia
*.- Checar el calculo de Iva y Ieps pendiente en devolucion de anticipos, liquidaciones, etc, cuando la compra o factura involucra retenciones... Probablemente no haga bien el cálculo... Hay que hacer pruebas
*.- Checar cancelación de recepciones, remisiones, pedidos, etc., cuando fueron generados a través de un documento anterior (por ejemplo, una recepción generada por una orden de compra), al cancelar la recepción no se cancela la orden por lo menos cuando la orden es de un mes cerrado. Hay que hacer pruebas en todos estos ámbitos.  Ya que haya ordenes, remisiones y cotizaciones, checar sus reportes. (No se checaron al momento del desarrollo portque no existian registros, solo se checó su sintaxis las cuales pasaron las pruebas)
*.- Rechazar alta o cambios en empleados de nómina cuando la fecha de ingreso sea 29 de febrero (año biciesto) porque falla en calculo de vacaciones
*.- Reparar modulo de plantillas, eliminar panel inferior de tiempo porque sale sobrando, y reparar panel izquierdo. Nomina y Tipo de este panel están 
intercambiados, ademas Nómina debería mostrar la Entidad ya que a esto se refiere. Actualizar ayuda tambien
*.- Agregar desarrollo de proceso del PTU al módulo de empleados.
*.- Agregar esta ayuda de ptu en empleados y actualizar ayuda de reportes y gráficas
*.- Actualizar función _p(String str)... Al agregar una cadena con & en la base de datos tiene que registrarse como &amp; porque de lo contrario falla en al render del pdf. Verificar si al hacer el sellado el & si lo manda como &amp; porque de ser asi, pudiera corregirse lo del pdf y amolarse al sellar o timbrar.
*.- Actualizar ayuda en clientes y facturas, cotizacines, remisiones y pedidos. El Metodo de pago conforme al catálogo del sat 
*.- Actualizar ayuda de CFDI y CE para Domicilios Fiscales y Expedidos En, conforme a base de datos de paises, estados, etc.
*.- Actualizar ayuda de Clientes y Proveedores (Por cambios en metodos de pago y complemento de comercio exterior)
*.- Eliminar posibilidad de varias lineas en campos de observación de compras, ventas, etc.. Al consultar el registro, cuando en sus observaciones tiene mas de una linea, Falla la carga. 

-------------------------- AL GENERAR ESTA ACTUALIZACION PARA DISTRIBUCION -----------------------------------------------------

ACT.- Agregar a la actualización el archivo cuentas_cont.csv sobre el directorio rec, archivo indice_reportes.html y estructura_reportes.html sobre bin/forseti_doc.
ACT.- ELIMINAR JMsj y HelloWorld antes de preparar la actualización
ACT.- Descomentar en JAdmBDDlg GENERAR_REPORTE / DOCUMENTACION y PROCESO donde indica que si es reporte de sistema no se puede cambiar if(repid < 10000) { .... 
ACT.- En AWS Ryrsa, copiar sp_cajas_cierrez antes de actualizar para despues poder reconstruirlo con OPT
ACT.- En .forseti_init reconstuir "insert into"s a TBL_CATALOGOS para que los catalogos queden configurados como se hicieron en las pruebas, es necesario agregarles la columna replong a cada insert into. 

-------------------------- EN PROXIMAS ACTUALIZACIONES ---------------------------

1.- Generar Web Services REST para JPacConn, JSmtpConn y JAwsS3Conn
2.- Leer QRCode desde archivo de imagen al cargarlo en compras y gastos para automatizar datos de CBB
3.- Corregir cabecero de movimientos de caja y bancos respecto a CE. En la poliza esta correcto pero el cabecero de mov presenta valores raros e inconsistentes
4.- Definir las unidades del sat a unidades de productos forseti y enlazarlas en codigo de inicio... Que queden por default al agregar una nueva empresa (Verificar proceso de agregado no virgen para que no se vayan a duplicar y marque un error).
5.- Checar diferencias de tablas, funciones, etc entre TAJA y TAJAB para verificar si están consistentes los desarrollos

-------------------------- OTRAS REVISIONES ----------------------------------------

1.- CHECAR CON VEDIA EL CALCULO DE Descuentos por inconsistencias en sp_calculo_nómina_faltas..... Aqui en forseti no aplicaba DIAS SIN PAGO DE TIEMPO. y se modificó.

*/

