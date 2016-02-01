 
CREATE OR REPLACE FUNCTION sp_nom_fonacot_agregar(
    _id_credito character varying,
    _id_empleado character,
    _fecha timestamp without time zone,
    _meses smallint,
    _plazo smallint,
    _importe numeric,
    _retencion numeric)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'El crédito fonacot se registró satisfactoriamente';
	
	-- si existe un empleado marcará error
	IF(select count(*) from TBL_NOM_FONACOT_CAB where ID_Credito = _ID_Credito) > 0
	THEN
		_err := 3;
		_result := 'ERROR: Ya existe un crédito con la misma clave';
	END IF;
	
	IF _err = 0
	THEN
		INSERT INTO TBL_NOM_FONACOT_CAB
		VALUES( _ID_Credito, _ID_Empleado, _Fecha, _Meses, _Plazo, _Importe, _Retencion );
		
		INSERT INTO TBL_NOM_FONACOT_DET
		SELECT _ID_Credito, Fecha, Descuento
		FROM _TMP_FONACOT_DET;

	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_credito;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_fonacot_agregar(character varying, character, timestamp without time zone, smallint, smallint, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_fonacot_cambiar(
    _id_credito character varying,
    _id_empleado character,
    _fecha timestamp without time zone,
    _meses smallint,
    _plazo smallint,
    _importe numeric,
    _retencion numeric)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'El crédito fonacot se cambió satisfactoriamente';
	
	-- si existe un empleado marcará error
	IF(select count(*) from TBL_NOM_FONACOT_CAB where ID_Credito = _ID_Credito) < 1
	THEN
		_err := 3;
		_result := 'ERROR: NO existe un crédito con esta clave';
	END IF;
	
	IF _err = 0
	THEN
		UPDATE TBL_NOM_FONACOT_CAB
		SET Fecha = _Fecha, Meses = _Meses, Plazo = _Plazo, Importe = _Importe, Retencion = _Retencion
		WHERE ID_Credito = _ID_Credito;
		
		DELETE FROM TBL_NOM_FONACOT_DET
		WHERE ID_Credito = _ID_Credito;

		INSERT INTO TBL_NOM_FONACOT_DET
		SELECT _ID_Credito, Fecha, Descuento
		FROM _TMP_FONACOT_DET;
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_credito;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_fonacot_cambiar(character varying, character, timestamp without time zone, smallint, smallint, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_fonacot_eliminar(_id_credito character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'El crédito fonacot se eliminó de la base de datos';
	
	-- si existe un empleado marcará error
	IF(select count(*) from TBL_NOM_FONACOT_CAB where ID_Credito = _ID_Credito) < 1
	THEN
		_err := 3;
		_result := 'ERROR: NO existe un crédito con esta clave';
	END IF;
	
	IF _err = 0
	THEN
		DELETE FROM TBL_NOM_FONACOT_CAB
		WHERE ID_Credito = _ID_Credito;
		
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_credito;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_fonacot_eliminar(character varying)
  OWNER TO [[owner]];

--@FIN_BLOQUE
ALTER TABLE tbl_compras_gastos_cfd
DROP CONSTRAINT fk_tbl_compras_gastos_cfd_tbl_cfdcomp;

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
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_DCAF char(19);
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
	_CC_IVARet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_IVARETC');
	_CC_ISRRet := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_ISRRETC');
	_CC_Desc := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSC');
	IF _Condicion = 0
	THEN
		_CC_DEVREB := (select valfanumerico from TBL_VARIABLES where ID_Variable = 'CC_DSCCONT');
		_ConceptoDescuento := 'Devolucion y/o rebaja sobre compra de contado';
	ELSE	
		_CC_DEVREB := (select cc from TBL_PROVEE_CXP_CONCEPTOS where ID_Concepto = _fsipg_id_concepto);
		_ConceptoDescuento := _fsipg_desc_concepto;
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

	IF _IEPS > 0.0 AND ( _CC_IEPS = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS no existe o no se ha enlazado';
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
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, 0.0, _IEPSPesos);	
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
CREATE OR REPLACE FUNCTION sp_nom_categorias_cambiar(
    _id_categoria smallint,
    _descripcion character varying,
    _sueldo numeric,
    _sueldoam numeric,
    _integradoam numeric,
    _vales numeric,
    _valesam numeric)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'La categoría se cambió satisfactoriamente, y todos los empleados con este sueldo se actualizaron';
	
	-- si existe marcar? error
	IF(select count(*) from TBL_NOM_CATEGORIAS where ID_Categoria = _ID_Categoria) < 1
	THEN
		_err := 3;
		_result := 'ERROR: No existe una categoría con la clave ';
	END IF;
	
	IF _err = 0
	THEN
		UPDATE TBL_NOM_CATEGORIAS
		SET Descripcion = _Descripcion, Sueldo = _Sueldo, SueldoAM = _SueldoAM, IntegradoAM = _IntegradoAM, Vales = _Vales, ValesAM = _ValesAM
		WHERE ID_Categoria = _ID_Categoria;
		
		-- ahora calcula y cambia los salarios del catalogo de empleados
		UPDATE TBL_NOM_MASEMP
		SET Salario_Diario = ( CASE WHEN Salario_Diario = 0 then _Sueldo else round( ((Salario_Diario * _SueldoAM) + Salario_Diario), 2) end ),
			Importe_Vales_de_Despensa = ( CASE WHEN Importe_Vales_de_Despensa = 0 then _Vales else round(  ((Importe_Vales_de_Despensa * _ValesAM) + Importe_Vales_de_Despensa), 2) end ),
			Salario_Integrado = round( ((Salario_Integrado * _IntegradoAM) + Salario_Integrado),2),
			Salario_Mixto = ( CASE WHEN Salario_Mixto = 0 then _Sueldo else round( ((Salario_Mixto * _SueldoAM) + Salario_Mixto), 2) end )
		WHERE ID_Categoria = _ID_Categoria and Status = 0;

		UPDATE TBL_NOM_MASEMP
		SET Salario_Nominal = round(Salario_Diario * Jornada,4), 
				Salario_por_Hora = round(Salario_Diario / 8,4)
		WHERE ID_Categoria = _ID_Categoria and Status = 0;

	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_categoria;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_categorias_cambiar(smallint, character varying, numeric, numeric, numeric, numeric, numeric)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_asistencias_server_agregar(_id_empleado character)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(4000); _fechatext varchar(15); _ID_FechaHora timestamp; _UltimaFechaHora timestamp; 
					_Ahorita timestamp; _ID_FechaMovimiento timestamp; _Indicador smallint; _Diferencias smallint;
BEGIN
	_Ahorita := NOW();
	-- Calcula la fecha del movimiento. Elimina la hora.
	_ID_FechaMovimiento := _Ahorita::date;
	-- Calcula la fecha con horas y minutos..... Elimina sgundos y decimas
	_ID_FechaHora := _ID_FechaMovimiento + (cast(date_part('hour',_Ahorita) as varchar) || ' hour')::interval;
	_ID_FechaHora := _ID_FechaHora + (cast(date_part('minute',_Ahorita) as varchar) || ' minute')::interval;
		
	_err := 0;
	_result := 'SE CAPTURÓ LA ENTRADA ';
	_fechatext := date_part('day', _ID_FechaHora)::varchar || '/' || date_part('month', _ID_FechaHora)::varchar || '/' || date_part('year', _ID_FechaHora)::varchar || ' ' || date_part('hour', _ID_FechaHora)::varchar || ':' || date_part('minute', _ID_FechaHora)::varchar; 

	_Diferencias := ( 	select VEntero from TBL_VARIABLES 
												where ID_Variable = 'DIFASIST' );
	-- si no existe un empleado marcar? error
	IF(select count(*) from TBL_NOM_MASEMP where ID_Empleado = _ID_Empleado) < 1
	THEN
		_err := 3;
		_result := 'ERROR: NO EXISTEN EMPLEADOS CON LA CLAVE '  || _ID_Empleado;
	END IF;
	
	IF (select count(*) from TBL_NOM_ASISTENCIAS_CHEQUEOS where ID_Empleado = _ID_Empleado and ID_FechaHora = _ID_FechaHora) > 0
	THEN	
		_err := 3;
		_result := 'ERROR: NO TE PUEDES VOLVER A REGISTRAR';
	END IF;

	IF _Diferencias is null 
	THEN
		_Diferencias := 5; -- Si no se ha establecido la variable de diferencias en el tiempo por error de pasar la tarjeta dos veces, la establece a 5 minutos
	END IF;
	
	IF _err = 0
	THEN
		--Inserta en los chequeos
		INSERT INTO TBL_NOM_ASISTENCIAS_CHEQUEOS
		VALUES( _ID_Empleado,	_ID_FechaHora);

		IF (select count(*) from TBL_NOM_ASISTENCIAS where ID_Empleado = _ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento) < 1
		THEN
			INSERT INTO TBL_NOM_ASISTENCIAS
			VALUES( _ID_Empleado, _ID_FechaMovimiento, _ID_FechaHora, null, 1, null, null );

		ELSIF (select Indicador	from TBL_NOM_ASISTENCIAS	where ID_Empleado = _ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento) = 4
		THEN 
			_err := 3;
			_result := 'ERROR: ESTE EMPLEADO HA ENTRADO Y SALIDO MUCHAS VECES '  || _ID_Empleado;
		ELSE
			_Indicador := (	select Indicador 
							from TBL_NOM_ASISTENCIAS 
							where ID_Empleado = _ID_Empleado and 
											ID_FechaMovimiento = _ID_FechaMovimiento) + 1; 
			_result := case 	when (_Indicador % 2) = 0 
							then 'SE CAPTURÓ LA SALIDA ' || _fechatext
							else 'SE CAPTURÓ LA ENTRADA ' || _fechatext 
					end;

			IF _Indicador = 2
			THEN
				_UltimaFechaHora := ( select Entrada from TBL_NOM_ASISTENCIAS where ID_Empleado = _ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento );
				-- SQLServer: IF datediff(minute, _UltimaFechaHora, _ID_FechaHora) >= _Diferencias
				IF (date_part('hour', _ID_FechaHora::time - _UltimaFechaHora::time) * 60 + date_part('minute', _ID_FechaHora::time - _UltimaFechaHora::time)) >= _Diferencias
				THEN
					UPDATE TBL_NOM_ASISTENCIAS
					SET Salida = _ID_FechaHora, Indicador = 2
					WHERE ID_Empleado = _ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento;
				ELSE
					_err := 3;
					_result := 'ERROR: NO SE PUEDE VOLVER A CAPTURAR LA ENTRADA '  || _ID_Empleado;
				END IF;
			ELSIF _Indicador = 3
			THEN
				_UltimaFechaHora := ( select Salida from TBL_NOM_ASISTENCIAS where ID_Empleado = _ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento );
				--IF datediff(minute, _UltimaFechaHora, _ID_FechaHora) >= _Diferencias
				IF (date_part('hour', _ID_FechaHora::time - _UltimaFechaHora::time) * 60 + date_part('minute', _ID_FechaHora::time - _UltimaFechaHora::time)) >= _Diferencias
				THEN
					UPDATE TBL_NOM_ASISTENCIAS
					SET Entrada2 = _ID_FechaHora, Indicador = 3
					WHERE ID_Empleado = _ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento;
				ELSE
					_err := 3;
					_result := 'ERROR: NO SE PUEDE VOLVER A CAPTURAR LA SALIDA '  || _ID_Empleado;
				END IF;
			ELSE 
				_UltimaFechaHora := ( select Entrada2 from TBL_NOM_ASISTENCIAS where ID_Empleado = _ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento );
				--IF datediff(minute, _UltimaFechaHora, _ID_FechaHora) >= _Diferencias
				IF (date_part('hour', _ID_FechaHora::time - _UltimaFechaHora::time) * 60 + date_part('minute', _ID_FechaHora::time - _UltimaFechaHora::time)) >= _Diferencias
				THEN
					UPDATE TBL_NOM_ASISTENCIAS
					SET Salida2 = _ID_FechaHora, Indicador = 4
					WHERE ID_Empleado = _ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento;
				ELSE
					_err := 3;
					_result := 'ERROR: NO SE PUEDE VOLVER A CAPTURAR LA SEGUNDA ENTRADA '  || _ID_Empleado;
				END IF;

			END IF;

		END IF;
		
	END IF;

	--	SELECT _ID_Empleado AS ID_Empleado, _result as Resultado

	IF _err = 0
	THEN
			RETURN QUERY 
			SELECT a.ID_FechaMovimiento, c.ID_Empleado, (c.Nombre || ' ' || c.Apellido_Paterno || ' ' || c.Apellido_Materno)::varchar as Nombre, 
											1 as RE, a.Entrada, case when a.Salida is null then 0 else 1 end as RS, a.Salida,
											case when a.Entrada2 is null then 0 else 1 end as RE2, a.Entrada2, case when a.Salida2 is null then 0 else 1 end as RS2, a.Salida2,	
											 _err as ERR, _result as RES
			FROM   TBL_NOM_MASEMP c INNER JOIN TBL_NOM_ASISTENCIAS a
				ON  a.ID_Empleado = c.ID_Empleado
			WHERE a.ID_FechaMovimiento = _ID_FechaMovimiento and c.ID_Empleado = _ID_Empleado;
	ELSE
			RETURN QUERY 
			SELECT _ID_FechaMovimiento as ID_FechaMovimiento, _ID_Empleado as ID_Empleado, ''::varchar as Nombre, 0 as RE, _ID_FechaHora as Entrada, 
					0 as RS, _ID_FechaHora as Salida, 0 as RE2, _ID_FechaHora as Entrada2, 0 as RS2, _ID_FechaHora as Salida2, _err as ERR, _result as RES;
	END IF;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_asistencias_server_agregar(character)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_calculo_nomina_vacaciones(
    _tipo smallint,
    _id_empleado character,
    _fecha_desde timestamp without time zone,
    _fecha_hasta timestamp without time zone)
  RETURNS SETOF record AS
$BODY$
DECLARE
	_TOTAL_DV smallint; _TOTAL_DVP smallint; _TOTAL_DVXF smallint; _TOTAL_DAV smallint; 
	_TOTAL_DVD smallint; _TOTAL_PV smallint; _DiasVac smallint; _PrimaVac numeric(2,2); _result varchar(254);

	_AnosCumplidos smallint; _Fecha_Ingreso timestamp; _Fecha_Corte timestamp; 
	_Permiso_Desde timestamp; _Permiso_Hasta timestamp;
BEGIN
	--SET DATEFIRST 1
	_TOTAL_DV := 0;
	_TOTAL_DVP := 0;
	_TOTAL_DVXF := 0;
	_TOTAL_DAV := 0;
	_TOTAL_DVD := 0;
	_TOTAL_PV := 0;
	_result := '';
	--primero calcula la antiguedad en a?os cumplidos, los dias de vacaciones y la prima vacacional
	_Fecha_Ingreso := (	select case when Fecha_Cambio_Empresa is null then Fecha_de_Ingreso else Fecha_Cambio_Empresa end 
						from TBL_NOM_MASEMP where ID_Empleado = _ID_Empleado	);
	_AnosCumplidos := getabsfechadiff('year', _Fecha_Ingreso, _Fecha_Hasta );
	_Fecha_Corte := _Fecha_Ingreso + (cast(_AnosCumplidos as text) || ' year')::interval; --dateadd(year,_AnosCumplidos,_Fecha_Ingreso)
	_DiasVac := case when
										(		select v.Dias --, v.PV 
												from TBL_NOM_VACACIONES v 
												where v.Desde <= _AnosCumplidos and v.Hasta >= _AnosCumplidos  limit 1 ) is null then 0 
					else
										(		select v.Dias --, v.PV 
												from TBL_NOM_VACACIONES v 
												where v.Desde <= _AnosCumplidos and v.Hasta >= _AnosCumplidos limit 1  ) 
					end;
					
	_PrimaVac := case when
										(		select v.PV 
												from TBL_NOM_VACACIONES v 
												where v.Desde <= _AnosCumplidos and v.Hasta >= _AnosCumplidos  limit 1 ) is null then 0 
						else
										(		select v.PV 
												from TBL_NOM_VACACIONES v 
												where v.Desde <= _AnosCumplidos and v.Hasta >= _AnosCumplidos limit 1 ) 
						end;
		
	-- Ahora calcula los permisos 
	-- empieza por las vacaciones ( se paga en la quincena apropiada ). Calcula los dias de vacaciones que corresponden al periodo de la nomina que se est? calculando
	-- Ejemplo si el permiso es del 10-ene al 20-ene y la nomina del 1-ene al 15 ene, calcula los dias del 10-ene al 15-ene, 
	--     y los restatnes los calcular? en el periodo de nomina siguiente ( del 16 al 31 de enero )
	_Permiso_Desde := ( 	select FechaHora_Desde
							from TBL_NOM_PERMISOS
							where ( FechaHora_Hasta >= _Fecha_Desde and 
																						FechaHora_Desde <= _Fecha_Hasta and
																								ID_Movimiento = -5 and ID_Empleado = _ID_Empleado ) limit 1 );
	_Permiso_Hasta := ( 	select FechaHora_Hasta
							from TBL_NOM_PERMISOS
							where ( FechaHora_Hasta >= _Fecha_Desde and 
																					FechaHora_Desde <= _Fecha_Hasta and
																								ID_Movimiento = -5 and ID_Empleado = _ID_Empleado ) limit 1 );
																								
	if(_Permiso_Desde is not null and _Permiso_Hasta is not null and _Permiso_Desde < _Permiso_Hasta)
	then
		-- calcula si ya tiene un a?o
		if(_AnosCumplidos < 1) -- si todavia no tiene ni un a?o lo rechaza
		then
			_TOTAL_DV := 0;			-- o si el permiso es antes de su fecha de cumplimiento de a?os tambien lo rechaza
			_result := _result || ' PRECAUCION: No se aplico el permiso de vacaciones porque no ha cumplido el año: ' + _ID_Empleado;
		elsif(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
		then
			_TOTAL_DV := (getfechadiff('day',_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval) ) +1) - getnum_dom(_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval));
		elsif(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
		then
			_TOTAL_DV := (getfechadiff('day',_Permiso_Desde,_Fecha_Hasta) +1) - getnum_dom(_Permiso_Desde,_Fecha_Hasta);
		elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
		then
			_TOTAL_DV := (getfechadiff('day',_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval)) +1) - getnum_dom(_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval));
		elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
		then
			_TOTAL_DV := (getfechadiff('day',_Fecha_Desde,_Fecha_Hasta) + 1) - getnum_dom(_Fecha_Desde,_Fecha_Hasta);
		end if;
	end if;  

	-- ahora los de las vacaciones pagada * fuera. ( se paga en la quincena apropiada ) ?????????????????????
	_Permiso_Desde := null;
	_Permiso_Hasta := null;
	_Permiso_Desde := ( 	select FechaHora_Desde
							from TBL_NOM_PERMISOS
							where ( FechaHora_Hasta >= _Fecha_Desde and 
												FechaHora_Desde <= _Fecha_Hasta and
													ID_Movimiento = -6 and ID_Empleado = _ID_Empleado) limit 1 );
	_Permiso_Hasta := ( 	select FechaHora_Hasta
							from TBL_NOM_PERMISOS
							where ( FechaHora_Hasta >= _Fecha_Desde and 
												FechaHora_Desde <= _Fecha_Hasta and
													ID_Movimiento = -6 and ID_Empleado = _ID_Empleado) limit 1 );
	if(_Permiso_Desde is not null and _Permiso_Hasta is not null and _Permiso_Desde < _Permiso_Hasta)
	then
		if(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
		then
			_TOTAL_DVXF := (getfechadiff('day',_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval) ) +1) - getnum_dom(_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval));
		elsif(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
		then
			_TOTAL_DVXF := (getfechadiff('day',_Permiso_Desde,_Fecha_Hasta) +1) - getnum_dom(_Permiso_Desde,_Fecha_Hasta);
		elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
		then
			_TOTAL_DVXF := (getfechadiff('day',_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval)) +1) - getnum_dom(_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval));
		elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
		then
			_TOTAL_DVXF := (getfechadiff('day',_Fecha_Desde,_Fecha_Hasta) + 1) - getnum_dom(_Fecha_Desde,_Fecha_Hasta);
		end if;
	end if;  

	-- ahora dias a cuenta de vacaciones. ( se paga toda la quincena )
	_Permiso_Desde := null;
	_Permiso_Hasta := null;
	_Permiso_Desde := ( 	select FechaHora_Desde
							from TBL_NOM_PERMISOS
							where ( ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and
																								ID_Movimiento = -7 and ID_Empleado = _ID_Empleado) limit 1 );
	_Permiso_Hasta := ( 	select FechaHora_Hasta
							from TBL_NOM_PERMISOS
							where ( ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and
																								ID_Movimiento = -7 and ID_Empleado = _ID_Empleado)  limit 1 );

	if(_Permiso_Desde is not null and _Permiso_Hasta is not null and _Permiso_Desde < _Permiso_Hasta)
	then
		_TOTAL_DAV = getfechadiff('day',_Permiso_Desde,_Permiso_Hasta) - getnum_dom(_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval));
	end if; 

	-- ahora dias de vacaciones pagadas. ( se pagan todos los dias en esta quincena )
	_Permiso_Desde := null;
	_Permiso_Hasta := null;
	_Permiso_Desde := ( 	select FechaHora_Desde
							from TBL_NOM_PERMISOS
							where ( ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and
																								ID_Movimiento = -8 and ID_Empleado = _ID_Empleado) limit 1 );
	_Permiso_Hasta = ( 	select FechaHora_Hasta
						from TBL_NOM_PERMISOS
						where ( ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and
																					ID_Movimiento = -8 and ID_Empleado = _ID_Empleado)  limit 1 );
	if(_Permiso_Desde is not null and _Permiso_Hasta is not null and _Permiso_Desde < _Permiso_Hasta)
	then
		_TOTAL_DVP := getfechadiff('day',_Permiso_Desde,_Permiso_Hasta) - getnum_dom(_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval)); --los domingos no los toma en cuenta si descontamos NUM_DOM
	end if;

	-- ahora dias de vacaciones disfrutadas. ( se paga en la quincena apropiada )
	_Permiso_Desde := null;
	_Permiso_Hasta := null;
	_Permiso_Desde := ( 	select FechaHora_Desde
							from TBL_NOM_PERMISOS
							where ( FechaHora_Hasta >= _Fecha_Desde and 
															FechaHora_Desde <= _Fecha_Hasta and
																								ID_Movimiento = -9 and ID_Empleado = _ID_Empleado)  limit 1 );
	_Permiso_Hasta := ( 	select  FechaHora_Hasta
							from TBL_NOM_PERMISOS
							where ( FechaHora_Hasta >= _Fecha_Desde and 
															FechaHora_Desde <= _Fecha_Hasta and
																								ID_Movimiento = -9 and ID_Empleado = _ID_Empleado) limit 1 );
	if(_Permiso_Desde is not null and _Permiso_Hasta is not null and _Permiso_Desde < _Permiso_Hasta)
	then
		if(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
		then
			_TOTAL_DVD := (getfechadiff('day',_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval) ) +1) - getnum_dom(_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval)); --los domingos no los toma en cuenta si descontamos NUM_DOM
		elsif(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
		then
			_TOTAL_DVD := (getfechadiff('day',_Permiso_Desde,_Fecha_Hasta) +1) - getnum_dom(_Permiso_Desde,_Fecha_Hasta);
		elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
		then
			_TOTAL_DVD := (getfechadiff('day',_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval)) +1) - getnum_dom(_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval));
		elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
		then
			_TOTAL_DVD := (getfechadiff('day',_Fecha_Desde,_Fecha_Hasta) + 1) - getnum_dom(_Fecha_Desde,_Fecha_Hasta);
		end if;
	end if;

	-- Ahora calcula la prima vacacional. ( se pagan todos los dias en esta quincena ) 
	_Permiso_Desde := null;
	_Permiso_Hasta := null;
	_Permiso_Desde = ( 	select ID_FechaMovimiento
							from TBL_NOM_PERMISOS
							where ( ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and
																		ID_Movimiento = -10 and ID_Empleado = _ID_Empleado) limit 1 );

	if(_Permiso_Desde is not null)
	then
		-- calcula si ya tiene un a?o y en caso de tenerlo, verifica que el permiso tenga una fecha posterior al la fecha de complimiento de antig?edad en la empresa 
		if(_AnosCumplidos < 1 or _Fecha_Corte > _Permiso_Desde) -- si todavia no tiene ni un a?o lo rechaza
		then
			_TOTAL_PV := _DiasVac;
			_result := _result || 'PRECAUCION: Se aplico la prima vacacional pero no ha cumplido el año: ' || _ID_Empleado;
		elsif( select count(*) from TBL_NOM_PERMISOS where  ID_Empleado = _ID_Empleado and ID_Movimiento = -10 and ID_FechaMovimiento between _Fecha_Corte and  (_Fecha_Desde - '1 day'::interval)) > 0
		then
			_TOTAL_PV := 0;
			_result := _result || 'PRECAUCION: No se aplico la prima vacacional porque ya se había aplicado antes en algun periodo: ' || _ID_Empleado;
		else
			_TOTAL_PV := _DiasVac;
		end if;
	end if;  

	RETURN QUERY
	SELECT _TOTAL_DV, _TOTAL_DVP, _TOTAL_DVXF, _TOTAL_DAV, _TOTAL_DVD, _TOTAL_PV, _DiasVac, _PrimaVac, _result;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_calculo_nomina_vacaciones(smallint, character, timestamp without time zone, timestamp without time zone)
  OWNER TO [[owner]];


--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_calculo_nomina(
    _id_nomina integer,
    _dpincon bit,
    _mesdesc smallint,
    _anodesc smallint,
    _hepf bit,
    _calvales bit)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_Ano smallint; _Numero_Nomina smallint; _Tipo smallint; _Fecha_Desde timestamp; _Fecha_Hasta timestamp; _Mes smallint;
	_TipoF smallint; _TipoV smallint; _TipoAg smallint; _Periodo smallint; _err int; _result varchar(4000); _resultflt varchar(1000); _resultvac varchar(1000); _ID_NominaF int;  _ID_NominaV int; 
	_DiasNomina smallint; _DiasNominaAc smallint; _DiasNominaAA smallint; _AnosCumplidos smallint; _DiasAg smallint; _ImporteAguinaldo numeric(19,4); _IAE numeric(10,2); _IAG numeric(10,2);
	_DiasAno smallint; _ID_Compania smallint; _ID_Sucursal smallint; 
	_M_SAL smallint; _M_FLT smallint; _M_IMSS smallint; _M_ISPT smallint; _M_ISPSB smallint; _M_CRDFS smallint;
	_M_IHEE smallint; _M_IHEG smallint; _M_IHE smallint; _M_IHDE smallint; _M_IHDG smallint; _M_PDE smallint; _M_PDG smallint;
	_M_IDV smallint; _M_IDVXF smallint; _M_IDAV smallint; _M_IDVP smallint; _M_IDVD smallint; _M_IPVE smallint; _M_IPVG smallint;
	_M_IAE smallint; _M_IAG smallint; _M_INFON smallint; _M_FONAC smallint; _M_IXM smallint; _M_IXA smallint; _M_IXE smallint;
	_M_DSP smallint; _M_DAP smallint; _M_DPI smallint; _M_CSIN smallint; _M_ACA smallint; _M_AAP smallint; _M_ASP smallint; _M_VALS smallint;

	_Inexistente bit; _FechaCont timestamp;
	
	_Fecha_Ingreso timestamp; _Fecha_DesdeNom timestamp; _Fecha_HastaNom timestamp; _DiasNominaNom smallint; _PromSueldoTurno numeric(19,4);
	_contTotal int; _contNum int; _isptFlag smallint; _ID_Empleado char(6); _NivelConfianza smallint; _CompensacionAnual smallint; _RANGO smallint; 
	_Sueldo numeric(19,4); _SalarioDiario numeric(19,4); _SalarioMinimo numeric(19,4); _SalarioPorHora numeric(19,4); _SalarioIntegrado numeric(19,4); _SalarioMixto numeric(19,4); _CalculoMixto bit; _SalarioTope numeric(19,4); 
	_SAVAR numeric(10,6); _DA numeric(5,2); _DAA numeric(5,2); _SA numeric(19,4); _SAA numeric(19,4); _GA numeric(19,4); _GAA numeric(19,4); _IMPAN numeric(19,4);_FI numeric(19,4);_LI numeric(19,4);
	_Exedente numeric(19,4);_CF numeric(19,4);_CFDT numeric(19,4);_CFDTGAA numeric(19,4);_IM numeric(19,4);_IME numeric(19,4);_AIM numeric(19,4);
	_Subsidio numeric(19,4);_CFS numeric(19,4);_SubTot numeric(19,4);_SubTotGAA numeric(19,4);_SubAcred numeric(19,4);_SubAA numeric(19,4);_ISPT numeric(10,2);
	_ISPSB numeric(10,2);_INGGET numeric(19,4);_INGM numeric(19,4);_ING numeric(19,4);_CSDiario numeric(19,4);_PRVCF numeric(10,2);_CRDFS numeric(10,2);_DT numeric(4,2);
	_TOTAL_HE numeric(6,2);_TOTAL_HD numeric(6,2);_TOTAL_HF numeric(6,2); _TOTAL_HT numeric(6,2); _MaxHE smallint; _Aplica_horas_Extras bit; _DiasHorasExtras smallint;
	_MHEE numeric(10,6);_IHEE numeric(10,2);_IHEG numeric(10,2);_IHDE numeric(10,2);_IHDG numeric(10,2);_IHE numeric(10,2);_PDE numeric(10,2);_PDG numeric(10,2);
	_ImporteHorasExtras numeric(19,4);_PrimaDominical numeric(19,4);_PRIDOM numeric(10,6);
	_TOTAL_DV smallint;_TOTAL_DVXF smallint;_TOTAL_DAV smallint;_TOTAL_DVP smallint;_TOTAL_DVD smallint;_TOTAL_PV smallint;_FAC_DVD numeric(10,6);
	_IV numeric(10,2);_IVXF numeric(10,2);_IAV numeric(10,2);_IVP numeric(10,2);_IVD numeric(10,2);_IPVE numeric(10,2);_IPVG numeric(10,2);
	_DiasVac smallint;_PrimaVac numeric(2,2);_ImportePrimaVac numeric(19,4);
	_SAL numeric(10,2);_IMP_DAP numeric(10,2);_FLT  numeric(10,2);_TOTAL_FLT decimal (4,2);_HRS_FLT decimal (6,2);_COMRETIMSS numeric(10,6);_IMSS numeric(10,2);_INFON numeric(10,2);_Prestamo_Fonacot numeric(19,4);_FONAC numeric(10,2);_FonAj smallint;_VALS numeric(10,2);
	_IIXM numeric(10,2);_IIXE  numeric(10,2);_IIXA numeric(10,2); _TOTAL_IXM decimal (4,2);_HRS_IXM decimal (6,2);
	_TOTAL_IXE numeric (4,2);_HRS_IXE numeric (6,2);_TOTAL_IXA numeric (4,2);_HRS_IXA numeric (6,2);_HRS_DESC numeric (6,2); _TOTAL_DAG numeric(10,6);
	_PPINCON numeric(5,2);_IMPPORINCON numeric(10,2);_CSIN numeric(10,6);_CUOTA_SINDICAL numeric(10,2);_Turno smallint;_DiasNominaSal numeric(12,9);_DiasNominaTurn numeric(12,9);_DiasNominaDiff numeric(12,9);_DiasNominaM numeric(12,9);_DTM numeric(12,9);
	_ACA numeric(10,6);_COMPENSACION_ANUAL numeric(10,2);_IMP_DSP numeric(10,2);_HRS_SINPAG decimal (6,2); _Castigo_Impuntualidad bit;

	_EMP RECORD;
BEGIN
	_err := 0;
	_result := 'La nomina se calculo satisfactoriamente';

	-- Revisa por los movimientos bsicos de permisos 
	IF (select count(*) from TBL_NOM_MOVIMIENTOS where ID_Movimiento between -32 and -1 ) < 29
	THEN
		_err := 3;
		_result := 'ERROR: No existen todos los movimientos básicos para permisos. Primero debe darlos de alta para poder crear la nomina'; 
	END IF; 
	
	-- Revisa por los movimientos bsicos del infonavit  fonacot y vales
	IF (select count(*) from TBL_NOM_MOVIMIENTOS_NOMINA where 
								Tipo_Movimiento = 'INFON' OR Tipo_Movimiento = 'FONAC' OR Tipo_Movimiento = 'VALS') < 3
	THEN
		_err := 3;
		_result := 'ERROR: No existen los movimientos de infonavit, fonacot y vales de despensa (INFON, FONAC, VALS). Primero debe darlos de alta para poder crear la nomina';
	END IF;
	-- Revisa por los movimientos bsicos de AGUINALDOS
	IF (select count(*) from TBL_NOM_MOVIMIENTOS_NOMINA where 
								Tipo_Movimiento = 'IAE' OR Tipo_Movimiento = 'IAG') < 2
	THEN
		_err := 3;
		_result := 'ERROR: No existen todos los movimientos de aguinaldo (IAG, IAE). Primero debe darlos de alta para poder crear la nomina de aguinaldos' ;
	END IF;
	-- Revisa por los movimientos bsicos de NOMINA ( POR RECIBO )
	IF (select count(*) from TBL_NOM_MOVIMIENTOS_NOMINA where 
								Tipo_Movimiento = 'SAL' OR Tipo_Movimiento = 'FLT' OR Tipo_Movimiento = 'IMSS'  
						OR 	Tipo_Movimiento = 'ISPT'  OR Tipo_Movimiento = 'ISPSB' OR Tipo_Movimiento = 'CRDFS' OR Tipo_Movimiento = 'CSIN') < 7
	THEN
		_err := 3;
		_result := 'ERROR: No existen todos los movimientos bsicos de nomina (SAL, FLT, IMSS, ISPT, ISPSB, CRDFS, AAP). Primero debe darlos de alta para poder crear la nomina';
	END IF;
	-- Revisa por los movimientos de horas extras y otros de NOMINA ( POR RECIBO )
	IF (select count(*) from TBL_NOM_MOVIMIENTOS_NOMINA where 
								Tipo_Movimiento = 'IHEE' OR Tipo_Movimiento = 'IHEG' OR Tipo_Movimiento = 'IHE'  
						OR 	Tipo_Movimiento = 'IHDE'  OR Tipo_Movimiento = 'IHDG' OR Tipo_Movimiento = 'PDE' 
						OR 	Tipo_Movimiento = 'PDG') < 7
	THEN
		_err := 3;
		_result := 'ERROR: No existen todos los movimientos de horas extras (IHEE, IHEG, IHE, IHDE, IHDG, PDE, PDG). Primero debe darlos de alta para poder crear la nomina';
	END IF;
	-- Revisa por los movimientos de vacaciones de NOMINA ( POR RECIBO )
	IF (select count(*) from TBL_NOM_MOVIMIENTOS_NOMINA where -- DV DVXF DAV DVP DVD PV
								Tipo_Movimiento = 'IDV' OR Tipo_Movimiento = 'IDVXF' OR Tipo_Movimiento = 'IDAV'  
						OR 	Tipo_Movimiento = 'IDVP'  OR Tipo_Movimiento = 'IDVD' OR Tipo_Movimiento = 'IPVE' OR Tipo_Movimiento = 'IPVG') < 7
	THEN
		_err := 3;
		_result := 'ERROR: No existen todos los movimientos de vacaciones ( IDV, IDVXF, IDAV, IDVP, IDVD, IPVE, IPVG ). Primero debe darlos de alta para poder crear la nomina';
	END IF;
	-- Revisa por los otros movimientos de NOMINA ( POR RECIBO ) como los descuentos por llegar tarde cuota sindical etc
	IF (select count(*) from TBL_NOM_MOVIMIENTOS_NOMINA where -- DAP
								Tipo_Movimiento = 'DAP' OR Tipo_Movimiento = 'DPI' OR Tipo_Movimiento = 'DSP' 
									OR 	Tipo_Movimiento = 'AAP'  OR 	Tipo_Movimiento = 'ASP' OR 	Tipo_Movimiento = 'ACA' ) < 6
	THEN
		_err := 3;
		_result := 'ERROR: No existen todos los movimientos de nomina alternos ( DAP, DPI, DSP, AAP, ASP, ACA ). Primero debe darlos de alta para poder crear la nomina';
	END IF;

	-- Revisa por los movimientos de incapacidad de NOMINA ( POR RECIBO )
	IF (select count(*) from TBL_NOM_MOVIMIENTOS_NOMINA where -- IXM, IXE, IXA
									Tipo_Movimiento = 'IXM' OR Tipo_Movimiento = 'IXE' OR Tipo_Movimiento = 'IXA') < 3
	THEN
		_err := 3;
		_result := 'ERROR: No existen todos los movimientos de incapacidad ( IXM, IXE, IXA ). Primero debe darlos de alta para poder crear la nomina'; 
	END IF;

	-- Revisa por variables de sistema
	IF (select VDecimal from TBL_VARIABLES
					where ID_Variable = 'SA') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable SA ( Subsidio acumulado ), No está definida'; 
	END IF;
	IF (select VDecimal from TBL_VARIABLES
				where ID_Variable = 'SALMIN') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable SALMIN ( Salario M?nimo ), No está definida';
	END IF;
	IF (select VDecimal from TBL_VARIABLES
				where ID_Variable = 'SALTOP') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable SALTOP ( Salario Tope ), No está definida'; 
	END IF;
	IF (select VDecimal from TBL_VARIABLES
				where ID_Variable = 'PRIDOM') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable PRIDOM ( Prima Dominical ), No esta definida';
	END IF;
	IF (select VDecimal from TBL_VARIABLES
					where ID_Variable = 'PPINCON') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable PPINCON ( Porcentaje de descuento por inconcistencias ), No esta definida';
	END IF;
	IF (select VDecimal from TBL_VARIABLES
					where ID_Variable = 'CSIN') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable CSIN ( Cuota Sindical ), No esta definida';
	END IF;
	IF (select VDecimal from TBL_VARIABLES
					where ID_Variable = 'ACA') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable ACA ( Ahorro, Compensaciones, Anualidades ), No esta definida';
	END IF;
	IF (select VEntero from TBL_VARIABLES
					where ID_Variable = 'FONAJ') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable FONAJ ( Ajuste de meses para el fonacot ), No esta definida';
	END IF;
	IF (select VEntero from TBL_VARIABLES
					where ID_Variable = 'NOMVALS') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable NOMVALS ( N?mina indepEND IF;iente para vales ), No esta definida';
	END IF;
	IF (select VDecimal from TBL_VARIABLES
					where ID_Variable = 'COMRETIMSS') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable COMRETIMSS ( Factor de complemento de retenci?n del seguro social ), No esta definida';
	END IF;
	IF (select VEntero from TBL_VARIABLES
					where ID_Variable = 'MAX_HE') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable MAX_HE ( MAXIMO DE HORAS EXTRAS ACUMULADAS ), No esta definida';
	END IF;
	IF (select VDecimal from TBL_VARIABLES
					where ID_Variable = 'MHEE') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable MHEE ( BASE PARA HORAS EXTRAS EXENTAS ), No esta definida';
	END IF;
	IF (select VDecimal from TBL_VARIABLES
					where ID_Variable = 'FAC_DVD') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable FAC_DVD ( FACTOR DE VACACIONES DISFRUTADAS ), No esta definida';
	END IF;
	IF (select VDecimal from TBL_VARIABLES
					where ID_Variable = 'VARISPT') is null
	THEN
		_err := 3;
		_result := 'ERROR: La variable VARISPT ( Variacion del calculo del ISPT sobre positivos), No esta definida';
	END IF;
		
	IF (select count(*) from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina and (Tipo = 1 or Tipo = 2 or Tipo = 5 or Tipo = 6) and Cerrado = '0') < 1
	THEN
		_err := 3;
		_result := 'ERROR: La nómina mandada no se puede calcular por alguna de las siguientes razones: <br>1) Ya esta cerrada o generada.<br>2) Se intenta calcular una nómina especial o de vales, cuando estas se calculan automaticamente por medio de la nómina normal.<br>3) La nómina por algun motivo no existe y se debe primero crear.<br> ';
	ELSE
		_ID_Compania := (select ID_Compania from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
		_ID_Sucursal := (select ID_Sucursal from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
		_Periodo := ( 	select case 	when Periodo = 'sem' then 7
										when Periodo = 'qui' then 15
										when Periodo = 'men' then 30
										else 30 end
						from TBL_COMPANIAS 
						where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal );
		_Ano := (select Ano from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
		_Numero_Nomina := (select Numero_Nomina from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
		_Tipo := (select Tipo from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
		_Fecha_Desde := (select Fecha_Desde from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
		_Fecha_Hasta := (select Fecha_Hasta from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
		_DiasNomina := (select Dias from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
		_Mes := (select Mes from TBL_NOM_CALCULO_NOMINA where ID_Nomina = _ID_Nomina );
		--**************************************************************************************************
		_DiasAno := getfechadiff('day', ('01-01-' || cast(_Ano as varchar))::timestamp, ('01-01-' || cast((_Ano + 1) as varchar))::timestamp );
		_TipoF := ( case when _Tipo = 2 then 4 when _Tipo = 1 then 3 else 0 end );
		_TipoV := ( case when _Tipo = 2 then 8 when _Tipo = 1 then 7 else 0 end );
		_TipoAg := ( case when _Tipo = 6 then 6 when _Tipo = 5 then 5 else 0 end );
		-- si es nomina semanal, revisa que esten cerrados cada uno de los dias de la nomina, de lo contrario no se calcula
		IF _Tipo = 1
		THEN
			_Inexistente = '0';
			_FechaCont = _Fecha_Desde;
			WHILE _FechaCont <= _Fecha_Hasta
			LOOP
				IF( select count(*) from TBL_NOM_DIARIO_CAB 
						where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and 
										ID_FechaMovimiento = _FechaCont /* and Cerrado = 1*/ ) = 0
				THEN
					_Inexistente = '1';
					EXIT;
				END IF;
				--print ' Fecha: ' + cast(_FechaCont as varchar)
				_FechaCont := _FechaCont + '1 day'::interval; --DATEADD(day, 1, _FechaCont);
				
			END LOOP;

			IF _Inexistente = '1'
			THEN
				_err := 3;
				_result := 'ERROR: La nómina que se intenta calcular es estricta y sin embargo no se han cerrado todos los dias que incluyen esta nómina. Necesitas cerrar todos los dias que incluyen esta nómina desde el módulo de cierre diario para poderla calcular';
			END IF;
		END IF;
	END IF;


	-- AHORA SI, DESPUES DE VERIFICAR LAS variables y ciertos parametros necesarios, procede a calcular la n?mina
	IF _err = 0
	THEN
		-- ///////////////////////////////////////////////////////////////////////////////////////	
		-- ////////////////////////  Asigna los movimientos de nomina //////////////////////////
		_M_SAL := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'SAL' );
		_M_FLT := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'FLT' );
		_M_IMSS := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IMSS' );
		_M_ISPT := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'ISPT' );
		_M_ISPSB := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'ISPSB' );
		_M_CRDFS := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'CRDFS' );
		_M_IHEE := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IHEE' );
		_M_IHEG := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IHEG' );
		_M_IHE := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IHE' );
		_M_IHDE := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IHDE' );
		_M_IHDG := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IHDG' );
		_M_PDE := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'PDE' );
		_M_PDG := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'PDG' );
		_M_IDV := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IDV' );
		_M_IDVXF := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IDVXF' );
		_M_IDAV := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IDAV' );
		_M_IDVP := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IDVP' );
		_M_IDVD := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IDVD' );
		_M_IPVE := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IPVE' );
		_M_IPVG := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IPVG' );
		_M_IAE := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IAE' );
		_M_IAG := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IAG' );
		_M_INFON := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'INFON' );
		_M_FONAC := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'FONAC' );
		_M_VALS := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'VALS' );
		_M_IXM := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IXM' );
		_M_IXE := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IXE' );
		_M_IXA := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'IXA' );
		_M_DAP := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'DAP' );
		_M_DPI := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'DPI' );
		_M_CSIN := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'CSIN' );
		_M_ACA := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'ACA' );
		_M_DSP := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'DSP' );
		_M_AAP := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'AAP' );
		_M_ASP := ( select ID_Movimiento	from TBL_NOM_MOVIMIENTOS_NOMINA	where Tipo_Movimiento = 'ASP' );
		-- //////////////////////////////////////////////////////////////////////////////////
		--/////////////////////////////// crea las tablas temporales ////////////////////////
		CREATE LOCAL TEMPORARY TABLE _TMP_CALCULO_NOMINA_DET (
			ID_Nomina int NOT NULL ,
			ID_Empleado char (6)  NOT NULL ,
			ID_Movimiento smallint NOT NULL ,
			Gravado numeric(10, 2) NOT NULL ,
			Exento numeric(10, 2) NOT NULL ,
			Deduccion numeric(10, 2) NOT NULL 
		);

		CREATE LOCAL TEMPORARY TABLE _TMP_CALCULO_NOMINA_ESP (
			ID_Nomina int NOT NULL ,
			ID_Empleado char (6)  NOT NULL ,
			Dias numeric(6,2) NOT NULL ,
			Faltas numeric(4,2) NOT NULL ,
			Recibo smallint NOT NULL,
			HE numeric(9,6) NOT NULL ,
			HD numeric(9,6) NOT NULL,
			ID_CFD integer,
			TFD smallint,
			Gravado numeric(10,2) NOT NULL,
			Exento numeric(10,2) NOT NULL,
			Deduccion numeric(10,2) NOT NULL,
			ISR numeric(10,2) NOT NULL,
			HT numeric(9,6) NOT NULL,
			IXA numeric(5,2) NOT NULL,
			IXE numeric(5,2) NOT NULL,
			IXM numeric(5,2) NOT NULL,
			DiasHorasExtras smallint NOT NULL

		);
		
		CREATE LOCAL TEMPORARY TABLE _TMP_CALCULO_NOMINA_ASIST (
			ID_Empleado char (6)  NOT NULL ,
			ID_FechaMovimiento timestamp NOT NULL ,
			ID_Movimiento smallint NOT NULL ,
			Entrada timestamp NULL ,
			Salida timestamp NULL
		);
		
		CREATE LOCAL TEMPORARY TABLE _TMP_CALCULO_NOMINA_HIS_DET_IMP (
			ID_Nomina int NOT NULL ,
			ID_Empleado char (6)  NOT NULL ,
			IMPDAP numeric(10,2) NULL ,
			IMPDSP numeric(10,2) NULL ,
			DiasNomina smallint NULL ,
			DT numeric(5,2) NULL ,
			SA numeric(19,4) null ,
			SAA numeric(19,4) null ,
			DA numeric(5,2) null ,
			DAA numeric(5,2) null ,
			IMPAN numeric(19,4) null ,
			GA numeric(19,4) null ,
			GAA numeric(19,4) null ,
			FI numeric(19,4) null ,
			LI numeric(19,4) null ,
			SalarioTope numeric(19,4) null ,
			Exedente numeric(19,4) null ,
			CF numeric(19,4) null ,
			CFDTGAA numeric(19,4) null ,
			CFDT numeric(19,4) null ,
			IME numeric(19,4) null ,
			IM numeric(19,4) null ,
			AIM numeric(19,4) null ,
			ISPT numeric(10,2) null,
			Subsidio numeric(19,4) null ,
			CFS numeric(19,4) null ,
			SubTot numeric(19,4) null ,
			SubTotGAA numeric(19,4) null ,
			ISPSB numeric(10,2) null
		);
		-- Crea tabla de analisis por trabajador
		CREATE LOCAL TEMPORARY TABLE _TMP_ANALISIS (
			ID_Num serial NOT NULL , 
			ID_Empleado char (6)  NOT NULL ,
			Salario_Diario numeric(8, 2) NOT NULL ,
			ISPT numeric(10, 2) NOT NULL ,
			ISPSB numeric(10, 2) NOT NULL ,
			IAE numeric(10, 2) NULL ,
			IAG numeric(10, 2) NULL ,
			CRDFS numeric(10, 2) NOT NULL ,
			IHEE numeric(10, 2) NOT NULL ,
			IHEG numeric(10, 2) NOT NULL ,
			IHE numeric(10, 2) NOT NULL ,
			IHDE numeric(10, 2) NOT NULL ,
			IHDG numeric(10, 2) NOT NULL ,
			PDE numeric(10, 2) NOT NULL ,
			PDG numeric(10, 2) NOT NULL ,
			IDV numeric(10, 2) NOT NULL ,
			IDVXF numeric(10, 2) NOT NULL ,
			IDAV numeric(10, 2) NOT NULL ,
			IDVP numeric(10, 2) NOT NULL ,
			IDVD numeric(10, 2) NOT NULL ,
			IPVE numeric(10, 2) NOT NULL ,
			IPVG numeric(10, 2) NOT NULL ,
			SAL numeric(10, 2) NOT NULL ,
			FLT numeric(10, 2) NOT NULL ,
			IMSS numeric(10, 2) NOT NULL ,
			INFON numeric(10, 2) NOT NULL ,
			FONAC numeric(10, 2) NOT NULL ,
			VALS numeric(10, 2) NOT NULL ,
			IXM numeric(10, 2) NOT NULL ,
			IXE numeric(10, 2) NOT NULL ,
			IXA numeric(10, 2) NOT NULL ,
			DAP numeric(10, 2) NOT NULL ,
			DPI numeric(10, 2) NOT NULL ,
			CSIN numeric(10,2) NOT NULL ,
			ACA numeric(10,2) NOT NULL ,
			DSP numeric(10,2) NOT NULL
		);

		-- Crea tabla temporal de permisos
		CREATE LOCAL TEMPORARY TABLE  _TMP_PERMISOS (
			ID_Num serial NOT NULL , 
			ID_Movimiento smallint NOT NULL ,
			ID_FechaMovimiento timestamp NOT NULL ,
			DiasCompletos bit NOT NULL ,
			FechaHora_Desde timestamp NOT NULL ,
			FechaHora_Hasta timestamp NOT NULL ,
			Num_de_Dias smallint NOT NULL ,
			Num_de_Horas numeric(4, 2) NOT NULL ,
			Tiempo_por_pagar numeric(4, 2) NOT NULL
		);
		-- Ahora crea tabla temporal para agrupar los totales de cada empleado por movimientos dinmicos
		-- Esta la utiliza el modulo externo sp_calculo_nomina_dinamicos
		CREATE LOCAL TEMPORARY TABLE  _TMP_PLANTILLAS_TOTALES (
			Tipo smallint NOT NULL ,
			ID_Empleado char(6)  NOT NULL,
			ID_Movimiento smallint NOT NULL ,
			TOTAL_GRAVADO numeric(19,4) NOT NULL ,
			TOTAL_EXENTO numeric(19,4) NOT NULL,
			TOTAL_DEDUCCION numeric(19,4) NOT NULL,
			ISPT bit NOT NULL
		);
		
		--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		--///////////// como ya estamos seguros de que existe la nomina, borra el detalle, el especial y el historial ///////////
		DELETE FROM TBL_NOM_CALCULO_NOMINA_DET
		WHERE ID_Nomina = _ID_Nomina;
	
		DELETE FROM TBL_NOM_CALCULO_NOMINA_ESP
		WHERE ID_Nomina = _ID_Nomina;
		
		DELETE FROM TBL_NOM_CALCULO_NOMINA_HIS_DET_IMP
		WHERE ID_Nomina = _ID_Nomina;
			
		DELETE FROM TBL_NOM_CALCULO_NOMINA_ASIST
		WHERE ID_Nomina = _ID_Nomina;
			
		-- Ahora revisa las nominas de vales y especial
		_ID_NominaF := (select ID_Nomina 
						from TBL_NOM_CALCULO_NOMINA 
						where ID_Compania = _ID_Compania and 
											ID_Sucursal = _ID_Sucursal and 
													Ano = _Ano and Numero_Nomina = _Numero_Nomina and Tipo = _TipoF );
		
		IF _ID_NominaF is not null -- Si si existe la nomina especial revisa:
		THEN
			-- como ya existe la nomina especial , borra el cabecero, y borra el detalle y el especial, y el historal de impuestos y asistencias
			DELETE FROM TBL_NOM_CALCULO_NOMINA
			WHERE ID_Nomina = _ID_NominaF;
			
			DELETE FROM TBL_NOM_CALCULO_NOMINA_DET
			WHERE ID_Nomina = _ID_NominaF;
			
			DELETE FROM TBL_NOM_CALCULO_NOMINA_ESP
			WHERE ID_Nomina = _ID_NominaF;
			
			DELETE FROM TBL_NOM_CALCULO_NOMINA_HIS_DET_IMP
			WHERE ID_Nomina = _ID_NominaF;
			
			DELETE FROM TBL_NOM_CALCULO_NOMINA_ASIST
			WHERE ID_Nomina = _ID_NominaF;
		
		END IF;
	
		_ID_NominaV := (select ID_Nomina 
						from TBL_NOM_CALCULO_NOMINA 
						where ID_Compania = _ID_Compania and 
										ID_Sucursal = _ID_Sucursal and 
												Ano = _Ano and Numero_Nomina = _Numero_Nomina and Tipo = _TipoV );
	
		IF _ID_NominaV is not null -- Si si existe la nomina de vales revisa:
		THEN
			-- como ya existe la nomina de vales, borra el cabecero, y borra el detalle y el especial, y el historal de impuestos y asistencias
			DELETE FROM TBL_NOM_CALCULO_NOMINA
			WHERE ID_Nomina = _ID_NominaV;
		
			DELETE FROM TBL_NOM_CALCULO_NOMINA_DET
			WHERE ID_Nomina = _ID_NominaV;
		
			DELETE FROM TBL_NOM_CALCULO_NOMINA_ESP
			WHERE ID_Nomina = _ID_NominaV;
		
			DELETE FROM TBL_NOM_CALCULO_NOMINA_HIS_DET_IMP
			WHERE ID_Nomina = _ID_NominaV;
		
			DELETE FROM TBL_NOM_CALCULO_NOMINA_ASIST
			WHERE ID_Nomina = _ID_NominaV;
		
		END IF;
		--- /////////////// fin borrado de nominas anteriores creadas especial y vales (si las habia)


		--////////////////// ahora si es de aguinaldo cambia a su tipo normal correspondiente /////////////////////////////////////
		IF _Tipo = 5 or _Tipo = 6 
		THEN
			_Tipo = _Tipo - 4; 
		END IF;		
		-- /////////////////////// calcula los dias acumulados del a?o /////////////////////
		_DiasNominaAc := ( 	select sum(Dias) 
							from TBL_NOM_CALCULO_NOMINA
							where Ano = _Ano and Tipo = _Tipo and 
											ID_Sucursal = _ID_Sucursal and 
													ID_Compania = _ID_Compania and 
														Numero_Nomina < _Numero_Nomina );

		IF _TipoAg = 0
		THEN
			_result := 'La nómina se calculó satisfactoriamente';
			_DiasNominaAA := ( case when _DiasNominaAc is null then _DiasNomina else _DiasNominaAc + _DiasNomina end );
		ELSE
			_result := 'La nómina de aguinaldos se calculó satisfactoriamente';
			_DiasNominaAA = ( case when _DiasNominaAc is null then 0 else _DiasNominaAc end );
		END IF;
		--/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		--///////////////////////////////// AHORA ANALIZA A CADA EMPLEADO /////////////////////////////////////////////
		--///////////////////////////////////////////////////////////////////////////////////////////////////////////// 
		_contNum := 1;
		_contTotal := (select count(*) from _TMP_ANALISIS);
		-- Aplica las variables a la variable del procedimiento
		_SalarioMinimo := (	select VDecimal from TBL_VARIABLES	
							where ID_Variable = 'SALMIN' );
		_SalarioTope := (	select VDecimal from TBL_VARIABLES	
							where ID_Variable = 'SALTOP' );
		_PPINCON := 	(	select VDecimal from TBL_VARIABLES
							where ID_Variable = 'PPINCON' );
		_FAC_DVD := (	select VDecimal from TBL_VARIABLES	-- EL FACTOR DE VACACIONES DISFRUTADAS
						where ID_Variable = 'FAC_DVD' );
		_MaxHE := (	select VEntero	from TBL_VARIABLES	
					where ID_Variable = 'MAX_HE' );
		_MHEE := (	select VDecimal from TBL_VARIABLES	
					where ID_Variable = 'MHEE' );
		_PRIDOM := (	select VDecimal from TBL_VARIABLES	
						where ID_Variable = 'PRIDOM' );
		_COMRETIMSS := (	select VDecimal from TBL_VARIABLES	
							where ID_Variable = 'COMRETIMSS' );
		_SAVAR := (	select VDecimal from TBL_VARIABLES 
					where ID_Variable = 'SA' );
		_CSIN := (	select VDecimal	from TBL_VARIABLES
					where ID_Variable = 'CSIN' );
		_ACA := (	select VDecimal	from TBL_VARIABLES
					where ID_Variable = 'ACA' );
		_FonAj := (	select VEntero	from TBL_VARIABLES	-- EL AJUSTE DE MESES PARA EL FONACOT
					where ID_Variable = 'FONAJ' );

		_DiasNominaM :=  ( 	select (case when sum(Dias) is null then 0 else sum(Dias) end )
							from TBL_NOM_CALCULO_NOMINA 
							where  ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and 
									Tipo = _Tipo and Ano = _Ano and Mes = _Mes and Numero_Nomina < _Numero_Nomina );
												
		-- TRASPASA LOS DIAS DE NOMINA Y LAS FECHAS DE ESTA
		_Fecha_DesdeNom := _Fecha_Desde;
		_Fecha_HastaNom := _Fecha_Hasta;
		_DiasNominaNom := getfechadiff('day', _Fecha_DesdeNom, _Fecha_HastaNom) + 1;
		-- FIN TRASPASO
		
		FOR _EMP IN (
						SELECT * FROM TBL_NOM_MASEMP me
						WHERE me.Tipo_de_nomina = _Tipo and me.ID_Compania = _ID_Compania and me.ID_Sucursal = _ID_Sucursal  and me.Fecha_de_Ingreso <= _Fecha_Hasta and 
							( ( me.Status = 0 ) or ( me.Status = 2 and me.Fecha_para_Liquidaciones >= _Fecha_Desde) ) -- solo los de la compañia y sucursal tal dados de alta
					)
		LOOP
			_ID_Empleado := _EMP.ID_Empleado;
			_NivelConfianza := _EMP.Sindicalizado;
			_CompensacionAnual := _EMP.CompensacionAnual;
			_SalarioDiario := _EMP.Salario_Diario; -- SALARIO DIARIO EN EL MASEMP
			_SalarioPorHora := _EMP.Salario_por_Hora; -- EL SALARIO POR HORA DEL MASEMP
			_SalarioIntegrado := _EMP.Salario_Integrado; -- EL SALARIO INTEGRADO DEL MASEMP
			_Turno := _EMP.ID_Turno;
			_SalarioMixto := _EMP.Salario_Mixto; -- SALARIO MIXTO EN EL MASEMP
			_CalculoMixto := _EMP.CalculoMixto; -- 1 SI APLICA EL SALARIO MIXTO
			_Fecha_Ingreso := _EMP.Fecha_de_Ingreso;
			_Castigo_Impuntualidad := _EMP.Castigo_Impuntualidad;
			
			--ESTABLECE LOS DIAS DE NOMINA Y FECHAS DE NOMINA DEL EMPLEADO USANDO EL TRASPASO ANTERIOR
			--SOLO SI NO ES DE AGUINALDO
			IF _TipoAg = 0
			THEN
				IF _Fecha_Ingreso > _Fecha_DesdeNom
				THEN
					_Fecha_Desde := _Fecha_Ingreso;
				ELSE
					_Fecha_Desde := _Fecha_DesdeNom;
				END IF;

				IF _EMP.Fecha_para_Liquidaciones is null
				THEN
					_Fecha_Hasta := _Fecha_HastaNom;
				ELSIF _EMP.Fecha_para_Liquidaciones < _Fecha_HastaNom
				THEN
					_Fecha_Hasta := EMP.Fecha_para_Liquidaciones;
				ELSE -- en este caso es igual o mayor 
					_Fecha_Hasta := _Fecha_HastaNom;
				END IF;
			END IF;
				
			_DiasNomina := getfechadiff('day', _Fecha_Desde, _Fecha_Hasta) + 1;	
					
			IF _Fecha_Desde > _Fecha_Hasta -- Aqui supone un error logico de fechas, lo cual hace que este empleado no proceda.
			THEN
				raise notice 'Error logico de fechas, la fecha de inicio de nomina es mayor al del final para el empleado %', _EMP.ID_Empleado;
				CONTINUE;
			END IF;
			
			--FIN DE ESTABLECIMIENTOS DE DIAS Y FECHAS DE NOMINA DE ESTE EMPLEADO
			_Sueldo := ROUND(_SalarioDiario * _DiasNomina, 2);

			--////////////////////////////////////////////////////////////////// calcula cabeceros de solo nomina de aguinaldos ////////////////////////////////////////////////////////////
			IF _TipoAg <> 0
			THEN
				_AnosCumplidos := getabsfechadiff('year',_Fecha_Ingreso,_Fecha_Hasta);
				_TOTAL_DAG := case when _Fecha_Ingreso < _Fecha_Desde
										then getfechadiff('day',_Fecha_Desde, _Fecha_Hasta) + 1
										else getfechadiff('day',_Fecha_Ingreso, _Fecha_Hasta) + 1 
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
					_DT := ROUND((_TOTAL_DAG / _DiasAno) * _DiasAg, 1 );
					_ImporteAguinaldo := ROUND( ((case when _CalculoMixto = '0' then _SalarioDiario else _SalarioMixto end) * (cast((_TOTAL_DAG * _DiasAg) as numeric) / _DiasAno )), 2);
					_IAE := case when ROUND( ((_TOTAL_DAG / _DiasAno) * (_SalarioMinimo * 30)), 2) < _ImporteAguinaldo 
								then  ROUND( ((_TOTAL_DAG / _DiasAno) * (_SalarioMinimo * 30)), 2) 
								else _ImporteAguinaldo 
							end;
					_IAG := case when _IAE < _ImporteAguinaldo then _ImporteAguinaldo - _IAE else 0.00 end;	
				ELSE
					_DT := 0.0;
					_IAE := 0.00;
					_IAG := 0.00;		
		 		END IF;

				IF _DT is null THEN _DT := 0; END IF;
				
		 		INSERT INTO _TMP_CALCULO_NOMINA_ESP
				VALUES(_ID_Nomina, _ID_Empleado, _DT, 0, _contNum, 0.0, 0.0,
						null,null,0.0,0.0,0.0,0.0,0.0,0,0,0,0);
			END IF;
			--///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			IF _TipoAg = 0
			THEN
				-- //////////////////////////////////////////////////////////////////
				-- AHORA CALCULA LOS RETARDOS, LAS FALTAS E INCAPACIDADES EN TODAS SUS MODALIDADES 
				-- primero calcula llamando al mdulo exterior, aqui manda la variable _IMPDESC que se calcula directamente en el modulo
				SELECT * INTO    _TOTAL_FLT, _TOTAL_IXM, _TOTAL_IXE, _TOTAL_IXA, _resultflt, 
								_IMP_DAP, _HRS_DESC, _HRS_FLT, _HRS_IXM, _HRS_IXE, _HRS_IXA,
								_IMPPORINCON, _IMP_DSP, _HRS_SINPAG
				FROM sp_nom_calculo_nomina_faltas( _Tipo, _DPIncon, _MesDesc, _AnoDesc, _PPINCON, _ID_Empleado, _Fecha_Desde, _Fecha_Hasta,
														_SalarioPorHora, _SalarioDiario, _Turno, _CalculoMixto, _SalarioMixto, _Fecha_Ingreso, _Castigo_Impuntualidad ) 
				AS 
						(
							TOTAL_FLT numeric(4,2), TOTAL_IXM numeric(4,2), TOTAL_IXE numeric(4,2), TOTAL_IXA numeric(4,2), result varchar(254),
							IMPDESC numeric(10,2), HRS_DESC numeric(6,2), HRS_FLT numeric(6,2), HRS_IXM numeric(6,2), HRS_IXE numeric(6,2), HRS_IXA numeric(6,2),
							IMPPORINCON numeric(10,2), IMPSINPAG numeric(10,2), HRS_SINPAG numeric(6,2)
						);
				if(_resultflt is not null and _resultflt <> '')
				then
					_result := _result || '<br>' || _resultflt;
				end if;
				-- AHORA QUE YA TIENE LOS DATOS, CALCULA LOS RESULTADOS EN PESOS EN LAS VARIABLES _FLT numeric(10,2), _IIXM numeric(10,2), _IIXE numeric(10,2), _IIXA numeric(10,2)
				-- Primero calcula las faltas, incapacidad por maternidad, por enfermedad y por accidente
				_FLT :=  round((case when _CalculoMixto = '0' then _SalarioDiario else _SalarioMixto end ) * _TOTAL_FLT,2);
				_IIXM := round(_SalarioDiario *	_TOTAL_IXM,2);
				_IIXE := round(_SalarioDiario *	_TOTAL_IXE,2);
				_IIXA := round(_SalarioDiario *	_TOTAL_IXA,2);

				IF _FLT is null THEN _FLT := 0; END IF;
				IF _IIXM is null THEN _IIXM := 0; END IF;
				IF _IIXE is null THEN _IIXE := 0; END IF;
				IF _IIXA is null THEN _IIXA := 0; END IF;
				IF _IMP_DAP is null THEN _IMP_DAP := 0; END IF;
				IF _IMPPORINCON is null THEN _IMPPORINCON := 0; END IF;
				IF _IMP_DSP is null THEN _IMP_DSP := 0; END IF;
			
				--/////////////////////////// fin faltas e incapacidades ///////////////////////////////////

				-- //////////////////////////////////////////////////////////////////
				-- AHORA CALCULA LAS vacaciones EN TODAS SUS MODALIDADES 

				-- primero calcula los dias llamando al mdulo exterior
				SELECT * INTO 	_TOTAL_DV, _TOTAL_DVP, _TOTAL_DVXF, 
								_TOTAL_DAV, _TOTAL_DVD, _TOTAL_PV, _DiasVac, _PrimaVac, _resultvac
				FROM sp_nom_calculo_nomina_vacaciones( _Tipo, _ID_Empleado, _Fecha_Desde, _Fecha_Hasta)
				AS
					(
						TOTAL_DV smallint, TOTAL_DVP smallint, TOTAL_DVXF smallint, 
						TOTAL_DAV smallint, TOTAL_DVD smallint, TOTAL_PV smallint, DiasVac smallint, PrimaVac numeric(2,2), result varchar(254)
					);
				if(_resultvac is not null  and _resultvac <> '')
				then
					_result := _result || '<br>' || _resultvac;
				end if;
				-- AHORA QUE YA TIENE LOS DATOS, CALCULA LOS RESULTADOS EN PESOS EN LAS VARIABLES _IV numeric(10,2), _IVXF numeric(10,2), _IAV numeric(10,2), _IVP numeric(10,2), _IVD numeric(10,2), _IPV numeric(10,2),
				-- Primero calcula las vacaciones, vacaciones por fuera, vacaciones pagadas, a cuenta de vacaciones y vacaciones disfrutadas
				_IV := ((case when _CalculoMixto = '0' then _SalarioDiario else _SalarioMixto end) * _TOTAL_DV); -- importe de vacvaciones
				_IVP := ((case when _CalculoMixto = '0' then _SalarioDiario else _SalarioMixto end) * _TOTAL_DVP); -- importe de vacaciones pagadas
				_IVXF := ((case when _CalculoMixto = '0' then _SalarioDiario else _SalarioMixto end) * _TOTAL_DVXF); -- importe de vacaciones por fuera
				_IAV := ((case when _CalculoMixto = '0' then _SalarioDiario else _SalarioMixto end) * _TOTAL_DAV); -- importe a cuenta de vacaciones
				_IVD := ((case when _CalculoMixto = '0' then _SalarioDiario else _SalarioMixto end) * (_TOTAL_DVD * _FAC_DVD)); -- importe de vacaciones disfrutadas por el factor			
				
				IF _IV is null THEN _IV := 0; END IF;
				IF _IVP is null THEN _IVP := 0; END IF;
				IF _IVXF is null THEN _IVXF := 0; END IF;
				IF _IAV is null THEN _IAV := 0; END IF;
				IF _IVD is null THEN _IVD := 0; END IF;
			
				-- Ahora calcula la prima vacacional
				IF _TOTAL_PV <> 0
				THEN
					_ImportePrimaVac := ((case when _CalculoMixto = '0' then _SalarioDiario else _SalarioMixto end) * _TOTAL_PV * _PrimaVac);
					_IPVE := case when ROUND((_SalarioMinimo * 15),2) < _ImportePrimaVac then ROUND((_SalarioMinimo * 15),2) else _ImportePrimaVac end;
					_IPVG := case when _IPVE < _ImportePrimaVac then _ImportePrimaVac - _IPVE else 0.00 end;							
				ELSE
					_IPVE := 0.00;
					_IPVG := 0.00;
				END IF;

				IF _IPVE is null THEN _IPVE := 0; END IF;
				IF _IPVG is null THEN _IPVG := 0; END IF;
				--//////////////////////// FIN CALCULO DE VACACIONES //////////////////////////////////
				
				--//////////////////////////////////////////////////////////////////////////////////////////////////
				--////////////////////////// AHORA CALCULA LOS DIAS TRABAJADOS ///////////////////////////////////////
				-- La variable PromSueldoTurno, DiasNominaTurn, DiasNominaDiff, se calculan en base al turno, y se refiere al costo del dia habil dividido entre los dias de la semana
				_PromSueldoTurno := ( 	select (( HNALunes + HNAMartes + HNAMiercoles + HNAJueves + HNAViernes + HNASabado + HNADomingo ) /
																				   ((case when ELunes is null then 0 else 1 end) +
																						(case when EMartes is null then 0 else 1 end) +
																						(case when EMiercoles is null then 0 else 1 end) +
																						(case when EJueves is null then 0 else 1 end) +
																						(case when EViernes is null then 0 else 1 end) +
																						(case when ESabado is null then 0 else 1 end) +
																						(case when EDomingo is null then 0 else 1 end)) / 8 )
																		from TBL_NOM_TURNOS 
																		where ID_Turno = _Turno );
				_DiasNominaSal := _DiasNomina; -- Establecemos los dias de nomina flotantes para poder calcular salario
				_DiasNominaTurn := FLOOR(_DiasNominaSal / 7);
				_DiasNominaDiff := (_DiasNominaSal / 7) - _DiasNominaTurn;
				-- La variable _DT ( dias trabajados ), se calcula sumando los dias de nomina menos las faltas, las incapacidades, y las vacaciones disfrutadas
				IF _DiasNomina = _DiasNominaNom -- si estaba dado de alta durante todo el periodo de esta nomina
				THEN
					_DT := ( _DiasNomina - _TOTAL_FLT - _TOTAL_IXM - _TOTAL_IXE - _TOTAL_IXA - _TOTAL_DVD );
				ELSE -- de lo contrario es un empleado que acaba de entrar o esta dado de baja antes que se generara la nomina
					_DT := ROUND( (((_DiasNominaTurn * 7) + (_PromSueldoTurno * _DiasNominaDiff * 7)) - _TOTAL_FLT - _TOTAL_IXM - _TOTAL_IXE - _TOTAL_IXA - _TOTAL_DVD), 2);
				END IF;
				
				IF _DT is null THEN _DT := 0; END IF;
				
				-- ////////////////////////////// fin calculo de dias trabajados ///////////////////////////////////////////////
				--///////////// REVISADO HASTA AQUI OK		
				
				-- ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				-- //////////////////    CALCULA HORAS EXTRAS EN TODAS SUS MODALIDADES  ////////////////////////////////////////
				_Aplica_horas_Extras := _EMP.Aplica_horas_Extras;
				
				IF _EMP.Aplica_horas_Extras = '1'
				THEN
					SELECT * INTO _TOTAL_HE, _TOTAL_HF, _TOTAL_HD, _TOTAL_HT, _DiasHorasExtras
					FROM sp_nom_calculo_nomina_horas_extras( _Tipo, _ID_Empleado, _Fecha_Desde, _Fecha_Hasta, _HEPF, _MaxHE)
					AS
						(
							TOTAL_HE decimal(6,2) , TOTAL_HF decimal(6,2), TOTAL_HD decimal(6,2), TOTAL_HT decimal(6,2), DiasHorasExtras smallint
						);
				ELSE
					_TOTAL_HE := 0.00;
					_TOTAL_HF := 0.00;
					_TOTAL_HD := 0.00;
					_TOTAL_HT := 0.00;
					_DiasHorasExtras := 0;
				END IF;
			
				IF (_TOTAL_HE + _TOTAL_HT) > 0.00 -- CALCULA HORAS EXTRAS DOBLES Y TRIPLES POR DENTRO
				THEN
					_ImporteHorasExtras := ((_SalarioPorHora * _TOTAL_HE) * 2) + ((_SalarioPorHora * _TOTAL_HT) * 3);
					if(_SalarioDiario <= _SalarioMinimo)
					then
						_IHEE := _ImporteHorasExtras;
						_IHEG := 0.00;
					else
						_IHEE := case when ROUND(_SalarioMinimo * _MHEE * (_DiasNomina/7),2) < _ImporteHorasExtras 
								 then ROUND(_SalarioMinimo * _MHEE * (_DiasNomina/7),2) else _ImporteHorasExtras end;
						_IHEG := case when _IHEE < _ImporteHorasExtras then _ImporteHorasExtras - _IHEE else 0.00 end;							
					end if;
				ELSE
					_IHEE := 0.00;
					_IHEG := 0.00;
				END IF;

				IF _IHEE is null THEN	_IHEE := 0; END IF;
				IF _IHEG is null THEN _IHEG := 0; END IF;

				IF _TOTAL_HF <> 0.00 -- CALCULA HORAS EXTRAS POR FUERA
				THEN
					_IHE := (_SalarioPorHora * _TOTAL_HF) * 2;
				ELSE
					_IHE := 0.00;
				END IF;

				IF _IHE is null THEN _IHE := 0; END IF;

				IF _TOTAL_HD <> 0.00 -- CALCULA HORAS EXTRAS DOBLES DEL DOMINGO POR DENTRO
				THEN
					_ImporteHorasExtras := (_SalarioPorHora * _TOTAL_HD) * 2;
					if(_SalarioDiario <= _SalarioMinimo)
					then
						_IHDE := _ImporteHorasExtras;
						_IHDG := 0.00;
					else
						_IHDE := case when ROUND(_SalarioMinimo * _MHEE * (_DiasNomina/7),2) < _ImporteHorasExtras 
									then ROUND(_SalarioMinimo * _MHEE * (_DiasNomina/7),2) else _ImporteHorasExtras end;
						_IHDG := case when _IHDE < _ImporteHorasExtras then _ImporteHorasExtras - _IHDE else 0.00 end;
					end if;
					_PrimaDominical := _ImporteHorasExtras * _PRIDOM;
					if(_SalarioDiario <= _SalarioMinimo)
					then
						_PDE := _PrimaDominical;
						_PDG := 0.00;
					else
						_PDE := case when ROUND(_SalarioMinimo * (_DiasNomina/7),2) < _PrimaDominical 
									then ROUND(_SalarioMinimo * (_DiasNomina/7),2) else _PrimaDominical end;
						_PDG := case when _PDE < _PrimaDominical then _PrimaDominical - _PDE else 0.00 end;	
					end if;						
				ELSE
					_IHDE := 0.00;
					_IHDG := 0.00;
					_PDE := 0.00;
					_PDG := 0.00;
				END IF;

				IF _IHDE is null THEN _IHDE := 0; END IF;
				IF _IHDG is null THEN _IHDG := 0; END IF;
				IF _PDE is null THEN _PDE := 0; END IF;
				IF _PDG is null THEN _PDG := 0; END IF;
				
				-- ///////////////////////////////  FIN DE HORAS EXTRAS  ////////////////////////////////////////////


				--//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				-- //////////// AQUI INSERTA EL CABECERO DEL EMPLEADO EN LA NOMINA NORMAL, ESPECIAL Y VALES ////////////////////////
				INSERT INTO _TMP_CALCULO_NOMINA_ESP
				VALUES(_ID_Nomina, _ID_Empleado, _DT, (_TOTAL_FLT + _TOTAL_IXM + _TOTAL_IXE + _TOTAL_IXA + _TOTAL_DVD), _contNum, _TOTAL_HE, _TOTAL_HD,
							null,null,0.0,0.0,0.0,0.0,(_TOTAL_HT + _TOTAL_HD),_TOTAL_IXA,_TOTAL_IXE,_TOTAL_IXM, _DiasHorasExtras);
				
				INSERT INTO _TMP_CALCULO_NOMINA_ESP
				VALUES(-1, _ID_Empleado, _DT, (_TOTAL_FLT + _TOTAL_IXM + _TOTAL_IXE + _TOTAL_IXA + _TOTAL_DVD), _contNum, _TOTAL_HF, 0.0,
							null,null,0.0,0.0,0.0,0.0,0.0,0,0,0,0);

				IF (select VEntero from TBL_VARIABLES where ID_Variable = 'NOMVALS') = 1 and _CalVales = '1'
				THEN
					INSERT INTO _TMP_CALCULO_NOMINA_ESP
					VALUES(-2, _ID_Empleado, _DT, (_TOTAL_FLT + _TOTAL_IXM + _TOTAL_IXE + _TOTAL_IXA + _TOTAL_DVD), _contNum, 0.0, 0.0,
							null,null,0.0,0.0,0.0,0.0,0.0,0,0,0,0);
				END IF;
			END IF; -- _TipoAg = 0
			-- ///////////////////////////////////////////////////////////////////////////////////////////////
			-- ///////////////////////   AHORA CALCULA LOS MOVIMIENTOS DINMICOS  /////////////////////////////
			-- Simplemente llama al porcedimiento externo porque este se encarga de meter los resultados en la tabla temporal de movimientos dinmicos
			IF _TipoAg = 0
			THEN
				-- Empieza por los de nomina normal
				EXECUTE sp_nom_calculo_nomina_dinamicos( _ID_Empleado, _ID_Compania, _ID_Sucursal, _Ano, _Numero_Nomina, '12', 
																								_NivelConfianza, _SalarioPorHora, _SalarioDiario, _SalarioMixto, _CalculoMixto);
				-- Ahora por los de nomina especial
				EXECUTE  sp_nom_calculo_nomina_dinamicos( _ID_Empleado, _ID_Compania, _ID_Sucursal, _Ano, _Numero_Nomina, '34', 
																								_NivelConfianza, _SalarioPorHora, _SalarioDiario, _SalarioMixto, _CalculoMixto);
				-- Ahora por los de nomina de vales
				IF (select VEntero from TBL_VARIABLES where ID_Variable = 'NOMVALS') = 1 and _CalVales = '1'
				THEN
					EXECUTE sp_nom_calculo_nomina_dinamicos( _ID_Empleado, _ID_Compania, _ID_Sucursal, _Ano, _Numero_Nomina, '78', 
																								_NivelConfianza, _SalarioPorHora, _SalarioDiario, _SalarioMixto, _CalculoMixto);
				END IF;
			ELSE -- Es nomina de aguinaldos
				EXECUTE sp_nom_calculo_nomina_dinamicos( _ID_Empleado, _ID_Compania, _ID_Sucursal, _Ano, _Numero_Nomina, '56', 
																								_NivelConfianza, _SalarioPorHora, _SalarioDiario, _SalarioMixto, _CalculoMixto);
			END IF;
			--///////////////////////// fin calculo movimientos diánmicos /////////////////////////////////////////////////////////////////////

			IF _TipoAg = 0
			THEN
				--//////////////////////////////////////////////////////////////////////////////////////////////////
				-- AHORA CALCULA EL SUELDO DE ESTE EMPLEADO MENOS VACACIONES SI ES QUE LAS TUVO ////////////////////
				IF _DiasNomina = _DiasNominaNom -- si estaba dado de alta durante todo el periodo de esta nomina
				THEN
					_SAL := ROUND(_SalarioDiario * _DiasNomina,2) - _IV - _IAV;
				ELSE -- de lo contrario es un empleado que acaba de entrar o esta dado de baja antes que se generara la nomina
					_SAL = 	ROUND( ((_DiasNominaTurn * 7) + (_PromSueldoTurno * _DiasNominaDiff * 7)) * _SalarioDiario,  2 ) - _IV - _IAV;
				END IF;
				
				IF _SAL is null THEN _SAL := 0.0; END IF;
				--/////////////////////////////////// fin calculo de sueldo ////////////////////////////////////
						
				-- /////////////////////////////////////////////////////////////////////////////////////////////
				-- ////////////////////////////////   AHORA CALCULA EL IMSS  //////////////////////////////////
				_IMSS := (SELECT case 	when (	select SUM(Cuota_Trabajador) 
												from TBL_NOM_IMSS) is null 
										then 0.00
										else 	case 	when _SalarioIntegrado < _SalarioTope 
														then ( round( (	select SUM(Cuota_Trabajador) 
																		from TBL_NOM_IMSS ) * ( _SalarioIntegrado * _DT), 2 ) )
														else	( round( (	select SUM(Cuota_Trabajador) -- cuando el sueldo es mayor al tope, entonces se calcula en base al tope 
																			from TBL_NOM_IMSS ) *	( _SalarioTope * _DT), 2 ) )
												end
								end );
				IF( _SalarioIntegrado > (_SalarioMinimo * 3))
				THEN
					_IMSS := _IMSS + ((_SalarioIntegrado - (_SalarioMinimo * 3)) * _COMRETIMSS * _DT);
				END IF;
				
				IF _IMSS is null THEN _IMSS := 0; END IF;
			--/////////////////////////////////// fin del imss //////////////////////////////////////
			END IF;
			--/////////////////////////////////////////////////////////////////////////////////////////////
			--////////////////////////////////////////// AHORA CALCULA EL ISPT ////////////////////////////
			_isptFlag := 0;
			_DA := (	select sum(e.Dias + e.Faltas) 
					from TBL_NOM_CALCULO_NOMINA_ESP e join TBL_NOM_CALCULO_NOMINA n on
							e.ID_Nomina = n.ID_Nomina
					where e.ID_Empleado = _ID_Empleado and
								n.Ano = _Ano and n.Tipo = _Tipo and 
										ID_Sucursal = _ID_Sucursal and 
										ID_Compania = _ID_Compania and 
										Numero_Nomina < _Numero_Nomina );
			IF _TipoAg = 0
			THEN
				_DAA := ( case 	when _DA is null 
								then _DT +  (_TOTAL_FLT + _TOTAL_IXM + _TOTAL_IXE + _TOTAL_IXA + _TOTAL_DVD)
								else _DA + _DiasNomina end );
				_SA := ( 	select SUM(Gravado) + SUM(Deduccion) -- SUELDOS ACUMULADOS SIN CONTAR EL DE ESTA NOMINA
						from TBL_NOM_CALCULO_NOMINA_DET cnd JOIN TBL_NOM_CALCULO_NOMINA cn ON
							cnd.ID_Nomina = cn.ID_Nomina
						where  (Tipo = _Tipo or Tipo = _TipoAg or Tipo = _TipoV) and Ano = _Ano and Numero_Nomina < _Numero_Nomina and
							ID_Empleado = _ID_Empleado and ( 
							cnd.ID_Movimiento = _M_IAG or
							cnd.ID_Movimiento = _M_SAL or
							cnd.ID_Movimiento = _M_IDV or
							cnd.ID_Movimiento = _M_DAP or
							cnd.ID_Movimiento = _M_DPI or
							cnd.ID_Movimiento = _M_DSP or
							cnd.ID_Movimiento = _M_FLT or
							cnd.ID_Movimiento = _M_IXM or
							cnd.ID_Movimiento = _M_IXE or
							cnd.ID_Movimiento = _M_IXA or
							cnd.ID_Movimiento = _M_IDVD ));
				_SAA := case when _SA is not null then 
							_SA + ((_SAL + _IV) - (_IMP_DAP + _IMPPORINCON + _IMP_DSP + _FLT + _IIXM + _IIXE + _IIXA + _IVD))
						else
							(_SAL + _IV) - (_IMP_DAP + _IMPPORINCON + _IMP_DSP + _FLT + _IIXM + _IIXE + _IIXA + _IVD)
						end;
				_GA := (	select SUM(Gravado) -- PERCEPCIONES ACUMULADAS (SIN CONTAR EL SUELDO) SIN CONTAR EL DE ESTA NOMINA
						from TBL_NOM_CALCULO_NOMINA_DET cnd JOIN TBL_NOM_CALCULO_NOMINA cn ON
								cnd.ID_Nomina = cn.ID_Nomina
						where  ( Tipo = _Tipo or Tipo = _TipoAg or Tipo = _TipoV ) and Ano = _Ano and Numero_Nomina < _Numero_Nomina and
								ID_Empleado = _ID_Empleado and 
								cnd.ID_Movimiento <> _M_IAG  and 
								cnd.ID_Movimiento <> _M_SAL  and 
								cnd.ID_Movimiento <> _M_IDV );
				_GAA = case when  (	SELECT sum(TOTAL_GRAVADO)
									FROM _TMP_PLANTILLAS_TOTALES 
									WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo <> '34' ) is null 
						then 	case 	when _GA is not null 
										then _GA + _IHEG + _IHDG + _PDG + _IAV + _IVP + _IPVG
										else _IHEG + _IHDG + _PDG + _IAV + _IVP + _IPVG  
								end
						else 	case 	when _GA is not null 
										then _GA + _IHEG + _IHDG + _PDG + _IAV + _IVP + _IPVG +
											(	SELECT sum(TOTAL_GRAVADO)
												FROM _TMP_PLANTILLAS_TOTALES 
												WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo <> '34' )
										else _IHEG + _IHDG + _PDG + _IAV + _IVP + _IPVG + 
											(	SELECT sum(TOTAL_GRAVADO)
												FROM _TMP_PLANTILLAS_TOTALES 
												WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo <> '34' ) 
								end
					end;
				_FI := (_SAA + _GAA) / _DAA; -- ES LA BASE1; EL IMPORTE ACUMULADO GRAVADO A FECHA DE NOMINA
				_RANGO := ( 	select ID_ISR 
								from TBL_NOM_ISR_ANUALIZADO
								where Limite_Inferior <= _FI and Limite_Superior >= _FI limit 1 );
				_LI := ( 	select Limite_Inferior 
						from TBL_NOM_ISR_ANUALIZADO
						where ID_ISR = _RANGO );					
				_Exedente := ((_SAA + _GAA) - (_LI * _DAA));
				_IME := ( _Exedente * ( 	select Porcentaje_Exd -- IMPUESTO MARGINAL EXEDENTE (IMPTEX)
										from TBL_NOM_ISR_ANUALIZADO
										where ID_ISR = _RANGO ));
				_CF := ( 	select Cuota_Fija * _DAA
						from TBL_NOM_ISR_ANUALIZADO
						where ID_ISR = _RANGO );
				_CFDTGAA := _IME + _CF;
				_AIM := case when ( 	select SUM(Deduccion) 
										from TBL_NOM_CALCULO_NOMINA_DET cnd JOIN TBL_NOM_CALCULO_NOMINA cn ON
											cnd.ID_Nomina = cn.ID_Nomina
										where  (Tipo = _Tipo or Tipo = _TipoAg or Tipo = _TipoV) and Ano = _Ano and Numero_Nomina < _Numero_Nomina and
																	ID_Empleado = _ID_Empleado and cnd.ID_Movimiento = _M_ISPT ) is null 
							then 0.0000
						else ( 	select SUM(Deduccion) 
								from TBL_NOM_CALCULO_NOMINA_DET cnd JOIN TBL_NOM_CALCULO_NOMINA cn ON
									cnd.ID_Nomina = cn.ID_Nomina
								where  (Tipo = _Tipo or Tipo = _TipoAg or Tipo = _TipoV) and Ano = _Ano and Numero_Nomina < _Numero_Nomina and
																ID_Empleado = _ID_Empleado and cnd.ID_Movimiento = _M_ISPT ) 
						end;
			ELSE --Impuesto de nominas de aguinaldo
				_DAA := _DA;
				_SA := ( 	select SUM(Gravado) +  SUM(Deduccion) -- SUELDOS ACUMULADOS SIN CONTAR EL DE ESTA NOMINA
						from TBL_NOM_CALCULO_NOMINA_DET cnd INNER JOIN TBL_NOM_CALCULO_NOMINA cn ON
							cnd.ID_Nomina = cn.ID_Nomina
						where  Tipo = _Tipo and Ano = _Ano and Numero_Nomina < _Numero_Nomina and 
							ID_Empleado = _ID_Empleado and ( 
							cnd.ID_Movimiento = _M_SAL or
							cnd.ID_Movimiento = _M_IDV or
							cnd.ID_Movimiento = _M_DAP or
							cnd.ID_Movimiento = _M_DPI or
							cnd.ID_Movimiento = _M_DSP or
							cnd.ID_Movimiento = _M_FLT or
							cnd.ID_Movimiento = _M_IXM or
							cnd.ID_Movimiento = _M_IXE or
							cnd.ID_Movimiento = _M_IXA or
							cnd.ID_Movimiento = _M_IDVD ));
							
				_SAA := case when _SA is not null then 
								_SA + _IAG 
							else 
								_IAG 
							end;
				_GA := (	select SUM(Gravado) -- SUELDOS ACUMULADOS SIN CONTAR EL DE ESTA NOMINA
						from TBL_NOM_CALCULO_NOMINA_DET cnd INNER JOIN TBL_NOM_CALCULO_NOMINA cn ON
							cnd.ID_Nomina = cn.ID_Nomina
						where  ( Tipo = _Tipo ) and Ano = _Ano and Numero_Nomina < _Numero_Nomina and
							ID_Empleado = _ID_Empleado and 
							cnd.ID_Movimiento <> _M_SAL  and 
							cnd.ID_Movimiento <> _M_IDV  );
				
				_GAA := case when  (	SELECT sum(TOTAL_GRAVADO)
										FROM _TMP_PLANTILLAS_TOTALES 
										WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo = '56' ) is null 
							then 	case 	when _GA is not null 
											then _GA 
											else 0.00 
									end
							else 	case 	when _GA is not null 
											then _GA +  
													(	SELECT sum(TOTAL_GRAVADO)
														FROM _TMP_PLANTILLAS_TOTALES 
														WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo = '56' )
											else  
													(	SELECT sum(TOTAL_GRAVADO)
														FROM _TMP_PLANTILLAS_TOTALES 
														WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo = '56' ) 
									end
					 	end;
				
				_FI := (_SAA + _GAA) / _DAA; -- ES LA BASE1; EL IMPORTE ACUMULADO GRAVADO A FECHA DE NOMINA
				_RANGO := ( 	select ID_ISR 
								from TBL_NOM_ISR_ANUALIZADO
								where Limite_Inferior <= _FI and Limite_Superior >= _FI
								limit 1 	);
				_LI := ( 	select Limite_Inferior 
						from TBL_NOM_ISR_ANUALIZADO
						where ID_ISR = _RANGO );
				_Exedente := ((_SAA + _GAA) - (_LI * _DAA));
				_IME := ( _Exedente * ( 	select Porcentaje_Exd -- IMPUESTO MARGINAL EXEDENTE (IMPTEX)
							 			from TBL_NOM_ISR_ANUALIZADO
							 			where ID_ISR = _RANGO ));
				_CF := (	select Cuota_Fija * _DAA
						from TBL_NOM_ISR_ANUALIZADO
						where ID_ISR = _RANGO );
				_CFDTGAA := _IME + _CF;
				_AIM := case 	when ( 	select SUM(Deduccion) 
										from TBL_NOM_CALCULO_NOMINA_DET cnd INNER JOIN TBL_NOM_CALCULO_NOMINA cn ON
											cnd.ID_Nomina = cn.ID_Nomina
										where  (Tipo = _Tipo) and Ano = _Ano and Numero_Nomina < _Numero_Nomina and
																						ID_Empleado = _ID_Empleado and cnd.ID_Movimiento = _M_ISPT ) is null 
								then 
									0.0000
								else ( 	select SUM(Deduccion) 
										from TBL_NOM_CALCULO_NOMINA_DET cnd INNER JOIN TBL_NOM_CALCULO_NOMINA cn ON
											cnd.ID_Nomina = cn.ID_Nomina
										where  (Tipo = _Tipo) and Ano = _Ano and Numero_Nomina < _Numero_Nomina and
																						ID_Empleado = _ID_Empleado and cnd.ID_Movimiento = _M_ISPT ) 
						end;
			END IF;

			_ISPT := ROUND((_CFDTGAA + _AIM),2); 
				
			IF _ISPT is null THEN _ISPT := 0.00; END IF;
				
			IF -_ISPT > 0.00
			THEN
				_ISPT := ROUND(_ISPT * (select VDecimal from TBL_VARIABLES where ID_Variable = 'VARISPT'),2);
			ELSE
				IF _EMP.CalculoSimplificado = '1'
				THEN
					_ISPT := ROUND(_ISPT * _EMP.PCS ,2);
				END IF;
			END IF;
				
			IF _ISPSB is null THEN _ISPSB := 0.00; END IF;

			--raise notice 'FIN ISPT y Subsidio';

			-- inserta en la tabla de historial de impuestos. Por ejemplo como el de arriba
			INSERT INTO _TMP_CALCULO_NOMINA_HIS_DET_IMP
			VALUES(_ID_Nomina,_ID_Empleado,coalesce(_IMP_DAP,0),coalesce(_IMP_DSP,0),_DiasNomina,_DT,_SA,_SAA,_DA,_DAA,_IMPAN,_GA,_GAA,_FI,_LI,_SalarioTope,_Exedente,_CF,_CFDTGAA,_CFDT,_IME,_IM,_AIM,_ISPT,_Subsidio,_CFS,_SubTot,_SubTotGAA,_ISPSB);
			--///////////////////////////////////////////////////////////

			IF _TipoAg = 0
			THEN
				-- Calcula el Credito al salario
				-- pRIMERO CHECA QUE NO ESTE DE INCAPACIDAD EN ESTA SEMANA//////////////	
					
				IF(date_part('Month',_Fecha_Ingreso) = _Mes AND date_part('Year',_Fecha_Ingreso) = _Ano) -- si es de nuevo ingreso en el mes solo calcula los dias trabajados del mes
				THEN
					_DTM := getfechadiff('day', _Fecha_Ingreso, _Fecha_Hasta) + 1;
				ELSE
					_DTM := _DiasNomina + _DiasNominaM;
				END IF;
				
				IF _DTM > 30.4 THEN _DTM := 30.4; END IF;

				-- Calcula los gravados totales de las semanas anteriores de este mes y luego le adiciona los de esta semana
				_INGGET = (		select SUM(Gravado) 
								from TBL_NOM_CALCULO_NOMINA_DET cnd JOIN TBL_NOM_CALCULO_NOMINA cn ON
									cnd.ID_Nomina = cn.ID_Nomina
								where  ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and
									Tipo = _Tipo and Ano = _Ano and Mes = _Mes and Numero_Nomina < _Numero_Nomina and
										ID_Empleado = _ID_Empleado and cnd.ID_Movimiento <> _M_AAP and cnd.ID_Movimiento <> _M_ASP ); 
					
				IF _INGGET is null THEN _INGGET := 0.0; END IF;
				
				_INGGET := _INGGET + ( 	case 	when  (	SELECT sum(TOTAL_GRAVADO) 
														FROM _TMP_PLANTILLAS_TOTALES 
														WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo <> '34' ) is null 
												then _Sueldo + _IHEG + _IHDG + _PDG + _IV + _IAV + _IVP + _IPVG
												else _Sueldo + _IHEG + _IHDG + _PDG + _IV + _IAV + _IVP + _IPVG + 
																				(	SELECT sum(TOTAL_GRAVADO)  
																					FROM _TMP_PLANTILLAS_TOTALES 
																					WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo <> '34' )
										end );

				-- Ahora Calcula los gravados  y deducciones de las semanas anteriores de este mes y luego los de esta semana
				_INGM := (	select SUM(Gravado) + SUM(Deduccion) -- SUELDOS ACUMULADOS SIN CONTAR EL CREDITO AL SALARIO
							from TBL_NOM_CALCULO_NOMINA_DET cnd JOIN TBL_NOM_CALCULO_NOMINA cn ON
								cnd.ID_Nomina = cn.ID_Nomina
							where  ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and
								Tipo = _Tipo and Ano = _Ano and Mes = _Mes and Numero_Nomina < _Numero_Nomina and
															ID_Empleado = _ID_Empleado and cnd.ID_Movimiento <> _M_CRDFS and
															cnd.ID_Movimiento <> _M_AAP and cnd.ID_Movimiento <> _M_ASP and
															((cnd.Gravado > 0) or (cnd.Deduccion <> 0 and ( cnd.ID_Movimiento = _M_FLT
																														or	cnd.ID_Movimiento	= _M_IDVD
																														or cnd.ID_Movimiento	= _M_IXM
																														or cnd.ID_Movimiento	= _M_IXE
																														or cnd.ID_Movimiento	= _M_IXA
																														or cnd.ID_Movimiento	= _M_DPI
																														or cnd.ID_Movimiento	= _M_DAP
																														or cnd.ID_Movimiento  = _M_DSP))) ); 
				IF _INGM is null THEN _INGM := 0.0; END IF;
				
				_ING := 	case when ( SELECT sum(TOTAL_GRAVADO)
									FROM _TMP_PLANTILLAS_TOTALES 
									WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo <> '34' ) is null 
							then _IHEG + _IHDG + _PDG + _IV + _IAV + _IVP + _IPVG - _FLT - _IIXM - _IIXE - _IIXA - _IVD - _IMPPORINCON - _IMP_DAP - _IMP_DSP
							else _IHEG + _IHDG + _PDG + _IV + _IAV + _IVP + _IPVG - _FLT - _IIXM - _IIXE - _IIXA - _IVD - _IMPPORINCON - _IMP_DAP - _IMP_DSP +
									(	SELECT sum(TOTAL_GRAVADO) 
										FROM _TMP_PLANTILLAS_TOTALES 
										WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo <> '34' )
						end;
					
				_RANGO := (	select ID_CS
								from TBL_NOM_CREDITO_SALARIO
								where ((Ingresos_Desde / 30.4) * _DTM) <= (_Sueldo + _ING + _INGM)
										and ((Ingresos_Hasta / 30.4) * _DTM) >= (_Sueldo + _ING + _INGM) limit 1 );
				_CSDiario := ( 	select (CSM / 30.4)
								from TBL_NOM_CREDITO_SALARIO
								where ID_CS = _RANGO );
				_PRVCF := (	select case when sum(Deduccion) is null 
										then (_DTM * _CSDiario)
										else (_DTM * _CSDiario) - sum(Deduccion) 
									end 
							from TBL_NOM_CALCULO_NOMINA_DET cnd JOIN TBL_NOM_CALCULO_NOMINA cn ON
									cnd.ID_Nomina = cn.ID_Nomina
							where  ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and
								Tipo = _Tipo and Ano = _Ano and Mes = _Mes and Numero_Nomina < _Numero_Nomina and
															ID_Empleado = _ID_Empleado and cnd.ID_Movimiento = _M_CRDFS ); 
					
				IF _PRVCF is null THEN _PRVCF := 0.00; END IF;

				_CRDFS := ( case when _INGGET = 0 
								then 0 else round( ((_Sueldo + _ING + _INGM)/_INGGET) * _PRVCF, 2) 
							end );

				IF _CRDFS is null THEN _CRDFS = 0.00; END IF;

				--select _Mes as MES, _Ano as ANO, _DTM as DTM, _DiasNominaM as DM, _ID_Empleado as Clave, _INGM as INGM, _ING as ING, _Sueldo as Sueldo, _Sueldo + _ING + _INGM as Neto, _INGGET as INGGET, _RANGO as RANGO, _CSDiario as CSDiario, _PRVCF as PRVCF, _CRDFS as CRDFS

				raise notice 'FIN CREDITO al SALARIO';
				
				-- ///////////////////////////////////////////////////////////////////////
				-- AHORA CALCULA EL INFONAVIT, FONACOT Y VALES
				IF _CalVales = '1'
				THEN
					IF _EMP.Ayuda_Vales_de_Despensa = '1'
					THEN
						_VALS = _EMP.Importe_Vales_de_Despensa;
					ELSE
						_VALS = 0.0;
					END IF;
				ELSE
					_VALS = 0.0;
				END IF;
			
				IF _EMP.Fecha_Alta_Infonavit is not null
						AND _EMP.Fecha_Liquidacion_Infonavit is null
				THEN
					IF _EMP.Porcentaje_Descuento <> 0.00 -- Primera Opcion
					THEN
						_INFON := ROUND( (_SalarioIntegrado * _DT * _EMP.Porcentaje_Descuento),2);
					ELSE
						_INFON := ROUND( (_SalarioMinimo * (_EMP.Descuento_VSM / 30) * _DT),2);
					END IF;
				ELSE
					_INFON := 0.00;
				END IF;
					
				--select _INFON, _SalarioIntegrado, _DT, Porcentaje_Descuento from TBL_MASEMP where ID_Empleado = _ID_Empleado
				-- ejecuta el calculo de fonacot en el procedimiento de fonacot				
				IF _EMP.Prestamo_Fonacot > 0
				THEN
					SELECT * INTO _FONAC
					FROM sp_nom_calculo_nomina_fonacot(_ID_Empleado, _Fecha_Hasta, _DiasNomina, _FonAJ) as ( FONAC numeric(19,4));
				ELSE
					_FONAC := 0.0;
				END IF;

				IF _FONAC is null THEN _FONAC := 0.0; END IF;
		
				-- Calcula la cuota sindical en base a todas las percepciones y algunas deducciones
				_CUOTA_SINDICAL :=	case 	when _NivelConfianza = '0'
												then 0.00
												else 
													case 	when  (	SELECT sum(TOTAL_GRAVADO) + sum(TOTAL_EXENTO)
																	FROM _TMP_PLANTILLAS_TOTALES 
																	WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo <> '34' ) is null 
															then 
																(( _SAL + _IHEE + _IHEG + _IHDE + _IHDG + _PDE + _PDG + /*_IHE + */ _IV + /*_IVXF*/ + _IAV + _IVP + _IPVE + _IPVG ) - 
																( _IMP_DAP + _IMP_DSP + _IMPPORINCON + _FLT + _IIXM + _IIXE + _IIXA + _IVD )) * 
																										(	select VDecimal
																											from TBL_VARIABLES
																											where ID_Variable = 'CSIN' )
															else 
																(   ((SELECT sum(TOTAL_GRAVADO)  + sum(TOTAL_EXENTO)
																	FROM _TMP_PLANTILLAS_TOTALES 
																	WHERE ID_Empleado = _ID_Empleado and ISPT = '1' and Tipo <> '34' ) + 
																( _SAL + _IHEE + _IHEG + _IHDE + _IHDG + _PDE + _PDG + /*_IHE + */ _IV + /*_IVXF*/ + _IAV + _IVP + _IPVE + _IPVG )) - 
																( _IMP_DAP + _IMP_DSP + _IMPPORINCON + _FLT + _IIXM + _IIXE + _IIXA + _IVD ) ) *
																										(	select VDecimal
																											from TBL_VARIABLES
																											where ID_Variable = 'CSIN' )
													end
										end;
				IF _CUOTA_SINDICAL is null
				THEN
					_CUOTA_SINDICAL := 0.00;
				ELSE
					_CUOTA_SINDICAL := round(_CUOTA_SINDICAL,2);
				END IF;

				-- Trabaja con el ahorro de compenzacion anual ( Utiles Escolares )
				_COMPENSACION_ANUAL :=	
						case	when _CompensacionAnual = 0
								then 0.00
								else 
									case 	when 	_EMP.CompensacionAnualFija = 0.00
											then _Sueldo *	_ACA
											else	_EMP.CompensacionAnualFija
									end
						end;

				IF _COMPENSACION_ANUAL is null
				THEN
					_COMPENSACION_ANUAL := 0.00;
				ELSE
					_COMPENSACION_ANUAL := round(_COMPENSACION_ANUAL,2);
				END IF;

			END IF;
			-- ///////////////////////////////////////////////////////////////////////////////////////
			-- AHORA ACTUALIZA LA TABLA DE ANALISIS
			IF _TipoAg = 0
			THEN
				INSERT INTO _TMP_ANALISIS(ID_Empleado,Salario_Diario,ISPT,ISPSB,IAE,IAG,CRDFS,IHEE,IHEG,IHE,			IHDE,IHDG,PDE,PDG,IDV,IDVXF,IDAV,IDVP,IDVD,IPVE,IPVG,SAL,FLT,			IMSS,INFON,FONAC,VALS,IXM,IXE,IXA,DAP,                                                   DPI,CSIN,ACA,DSP)
				VALUES(_EMP.ID_Empleado, _EMP.Salario_Diario, _ISPT, _ISPSB, 0.00, 0.00, _CRDFS, _IHEE, _IHEG, _IHE, _IHDE, _IHDG, _PDE, _PDG, _IV, _IVXF, _IAV, _IVP, _IVD, _IPVE, _IPVG, _SAL, _FLT, _IMSS, _INFON, _FONAC, _VALS, _IIXM, _IIXE, _IIXA, _IMP_DAP, _IMPPORINCON, _CUOTA_SINDICAL, _COMPENSACION_ANUAL, _IMP_DSP);
			ELSE
				INSERT INTO _TMP_ANALISIS(ID_Empleado,Salario_Diario,ISPT,ISPSB,IAE,IAG,CRDFS,IHEE,IHEG,IHE,			IHDE,IHDG,PDE,PDG,IDV,IDVXF,IDAV,IDVP,IDVD,IPVE,IPVG,SAL,FLT,			IMSS,INFON,FONAC,VALS,IXM,IXE,IXA,DAP,                                                   DPI,CSIN,ACA,DSP)
				VALUES(_EMP.ID_Empleado, _EMP.Salario_Diario, _ISPT, _ISPSB, _IAE, _IAG, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00);
			END IF;
			
			_contNum := _contNum + 1;
			
		END LOOP;
		-- ///////////////////////////////////////////////////////////////////////////////////////////////////////
		-- //////////////// AHORA INGRESA LOS RESULTADOS DE ESTA NOMINA EN LA TABLA TEMPORAL //////////////////////
		-- ///////////////////////////////////////////////////////////////////////////////////////////////////////
		IF _TipoAg = 0
		THEN
			-- comienza por las deducciones
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_DSP, 0.00, 0.00, -DSP 
			FROM _TMP_ANALISIS
			WHERE DSP <> 0.00; 
		
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_DAP, 0.00, 0.00, -DAP 
			FROM _TMP_ANALISIS
			WHERE DAP <> 0.00;
		
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_DPI, 0.00, 0.00, -DPI 
			FROM _TMP_ANALISIS
			WHERE DPI <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_FLT, 0.00, 0.00, -FLT 
			FROM _TMP_ANALISIS
			WHERE FLT <> 0.00; 
			
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IXM, 0.00, 0.00, -IXM 
			FROM _TMP_ANALISIS
			WHERE IXM <> 0.00; 
			
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IXE, 0.00, 0.00, -IXE 
			FROM _TMP_ANALISIS
			WHERE IXE <> 0.00;

			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IXA, 0.00, 0.00, -IXA 
			FROM _TMP_ANALISIS
			WHERE IXA <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_CSIN, 0.00, 0.00, -CSIN 
			FROM _TMP_ANALISIS
			WHERE CSIN <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_ACA, 0.00, 0.00, -ACA 
			FROM _TMP_ANALISIS
			WHERE ACA <> 0.00; 

			-- AHORA POR LOS IMPUESTOS
			INSERT INTO _TMP_CALCULO_NOMINA_DET	-- Ingresa el IMSS solo si el sueldo es mayor al m?nimo
			SELECT _ID_Nomina, ID_Empleado, _M_IMSS, 0.00, 0.00, -IMSS
			FROM _TMP_ANALISIS
			WHERE IMSS <> 0.00 AND Salario_Diario > _SalarioMinimo;

			INSERT INTO _TMP_CALCULO_NOMINA_DET -- Ingresa el ISPT solo si el sueldo es mayor al mnimo
			SELECT _ID_Nomina, ID_Empleado, _M_ISPT, 0.00, 0.00, -ISPT
			FROM _TMP_ANALISIS
			WHERE ISPT <> 0.00 AND Salario_Diario > _SalarioMinimo;
				
			INSERT INTO _TMP_CALCULO_NOMINA_DET -- Ingresa el ISPSB solo si el sueldo es mayor al mnimo
			SELECT _ID_Nomina, ID_Empleado, _M_ISPSB, 0.00, 0.00, ISPSB
			FROM _TMP_ANALISIS
			WHERE ISPSB <> 0.00 AND Salario_Diario > _SalarioMinimo;
				
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_CRDFS, 0.00, 0.00, CRDFS
			FROM _TMP_ANALISIS
			WHERE CRDFS <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET 
			SELECT _ID_Nomina, ID_Empleado, _M_INFON, 0.00, 0.00, -INFON
			FROM _TMP_ANALISIS
			WHERE INFON <> 0.00; 
				
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_FONAC, 0.00, 0.00, -FONAC
			FROM _TMP_ANALISIS
			WHERE FONAC <> 0.00; 
		
			-- AHORA INGRESA LAS PERCEPCIONES 
			-- Aplica los vales segun sea la configuracion
			IF _CalVales = '1'
			THEN
				IF (select VEntero from TBL_VARIABLES where ID_Variable = 'NOMVALS') = 1
				THEN	-- Aplica en nomina de vales
					INSERT INTO _TMP_CALCULO_NOMINA_DET
					SELECT -2, ID_Empleado, _M_VALS, 0.00, VALS, 0.00
					FROM _TMP_ANALISIS
					WHERE VALS <> 0.00; 
					--Agrega ISPT a Cero, esto lo hace para poder sellar la nomina de vales ya que hacienda exige el ISPT en el archivo XML aun si este es cero
					INSERT INTO _TMP_CALCULO_NOMINA_DET -- Ingresa el ISPT solo si los vales son mayor a cero
					SELECT -2, ID_Empleado, _M_ISPT, 0.00, 0.00, 0.00
					FROM _TMP_ANALISIS
					WHERE VALS <> 0.00;
				ELSE 		-- Aplica en nomina normal
					INSERT INTO _TMP_CALCULO_NOMINA_DET
					SELECT _ID_Nomina, ID_Empleado, _M_VALS, 0.00, VALS, 0.00
					FROM _TMP_ANALISIS
					WHERE VALS <> 0.00;
				END IF;
			END IF;

			INSERT INTO _TMP_CALCULO_NOMINA_DET -- Aplica el sueldo en exento o gravado segun salario minimo
			SELECT _ID_Nomina, ID_Empleado, _M_SAL, CASE WHEN Salario_Diario > _SalarioMinimo THEN SAL ELSE 0.00 END, CASE WHEN Salario_Diario > _SalarioMinimo THEN 0.00 ELSE SAL END, 0.00
			FROM _TMP_ANALISIS
			WHERE SAL <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IHEE, 0.00, IHEE, 0.00
			FROM _TMP_ANALISIS
			WHERE IHEE <> 0.00;
				
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IHEG, IHEG, 0.00, 0.00
			FROM _TMP_ANALISIS
			WHERE IHEG <> 0.00; 
				
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IHDE, 0.00, IHDE, 0.00
			FROM _TMP_ANALISIS
			WHERE IHDE <> 0.00; 
				
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IHDG, IHDG, 0.00, 0.00 
			FROM _TMP_ANALISIS
			WHERE IHDG <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_PDE, 0.00, PDE, 0.00 
			FROM _TMP_ANALISIS
			WHERE PDE <> 0.00; 
				
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_PDG,  PDG, 0.00, 0.00
			FROM _TMP_ANALISIS
			WHERE PDG <> 0.00; 
				
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT -1, ID_Empleado, _M_IHE, 0.00, IHE, 0.00
			FROM _TMP_ANALISIS
			WHERE IHE <> 0.00; 
				
			-- AHORA APLICA LAS VACACIONES
			INSERT INTO _TMP_CALCULO_NOMINA_DET -- Aplica las vacaciones en exento o gravado segun salario minimo
			SELECT _ID_Nomina, ID_Empleado, _M_IDV, CASE WHEN Salario_Diario > _SalarioMinimo THEN IDV ELSE 0.00 END, CASE WHEN Salario_Diario > _SalarioMinimo THEN 0.00 ELSE IDV END, 0.00
			FROM _TMP_ANALISIS
			WHERE IDV <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT -1, ID_Empleado, _M_IDVXF, 0.00, IDVXF, 0.00
			FROM _TMP_ANALISIS
			WHERE IDVXF <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET -- Aplica vacaciones a cuenta en exento o gravado segun salario minimo
			SELECT _ID_Nomina, ID_Empleado, _M_IDAV, CASE WHEN Salario_Diario > _SalarioMinimo THEN IDAV ELSE 0.00 END, CASE WHEN Salario_Diario > _SalarioMinimo THEN 0.00 ELSE IDAV END, 0.00
			FROM _TMP_ANALISIS
			WHERE IDAV <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET -- Aplica vacaciones pagadas en exento o gravado segun salario minimo
			SELECT _ID_Nomina, ID_Empleado, _M_IDVP, CASE WHEN Salario_Diario > _SalarioMinimo THEN IDVP ELSE 0.00 END, CASE WHEN Salario_Diario > _SalarioMinimo THEN 0.00 ELSE IDVP END, 0.00
			FROM _TMP_ANALISIS
			WHERE IDVP <> 0.00;
		 
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IDVD, 0.00, 0.00, -IDVD 
			FROM _TMP_ANALISIS
			WHERE IDVD <> 0.00; 
			
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IPVE, 0.00, IPVE, 0.00
			FROM _TMP_ANALISIS
			WHERE IPVE <> 0.00; 

			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IPVG, IPVG, 0.00, 0.00
			FROM _TMP_ANALISIS
			WHERE IPVG <> 0.00; 
				
			--Inserta ahora los movimientos dinamicos para los salarios 
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, p.ID_Empleado, p.ID_Movimiento,  
				CASE 	WHEN me.Salario_Diario > _SalarioMinimo 
							THEN sum(TOTAL_GRAVADO) ELSE 0.00 END, 
				CASE 	WHEN me.Salario_Diario > _SalarioMinimo 
							THEN sum(TOTAL_EXENTO) ELSE sum(TOTAL_GRAVADO) + sum(TOTAL_EXENTO) END, 
				-sum(TOTAL_DEDUCCION)
			FROM _TMP_PLANTILLAS_TOTALES p INNER JOIN TBL_NOM_MASEMP me ON
				p.ID_Empleado = me.ID_Empleado
			WHERE p.Tipo = 12
			GROUP BY p.Tipo, p.ID_Empleado, me.Salario_Diario, p.ID_Movimiento;
					
			--Inserta ahora los movimientos dinamicos de la especial
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT -1, p.ID_Empleado, p.ID_Movimiento,  
					0.0, sum(TOTAL_GRAVADO) + sum(TOTAL_EXENTO), -sum(TOTAL_DEDUCCION)
			FROM _TMP_PLANTILLAS_TOTALES p INNER JOIN TBL_NOM_MASEMP me ON
				p.ID_Empleado = me.ID_Empleado
			WHERE p.Tipo = 34
			GROUP BY p.Tipo, p.ID_Empleado, p.ID_Movimiento;

			--Inserta ahora los movimientos dinamicos de vales
			IF (select VEntero from TBL_VARIABLES where ID_Variable = 'NOMVALS') = 1 and _CalVales = '1'
			THEN	-- Aplica en nomina de vales
				INSERT INTO _TMP_CALCULO_NOMINA_DET
				SELECT -2, p.ID_Empleado, p.ID_Movimiento,  
						sum(TOTAL_GRAVADO), sum(TOTAL_EXENTO), -sum(TOTAL_DEDUCCION)
				FROM _TMP_PLANTILLAS_TOTALES p INNER JOIN TBL_NOM_MASEMP me ON
					p.ID_Empleado = me.ID_Empleado
				WHERE p.Tipo = 78
				GROUP BY p.Tipo, p.ID_Empleado, p.ID_Movimiento;
			END IF;

			-- Inserta ahora la deduccion ASP de Actualizacion a Pago AAP de nominas pasadas
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_ASP, 0.00, 0.00, -(sum(Exento) + sum(Deduccion)) 
			FROM TBL_NOM_CALCULO_NOMINA_DET cnd INNER JOIN TBL_NOM_CALCULO_NOMINA cn ON
				cnd.ID_Nomina = cn.ID_Nomina
			WHERE  ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and Tipo = _Tipo and Ano = _Ano and Numero_Nomina < _Numero_Nomina and
						 ( cnd.ID_Movimiento = _M_AAP OR cnd.ID_Movimiento = _M_ASP )
			GROUP BY ID_Empleado
			HAVING sum(Exento) + sum(Deduccion) > 0;
				
			-- Inserta ahora la Actualizacion a Pago AAP en los recibos menores a cero para dejarlos a cero
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_AAP, 0.00, abs(sum(round(Gravado,1) + round(Exento,1) + round(Deduccion,1))), 0.00 
			FROM _TMP_CALCULO_NOMINA_DET
			WHERE ID_Nomina = _ID_Nomina
			GROUP BY ID_Empleado
			HAVING sum(round(Gravado,1) + round(Exento,1) + round(Deduccion,1)) < 0;
		ELSE -- es de aguinaldos
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IAE, 0.00, IAE, 0.00
			FROM _TMP_ANALISIS
			WHERE IAE <> 0.00;

			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, ID_Empleado, _M_IAG, IAG, 0.00, 0.00
			FROM _TMP_ANALISIS
			WHERE IAG <> 0.00;
			
			INSERT INTO _TMP_CALCULO_NOMINA_DET -- Ingresa el ISPT solo si el sueldo es mayor al mínimo
			SELECT _ID_Nomina, ID_Empleado, _M_ISPT, 0.00, 0.00, -ISPT
			FROM _TMP_ANALISIS
			WHERE ISPT <> 0.00 AND Salario_Diario > _SalarioMinimo;
			
			INSERT INTO _TMP_CALCULO_NOMINA_DET 
			SELECT _ID_Nomina, ID_Empleado, _M_ISPSB, 0.00, 0.00, ISPSB
			FROM _TMP_ANALISIS
			WHERE ISPSB <> 0.00 AND Salario_Diario > _SalarioMinimo;	

			--Inserta ahora los movimientos dinamicos para los aguinaldos
			INSERT INTO _TMP_CALCULO_NOMINA_DET
			SELECT _ID_Nomina, p.ID_Empleado, p.ID_Movimiento,  
					sum(TOTAL_GRAVADO), sum(TOTAL_EXENTO), -sum(TOTAL_DEDUCCION)
			FROM _TMP_PLANTILLAS_TOTALES p INNER JOIN TBL_NOM_MASEMP me ON
				p.ID_Empleado = me.ID_Empleado
			WHERE p.Tipo = 56
			GROUP BY p.Tipo, p.ID_Empleado, p.ID_Movimiento;
			
		END IF;
		--///////////////////////// FIN DE INGRESO DE RESULTADOS //////////////////////////////////////////////
	
		-- //////////////////////////////////////////////////////////////////////////////////////////
		-- ///////////////////////// COPIA DESDE TABLAS TEMPORALES A FINALES //////////////////////////////////
		--//////////////////////////////////////////////////////////////////////////////////////////////////

		INSERT INTO TBL_NOM_CALCULO_NOMINA_ESP
		SELECT _ID_Nomina, ID_Empleado, Dias, Faltas, Recibo, HE, HD, ID_CFD, TFD, Gravado, Exento, Deduccion, ISR, HT, IXA, IXE, IXM, DiasHorasExtras
		FROM _TMP_CALCULO_NOMINA_ESP
		WHERE ID_Nomina = _ID_Nomina;
	 
		INSERT INTO TBL_NOM_CALCULO_NOMINA_DET
		SELECT _ID_Nomina, ID_Empleado, ID_Movimiento, round(Gravado,1), round(Exento,1), round(Deduccion,1)
		FROM _TMP_CALCULO_NOMINA_DET
		WHERE ID_Nomina = _ID_Nomina;
	 
		-- inserta en tabla de asistencias
		INSERT INTO TBL_NOM_CALCULO_NOMINA_ASIST
		SELECT DISTINCT _ID_Nomina, ID_Empleado, ID_FechaMovimiento, ID_Movimiento, Entrada, Salida  
		FROM _TMP_CALCULO_NOMINA_ASIST;
		
		INSERT INTO TBL_NOM_CALCULO_NOMINA_HIS_DET_IMP
		SELECT *
		FROM _TMP_CALCULO_NOMINA_HIS_DET_IMP;
		
		--Ahora aplica el cabecero de nominas especial y vales
		IF (select count(*) from _TMP_CALCULO_NOMINA_DET where ID_Nomina = -1) > 0
		THEN
			INSERT INTO TBL_NOM_CALCULO_NOMINA(id_compania, id_sucursal, ano, numero_nomina, tipo, fecha_desde, fecha_hasta, dias, cerrado, mes, status, formapago, id_mov, id_pol)
 			VALUES(_ID_Compania, _ID_Sucursal, _Ano, _Numero_Nomina, _TipoF, _Fecha_Desde, _Fecha_Hasta, _DiasNomina, '0', _Mes, 'G', 'N', -1, -1)
			RETURNING currval(pg_get_serial_sequence('TBL_NOM_CALCULO_NOMINA', 'id_nomina')) INTO _ID_NominaF;
		
			INSERT INTO TBL_NOM_CALCULO_NOMINA_ESP
			SELECT _ID_NominaF, ID_Empleado, Dias, Faltas, Recibo, HE, HD, ID_CFD, TFD, Gravado, Exento, Deduccion, ISR, HT, IXA, IXE, IXM, DiasHorasExtras
			FROM _TMP_CALCULO_NOMINA_ESP
			WHERE ID_Nomina = -1 and ID_Empleado in (select Distinct ID_Empleado 
														from _TMP_CALCULO_NOMINA_DET 
														where ID_Nomina = -1);
	 
			INSERT INTO TBL_NOM_CALCULO_NOMINA_DET
			SELECT _ID_NominaF, ID_Empleado, ID_Movimiento, round(Gravado,1), round(Exento,1), round(Deduccion,1)
			FROM _TMP_CALCULO_NOMINA_DET
			WHERE ID_Nomina = -1;
		END IF;

		-- Aqui aplica el cabecero de n?mina de vales
		IF (select VEntero from TBL_VARIABLES where ID_Variable = 'NOMVALS') = 1 and _CalVales = '1'
		THEN
			IF (select count(*) from _TMP_CALCULO_NOMINA_DET where ID_Nomina = -2) > 0
			THEN
				INSERT INTO TBL_NOM_CALCULO_NOMINA(id_compania, id_sucursal, ano, numero_nomina, tipo, fecha_desde, fecha_hasta, dias, cerrado, mes, status, formapago, id_mov, id_pol)
				VALUES(_ID_Compania, _ID_Sucursal, _Ano, _Numero_Nomina, _TipoV, _Fecha_Desde, _Fecha_Hasta, _DiasNomina, '0', date_part('Month',_Fecha_Hasta), 'G', 'N', -1, -1)
				RETURNING currval(pg_get_serial_sequence('TBL_NOM_CALCULO_NOMINA', 'id_nomina')) INTO _ID_NominaV;
			
				INSERT INTO TBL_NOM_CALCULO_NOMINA_ESP
				SELECT _ID_NominaV, ID_Empleado, Dias, Faltas, Recibo, HE, HD, ID_CFD, TFD, Gravado, Exento, Deduccion, ISR, HT, IXA, IXE, IXM, DiasHorasExtras
				FROM _TMP_CALCULO_NOMINA_ESP
				WHERE ID_Nomina = -2 and ID_Empleado in (select Distinct ID_Empleado 
															from _TMP_CALCULO_NOMINA_DET
															where ID_Nomina = -2);
	 
				INSERT INTO TBL_NOM_CALCULO_NOMINA_DET
				SELECT _ID_NominaV, ID_Empleado, ID_Movimiento, round(Gravado,1), round(Exento,1), round(Deduccion,1)
				FROM _TMP_CALCULO_NOMINA_DET
				WHERE ID_Nomina = -2;

			END IF;
		END IF;
		-- //////////////////////////////////// FIN DE COPIA ///////////////////////////////////////////
		
		--////////////////////////// ahora genera los cabeceros de recibos con la suma de gravado exento y deduccio e isr de los detalles////////////////////////////////	
		CREATE LOCAL TEMPORARY TABLE _TMP_ESP AS
		SELECT e.ID_Nomina, e.ID_Empleado, sum(d.Gravado) as Gravado, sum(d.Exento) as Exento, sum(d.Deduccion) as Deduccion
		FROM TBL_NOM_CALCULO_NOMINA_DET d INNER JOIN TBL_NOM_CALCULO_NOMINA_ESP e ON
			d.ID_Nomina = e.ID_Nomina and d.ID_Empleado = e.ID_Empleado 
		WHERE e.ID_Nomina = _ID_Nomina or e.ID_Nomina = _ID_NominaF or e.ID_Nomina = _ID_NominaV
		GROUP BY e.ID_Nomina, e.ID_Empleado;

		UPDATE TBL_NOM_CALCULO_NOMINA_ESP
		SET Gravado = t.Gravado, Exento = t.Exento, Deduccion = t.Deduccion
		FROM _TMP_ESP t, TBL_NOM_CALCULO_NOMINA_ESP e 
		WHERE	t.ID_Nomina = e.ID_Nomina and TBL_NOM_CALCULO_NOMINA_ESP.ID_Nomina = e.ID_Nomina and 
				t.ID_Empleado = e.ID_Empleado and TBL_NOM_CALCULO_NOMINA_ESP.ID_Empleado = e.ID_Empleado;

		DROP TABLE _TMP_ESP;

		CREATE LOCAL TEMPORARY TABLE _TMP_ISR AS
		SELECT e.ID_Nomina, e.ID_Empleado, sum(d.Deduccion) as ISR
		FROM TBL_NOM_CALCULO_NOMINA_DET d INNER JOIN TBL_NOM_CALCULO_NOMINA_ESP e ON
			d.ID_Nomina = e.ID_Nomina and d.ID_Empleado = e.ID_Empleado 
		WHERE (e.ID_Nomina = _ID_Nomina or e.ID_Nomina = _ID_NominaF or e.ID_Nomina = _ID_NominaV) and d.ID_Movimiento = 690 --690 es numero de movimiento del ISPT
		GROUP BY e.ID_Nomina, e.ID_Empleado;
		
		UPDATE TBL_NOM_CALCULO_NOMINA_ESP
		SET ISR = t.ISR
		FROM _TMP_ISR t, TBL_NOM_CALCULO_NOMINA_ESP e 
		WHERE 	t.ID_Nomina = e.ID_Nomina and TBL_NOM_CALCULO_NOMINA_ESP.ID_Nomina = e.ID_Nomina and 
				t.ID_Empleado = e.ID_Empleado and TBL_NOM_CALCULO_NOMINA_ESP.ID_Empleado = e.ID_Empleado;

		DROP TABLE _TMP_ISR;
		--//////////////////////////////// 
		
		--//////////////////////////////////////////////////////////////////////////////
		--/////////////////////  BORRA TABLAS TEMPORALES ///////////////////////////////
		DROP TABLE _TMP_PLANTILLAS_TOTALES;
		DROP TABLE _TMP_PERMISOS;
		DROP TABLE _TMP_ANALISIS;
		DROP TABLE _TMP_CALCULO_NOMINA_HIS_DET_IMP;
		DROP TABLE _TMP_CALCULO_NOMINA_ASIST;
		DROP TABLE _TMP_CALCULO_NOMINA_ESP;
		DROP TABLE _TMP_CALCULO_NOMINA_DET;

	END IF;	
	
	RETURN QUERY SELECT _err, _result, _id_nomina;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_calculo_nomina(integer, bit, smallint, smallint, bit, bit)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_calculo_nomina_faltas(
    _tipo smallint,
    _dxincon bit,
    _mesdesc smallint,
    _anodesc smallint,
    _porcentaje numeric,
    _id_empleado character,
    _fecha_desde timestamp without time zone,
    _fecha_hasta timestamp without time zone,
    _salarioporhora numeric,
    _salariodiario numeric,
    _turnoemp smallint,
    _calculomixto bit,
    _salariomixto numeric,
    _fechaingreso timestamp without time zone,
    _castigo_impuntualidad bit)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	-- Variables de regreso
	_TOTAL_FLT numeric(4,2); _TOTAL_IXM numeric(4,2); _TOTAL_IXE numeric(4,2); _TOTAL_IXA numeric(4,2); _result varchar(254);
	_IMPDESC numeric(10,2); _HRS_DESC numeric(6,2); _HRS_FLT numeric(6,2); _HRS_IXM numeric(6,2); _HRS_IXE numeric(6,2); _HRS_IXA numeric(6,2);
	_IMPPORINCON numeric(10,2); _IMPSINPAG numeric(10,2); _HRS_SINPAG numeric(6,2); 
	
	--Variables en declare
	_Permiso_Desde timestamp; _Permiso_Hasta timestamp; 
	_contper smallint; _conttot smallint; _diaper smallint; _diatot smallint; _hoy_es smallint; 
	_HRS_JORNADA numeric(6,2); _DIAS_DESCONTADOS smallint; _PREV_IMPSINPAG numeric(10,2); _PREV_IMPDESC numeric(10,2);
BEGIN
	_TOTAL_FLT := 0.00;
	_TOTAL_IXM := 0.00;
	_TOTAL_IXA := 0.00;
	_TOTAL_IXE := 0.00;
	_IMPDESC := 0.00;
	_IMPSINPAG := 0.00;
	_IMPPORINCON := 0.00;
	_HRS_DESC := 0.00;
	_HRS_FLT := 0.00;
	_HRS_IXM := 0.00;
	_HRS_IXA := 0.00;
	_HRS_IXE := 0.00;

	IF _Tipo = 2 --Es nomina de confianza
	THEN
		-- Primero inserta las asistencias del turno
		_contper := 0;
		_conttot := getfechadiff('day',_Fecha_Desde,_Fecha_Hasta) + 1;
		--raise notice 'total dias %', _conttot;
		
		while _contper < _conttot
		loop
			--_hoy_es = DATEPART(weekday, dateadd(day,_contper,_Fecha_Desde)) 
			_hoy_es := date_part( 'dow',   (_Fecha_Desde + (cast(_contper as text) || ' days')::interval)    ); 
			if _hoy_es = 0 then _hoy_es := 7; end if;
			
			insert into _TMP_CALCULO_NOMINA_ASIST
			select _ID_Empleado, (_Fecha_Desde + (cast(_contper as text) || ' days')::interval), 
						case 	when _hoy_es = 1 and ELunes is null then -4 
									when _hoy_es = 1 and ELunes is not null then -1
									when _hoy_es = 2 and EMartes is null then -4 
									when _hoy_es = 2 and EMartes is not null then -1
									when _hoy_es = 3 and EMiercoles is null then -4 
									when _hoy_es = 3 and EMiercoles is not null then -1
									when _hoy_es = 4 and EJueves is null then -4 
									when _hoy_es = 4 and EJueves is not null then -1
									when _hoy_es = 5 and EViernes is null then -4 
									when _hoy_es = 5 and EViernes is not null then -1
									when _hoy_es = 6 and ESabado is null then -4 
									when _hoy_es = 6 and ESabado is not null then -1
									when _hoy_es = 7 and EDomingo is null then -4 
									when _hoy_es = 7 and EDomingo is not null then -1
									else -4 end,
						case 	when _hoy_es = 1 then ELunes 
										when _hoy_es = 2 then EMartes 
										when _hoy_es = 3 then EMiercoles 
										when _hoy_es = 4 then EJueves 
										when _hoy_es = 5 then EViernes 
										when _hoy_es = 6 then ESabado  
										else EDomingo end, 
							case 	when _hoy_es = 1 then SLunes 
										when _hoy_es = 2 then SMartes 
										when _hoy_es = 3 then SMiercoles 
										when _hoy_es = 4 then SJueves 
										when _hoy_es = 5 then SViernes 
										when _hoy_es = 6 then SSabado  
										else SDomingo end
			from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp;

			_contper := _contper + 1;
		end loop;

		-- Ahora calcula los permisos 
		-- empieza por las faltas. Calcula los dias faltantes.
		-- Ejemplo si el permiso es del 10-ene al 20-ene y la nomina del 1-ene al 15 ene, calcula los dias del 10-ene al 15-ene, 
		--     y los restatnes los calcular? en el periodo de nomina siguiente ( del 16 al 31 de enero )
		
		-- primero copia los permisos de falta a la tabla temporal
		_contper := 1;
		insert into _TMP_PERMISOS (ID_Movimiento, ID_FechaMovimiento, DiasCompletos, FechaHora_Desde, FechaHora_Hasta, Num_de_Dias,	Num_de_Horas, Tiempo_por_pagar)
		select ID_Movimiento, ID_FechaMovimiento, DiasCompletos, FechaHora_Desde, FechaHora_Hasta, Num_de_Dias, Num_de_Horas, Tiempo_por_pagar
		from TBL_NOM_PERMISOS
		where ID_Empleado = _ID_Empleado and ID_Movimiento = -3;
		
		_conttot = (select count(*) from _TMP_PERMISOS);

		while _contper <= _conttot
		loop
			_Permiso_Desde := ( 	select FechaHora_Desde
									from _TMP_PERMISOS
									where ( FechaHora_Hasta >= _Fecha_Desde and FechaHora_Desde <= _Fecha_Hasta and ID_Num = _contper) );
			_Permiso_Hasta := ( 	select FechaHora_Hasta
									from _TMP_PERMISOS
									where ( FechaHora_Hasta >= _Fecha_Desde and FechaHora_Desde <= _Fecha_Hasta and ID_Num = _contper ) );
		
			if(_Permiso_Desde is not null and _Permiso_Hasta is not null and _Permiso_Desde < _Permiso_Hasta)
			then
				if(_Permiso_Desde >= _Fecha_Desde and ( _Permiso_Hasta - '1 day'::interval ) <= _Fecha_Hasta)
				then
					--set _TOTAL_FLT = _TOTAL_FLT +  getfechadiff('day',_Permiso_Desde,(dateadd(day,-1,_Permiso_Hasta)) ) + 1
					_diatot := getfechadiff('day',_Permiso_Desde,( _Permiso_Hasta - '1 day'::interval ) ) + 1;
					_diaper := 0;
					while _diaper < _diatot
					loop
						update _TMP_CALCULO_NOMINA_ASIST
						set ID_Movimiento = -3, Entrada = NULL, Salida = NULL
						where ID_Empleado = ID_Empleado and ID_FechaMovimiento = (_Permiso_Desde + (cast(_diaper as text) || ' days')::interval);
						
						--_hoy_es := DATEPART(weekday, dateadd(day,_diaper,_Permiso_Desde)) 
						_hoy_es := date_part( 'dow',   (_Permiso_Desde + (cast(_diaper as text) || ' days')::interval)    ); 
						if _hoy_es = 0 then _hoy_es := 7; end if;
						
						_TOTAL_FLT := _TOTAL_FLT + 
												case 	when _hoy_es = 1 then round((select HEALunes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 2 then round((select HEAMartes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 3 then round((select HEAMiercoles from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 4 then round((select HEAJueves from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 5 then round((select HEAViernes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 6 then round((select HEASabado from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2)  
															else 0.00 
												end;  
							
						_diaper := _diaper + 1;
					end loop;
				elsif(_Permiso_Desde >= _Fecha_Desde and ( _Permiso_Hasta - '1 day'::interval ) > _Fecha_Hasta)
				then
					--set _TOTAL_FLT = _TOTAL_FLT +  getfechadiff('day',_Permiso_Desde,_Fecha_Hasta) + 1
					_diatot := getfechadiff('day',_Permiso_Desde,_Fecha_Hasta) + 1;
					_diaper := 0;
					while _diaper < _diatot
					loop
						update _TMP_CALCULO_NOMINA_ASIST
						set ID_Movimiento = -3, Entrada = NULL, Salida = NULL
						where ID_Empleado = ID_Empleado and ID_FechaMovimiento = (_Permiso_Desde + (cast(_diaper as text) || ' days')::interval);
		
						_hoy_es := date_part( 'dow',   (_Permiso_Desde + (cast(_diaper as text) || ' days')::interval)    ); 
						if _hoy_es = 0 then _hoy_es := 7; end if;
						
						_TOTAL_FLT := _TOTAL_FLT + 
												case 	when _hoy_es = 1 then round((select HEALunes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 2 then round((select HEAMartes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 3 then round((select HEAMiercoles from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 4 then round((select HEAJueves from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 5 then round((select HEAViernes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 6 then round((select HEASabado from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2)  
															else 0.00 
												end;  
							
						_diaper := _diaper + 1;
					end loop;
				elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
				then
					--set _TOTAL_FLT = _TOTAL_FLT +  getfechadiff('day',_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval)) + 1
					_diatot := getfechadiff('day',_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval)) + 1;
					_diaper := 0;
					while _diaper < _diatot
					loop
						update _TMP_CALCULO_NOMINA_ASIST
						set ID_Movimiento = -3, Entrada = NULL, Salida = NULL
						where ID_Empleado = ID_Empleado and ID_FechaMovimiento = (_Fecha_Desde + (cast(_diaper as text) || ' days')::interval);
		
						--set _hoy_es = DATEPART(weekday, (_Fecha_Desde + (cast(_diaper as text) || ' days')::interval)) 
						_hoy_es := date_part( 'dow',   (_Fecha_Desde + (cast(_diaper as text) || ' days')::interval)    ); 
						if _hoy_es = 0 then _hoy_es := 7; end if;
						
						_TOTAL_FLT := _TOTAL_FLT + 
												case 	when _hoy_es = 1 then round((select HEALunes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 2 then round((select HEAMartes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 3 then round((select HEAMiercoles from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 4 then round((select HEAJueves from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 5 then round((select HEAViernes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 6 then round((select HEASabado from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2)  
															else 0.00 
												end;  
							
						_diaper := _diaper + 1;
					end loop;
				elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
				then
					--set _TOTAL_FLT = _TOTAL_FLT +  getfechadiff('day',_Fecha_Desde,_Fecha_Hasta) + 1
					_diatot = getfechadiff('day',_Fecha_Desde,_Fecha_Hasta) + 1;
					_diaper = 0;
					while _diaper < _diatot
					loop
						update _TMP_CALCULO_NOMINA_ASIST
						set ID_Movimiento = -3, Entrada = NULL, Salida = NULL
						where ID_Empleado = ID_Empleado and ID_FechaMovimiento = (_Fecha_Desde + (cast(_diaper as text) || ' days')::interval);
		
						_hoy_es := date_part( 'dow',  (_Fecha_Desde + (cast(_diaper as text) || ' days')::interval));
						if _hoy_es = 0 then _hoy_es := 7; end if;
						
						_TOTAL_FLT = _TOTAL_FLT + 
												case 	when _hoy_es = 1 then round((select HEALunes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 2 then round((select HEAMartes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 3 then round((select HEAMiercoles from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 4 then round((select HEAJueves from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 5 then round((select HEAViernes from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2) 
															when _hoy_es = 6 then round((select HEASabado from TBL_NOM_TURNOS where ID_Turno = _TurnoEmp)/8, 2)  
															else 0.00 
												end;  
							
						_diaper := _diaper + 1;
					end loop;
				end if;
			end if;
			_contper := _contper + 1;  
		end loop;

		--select * from _TMP_PERMISOS
		truncate table _TMP_PERMISOS  RESTART IDENTITY;
	
	
		-- ahora los de incapacidad por maternidad
		_Permiso_Desde := null;
		_Permiso_Hasta := null;
		
		-- primero copia los permisos de incapacidad por maternidad a la tabla temporal
		_contper := 1;
		insert into _TMP_PERMISOS (ID_Movimiento, ID_FechaMovimiento, DiasCompletos, FechaHora_Desde, FechaHora_Hasta, Num_de_Dias,	Num_de_Horas, Tiempo_por_pagar)
		select ID_Movimiento, ID_FechaMovimiento, DiasCompletos, FechaHora_Desde, FechaHora_Hasta, Num_de_Dias, Num_de_Horas, Tiempo_por_pagar
		from TBL_NOM_PERMISOS
		where ID_Empleado = _ID_Empleado and ID_Movimiento = -11;
		
		_conttot := (select count(*) from _TMP_PERMISOS);

		while _contper <= _conttot
		loop
			_Permiso_Desde := (	 	select FechaHora_Desde
										from _TMP_PERMISOS
										where ( FechaHora_Hasta >= _Fecha_Desde and 
										FechaHora_Desde <= _Fecha_Hasta and ID_Num = _contper) );
			_Permiso_Hasta := ( 	select FechaHora_Hasta
									from _TMP_PERMISOS
									where ( FechaHora_Hasta >= _Fecha_Desde and 
									FechaHora_Desde <= _Fecha_Hasta and ID_Num = _contper ) );
		
			if(_Permiso_Desde is not null and _Permiso_Hasta is not null and _Permiso_Desde < _Permiso_Hasta)
			then
				if(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -11, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Permiso_Desde and (_Permiso_Hasta - '1 day'::interval);

					_TOTAL_IXM := _TOTAL_IXM + getfechadiff('day',_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval) ) + 1;
				elsif(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -11, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Permiso_Desde and _Fecha_Hasta;

					_TOTAL_IXM := _TOTAL_IXM +  getfechadiff('day',_Permiso_Desde,_Fecha_Hasta) + 1;
				elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -11, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval);

					_TOTAL_IXM := _TOTAL_IXM +  getfechadiff('day',_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval)) + 1;
				elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -11, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta;

					_TOTAL_IXM := _TOTAL_IXM +  getfechadiff('day',_Fecha_Desde,_Fecha_Hasta) + 1;
				end if;
			end if;
			_contper := _contper + 1;  
		end loop;
		
		truncate table _TMP_PERMISOS RESTART IDENTITY;
		
		-- ahora los de incapacidad por enfermedad
		_Permiso_Desde := null;
		_Permiso_Hasta := null;
		
		-- primero copia los permisos de incapacidad por enfermedad a la tabla temporal
		_contper := 1;
		
		insert into _TMP_PERMISOS (ID_Movimiento, ID_FechaMovimiento, DiasCompletos, FechaHora_Desde, FechaHora_Hasta, Num_de_Dias,	Num_de_Horas, Tiempo_por_pagar)
		select ID_Movimiento, ID_FechaMovimiento, DiasCompletos, FechaHora_Desde, FechaHora_Hasta, Num_de_Dias, Num_de_Horas, Tiempo_por_pagar
		from TBL_NOM_PERMISOS
		where ID_Empleado = _ID_Empleado and ID_Movimiento = -12;
		
		_conttot := (select count(*) from _TMP_PERMISOS);

		while _contper <= _conttot
		loop
			_Permiso_Desde := (	 	select FechaHora_Desde
										from _TMP_PERMISOS
										where ( FechaHora_Hasta >= _Fecha_Desde and 
										FechaHora_Desde <= _Fecha_Hasta and ID_Num = _contper) );
			_Permiso_Hasta := ( 	select FechaHora_Hasta
									from _TMP_PERMISOS
									where ( FechaHora_Hasta >= _Fecha_Desde and 
									FechaHora_Desde <= _Fecha_Hasta and ID_Num = _contper ) );
		
			if(_Permiso_Desde is not null and _Permiso_Hasta is not null and _Permiso_Desde < _Permiso_Hasta)
			then
				if(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -12, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Permiso_Desde and (_Permiso_Hasta - '1 day'::interval);

					_TOTAL_IXE := _TOTAL_IXE + getfechadiff('day',_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval) ) + 1;
				elsif(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -12, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Permiso_Desde and _Fecha_Hasta;

					_TOTAL_IXE := _TOTAL_IXE +  getfechadiff('day',_Permiso_Desde,_Fecha_Hasta) + 1;
				elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -12, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval);

					_TOTAL_IXE := _TOTAL_IXE +  getfechadiff('day',_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval)) + 1;
				elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -12, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta;

					_TOTAL_IXE := _TOTAL_IXE +  getfechadiff('day',_Fecha_Desde,_Fecha_Hasta) + 1;
				end if;
			end if;
			_contper := _contper + 1;  
		end loop;
		
		truncate table _TMP_PERMISOS RESTART IDENTITY;
		
		-- ahora los de incapacidad por accidente
		_Permiso_Desde := null;
		_Permiso_Hasta := null;
		
		-- primero copia los permisos de incapacidad por accidente a la tabla temporal
		_contper := 1;
		
		insert into _TMP_PERMISOS (ID_Movimiento, ID_FechaMovimiento, DiasCompletos, FechaHora_Desde, FechaHora_Hasta, Num_de_Dias,	Num_de_Horas, Tiempo_por_pagar)
		select ID_Movimiento, ID_FechaMovimiento, DiasCompletos, FechaHora_Desde, FechaHora_Hasta, Num_de_Dias, Num_de_Horas, Tiempo_por_pagar
		from TBL_NOM_PERMISOS
		where ID_Empleado = _ID_Empleado and ID_Movimiento = -13;
		
		_conttot := (select count(*) from _TMP_PERMISOS);
		
		while _contper <= _conttot
		loop
			_Permiso_Desde := (	 	select FechaHora_Desde
										from _TMP_PERMISOS
										where ( FechaHora_Hasta >= _Fecha_Desde and 
										FechaHora_Desde <= _Fecha_Hasta and ID_Num = _contper) );
			_Permiso_Hasta := ( 	select FechaHora_Hasta
									from _TMP_PERMISOS
									where ( FechaHora_Hasta >= _Fecha_Desde and 
									FechaHora_Desde <= _Fecha_Hasta and ID_Num = _contper ) );
		
			if(_Permiso_Desde is not null and _Permiso_Hasta is not null and _Permiso_Desde < _Permiso_Hasta)
			then
				if(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -13, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Permiso_Desde and (_Permiso_Hasta - '1 day'::interval);

					_TOTAL_IXA := _TOTAL_IXA + getfechadiff('day',_Permiso_Desde,(_Permiso_Hasta - '1 day'::interval) ) + 1;
				elsif(_Permiso_Desde >= _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -13, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Permiso_Desde and _Fecha_Hasta;

					_TOTAL_IXA := _TOTAL_IXA +  getfechadiff('day',_Permiso_Desde,_Fecha_Hasta) + 1;
				elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) <= _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -13, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval);

					_TOTAL_IXA := _TOTAL_IXA +  getfechadiff('day',_Fecha_Desde,(_Permiso_Hasta - '1 day'::interval)) + 1;
				elsif(_Permiso_Desde < _Fecha_Desde and (_Permiso_Hasta - '1 day'::interval) > _Fecha_Hasta)
				then
					update _TMP_CALCULO_NOMINA_ASIST
					set ID_Movimiento = -13, Entrada = NULL, Salida = NULL
					where ID_Empleado = ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta;

					_TOTAL_IXA := _TOTAL_IXA +  getfechadiff('day',_Fecha_Desde,_Fecha_Hasta) + 1;
				end if;
			end if;
			_contper := _contper + 1;  
		end loop;
		-- fin de calculo nomina de confianza
	ELSE
		-- calcula las faltas y las incapacidades y asistencias de nomina de los de la nomina estricta
		-- comienza por el calculo de las asistencias y faltas o incapacidades
		insert into _TMP_CALCULO_NOMINA_ASIST
		select _ID_Empleado, ID_FechaMovimiento, ID_Movimiento, Entrada, case when Salida2 is not null then Salida2 else Salida end
		from TBL_NOM_DIARIO_DET 
		where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and 
			( ID_Movimiento = -1 or ID_Movimiento = -2 or ID_Movimiento = -3 or ID_Movimiento = -4  or 
				ID_Movimiento = -5 or ID_Movimiento = -7 or ID_Movimiento = -9 or ID_Movimiento = -11 or 
				ID_Movimiento = -12 or ID_Movimiento = -13 or ID_Movimiento = -16 or ID_Movimiento = -18 or 
				ID_Movimiento = -20 or ID_Movimiento = -26 );

		-- empieza por calcular las horas perdidas, ( las de retraso o por irse antes )
		_DIAS_DESCONTADOS := ( 	select count(*) 
									from TBL_NOM_DIARIO_DET 
									where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -2 or ID_Movimiento = -25 ) );
												
		_HRS_JORNADA := 	case 	when ( 	select sum(HNA)
											from TBL_NOM_DIARIO_DET 
											where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -2 or ID_Movimiento = -25 ) ) is null
									then 0.00 
									else	(	select sum(HNA)
												from TBL_NOM_DIARIO_DET 
												where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -2 or ID_Movimiento = -25 ) )
							end;
		_HRS_DESC := case 	when ( 	select sum(HNP)
									from TBL_NOM_DIARIO_DET 
									where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -2 or ID_Movimiento = -25 ) ) is null
							then 0.00 
							else ( 	select sum(HNP)
									from TBL_NOM_DIARIO_DET 
									where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -2 or ID_Movimiento = -25 ) ) 
						end;
		_HRS_SINPAG := case 	when ( 	select sum(HNA)
										from TBL_NOM_DIARIO_DET 
										where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -17 or ID_Movimiento = -23 or ID_Movimiento = -24 ) ) is null
								then 0.00 
								else ( 	select sum(HNA)
										from TBL_NOM_DIARIO_DET 
										where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -17 or ID_Movimiento = -23 or ID_Movimiento = -24 ) ) 
						end;
		_PREV_IMPDESC := case 	when ( 	select sum(HNP)
											from TBL_NOM_DIARIO_DET 
											where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -2 or ID_Movimiento = -25 ) ) is null
									then 0.00 
									else	(	select SUM( round( (((HNA / 8) * _SalarioPorHora) * HNP),2) ) -- (HNA / 8) as Factor, ((HNA / 8) * _SalarioPorHora) as SPH, round( (((HNA / 8) * _SalarioPorHora) * HNP),2) as TOTAL
												from TBL_NOM_DIARIO_DET 
												where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -2 or ID_Movimiento = -25 ) )
							end;

		_PREV_IMPSINPAG := case 	when ( 	select sum(HNA)
											from TBL_NOM_DIARIO_DET 
											where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -17 or ID_Movimiento = -23 or ID_Movimiento = -24 ) ) is null
									then 0.00 
									else	case 	when _DIAS_DESCONTADOS > 0 
													then (	select SUM( round( (((_HRS_JORNADA / _DIAS_DESCONTADOS / 8) * _SalarioPorHora) * HNA),2) )
															from TBL_NOM_DIARIO_DET 
															where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ( ID_Movimiento = -17 or ID_Movimiento = -23 or ID_Movimiento = -24 ) )
													else 0.00
											end
							end;

		-- el descuento por retardos se nulifica en caso de que existan descuentos por permisos de Descuentos Sin Pago De Tiempo
		_IMPDESC :=  ( CASE WHEN ( _PREV_IMPDESC - _PREV_IMPSINPAG ) < 0 THEN 0.00 ELSE ( _PREV_IMPDESC - _PREV_IMPSINPAG ) END );
		_IMPSINPAG := ( CASE WHEN ( _PREV_IMPDESC - _PREV_IMPSINPAG ) < 0 THEN _PREV_IMPDESC ELSE _PREV_IMPSINPAG END );

		-- ahora calcula faltas e incapacidades
		_TOTAL_FLT := case when ( 	select sum(HNP)
									from TBL_NOM_DIARIO_DET 
									where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -3 ) is null
							then 0.00 
							else round((
										( 	select sum(HNP)
											from TBL_NOM_DIARIO_DET 
											where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -3 ) / 8),2)
						end;

		_HRS_FLT := case when ( 	select sum(HNP)
								from TBL_NOM_DIARIO_DET 
								where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -3 ) is null
						then 0.00 
						else ( 	select sum(HNP)
								from TBL_NOM_DIARIO_DET 
								where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -3 ) 
					end;
		_TOTAL_IXM := case when ( 	select sum(HNP)
									from TBL_NOM_DIARIO_DET 
									where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -11 ) is null
							then 0.00 
							else round((
											( 	select sum(HNP)
												from TBL_NOM_DIARIO_DET 
												where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -11 ) / 8),2)
						end;
		_HRS_IXM := case when ( 	select sum(HNP)
									from TBL_NOM_DIARIO_DET 
									where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -11 ) is null
						then 0.00 
						else ( 	select sum(HNP)
								from TBL_NOM_DIARIO_DET 
								where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -11 ) 
					end;
		_TOTAL_IXE := case when ( 	select sum(HNP)
									from TBL_NOM_DIARIO_DET 
									where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -12 ) is null
							then 0.00 
							else round((
										( 	select sum(HNP)
											from TBL_NOM_DIARIO_DET 
											where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -12 ) / 8),2)
					end;
		_HRS_IXE := case when ( 	select sum(HNP)
															from TBL_NOM_DIARIO_DET 
															where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -12 ) is null
											then 0.00 
											else ( 	select sum(HNP)
															from TBL_NOM_DIARIO_DET 
															where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -12 ) 
											end;
		_TOTAL_IXA := case when ( 	select sum(HNP)
															from TBL_NOM_DIARIO_DET 
															where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -13 ) is null
											then 0.00 
											else round((
													( 	select sum(HNP)
															from TBL_NOM_DIARIO_DET 
															where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -13 ) / 8),2)
											end;
		_HRS_IXA := case when ( 	select sum(HNP)
									from TBL_NOM_DIARIO_DET 
									where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -13 ) is null
						then 0.00 
						else ( 	select sum(HNP)
								from TBL_NOM_DIARIO_DET 
								where ID_Empleado = _ID_Empleado and ID_FechaMovimiento between _Fecha_Desde and _Fecha_Hasta and ID_Movimiento = -13 ) 
					end;

		-- ahora calcula el importe por inconsistencias
		IF _DXIncon = '1' and _Castigo_Impuntualidad = '1'
		THEN
			--print 'Inconcistencias'
			IF date_part('year',_FechaIngreso) > _AnoDesc or ( date_part('year',_FechaIngreso) = _AnoDesc and date_part('month',_FechaIngreso) > _MesDesc ) 
			THEN
				_IMPPORINCON = 0.00;
			ELSE
				CREATE LOCAL TEMPORARY TABLE _TMP_INCONSIS (
					ID_FechaMovimiento timestamp NOT NULL ,
					Incon smallint NOT NULL 
				);

				insert into _TMP_INCONSIS 
				select dd.ID_FechaMovimiento, 
							case when 
								( select sum(HNA) from TBL_NOM_DIARIO_DET
									where ID_FechaMovimiento = dd.ID_FechaMovimiento and 
												ID_Empleado = _ID_Empleado and 
												( ID_Movimiento = -15 or ID_Movimiento = -17 or ID_Movimiento = -19 or ID_Movimiento = -30 or ID_Movimiento = -31 or ID_Movimiento = -32 or 
													ID_Movimiento = -21 or ID_Movimiento = -22 or ID_Movimiento = -23 or ID_Movimiento = -24 )   ) is null then 1
									when		dd.HNP -  
								( select sum(HNA) from TBL_NOM_DIARIO_DET
									where ID_FechaMovimiento = dd.ID_FechaMovimiento and 
												ID_Empleado = _ID_Empleado and 
												( ID_Movimiento = -15 or ID_Movimiento = -17 or ID_Movimiento = -19 or ID_Movimiento = -30 or ID_Movimiento = -31 or ID_Movimiento = -32 or 
													ID_Movimiento = -21 or ID_Movimiento = -22 or ID_Movimiento = -23 or ID_Movimiento = -24 )   ) > 0 then 1 
									else 0 
							end
				from TBL_NOM_DIARIO_DET dd
				where ID_Empleado = _ID_Empleado and date_part('month',ID_FechaMovimiento) = _MesDesc and 
							date_part('year',ID_FechaMovimiento) = _AnoDesc and ( ID_Movimiento = -2 or (ID_Movimiento = -25 and HNP > 0.00));
			
				insert into _TMP_INCONSIS 
				select dd.ID_FechaMovimiento, 
							case when 
								( select count(*) from TBL_NOM_DIARIO_DET
									where ID_FechaMovimiento = dd.ID_FechaMovimiento and 
												ID_Empleado = _ID_Empleado and 
												 ID_Movimiento = -18  ) = 0 then 1
								else 0 
							end
				from TBL_NOM_DIARIO_DET dd
				where ID_Empleado = _ID_Empleado and date_part('month',ID_FechaMovimiento) = _MesDesc and 
							date_part('year',ID_FechaMovimiento) = _AnoDesc and ID_Movimiento = -3;
							
				_IMPPORINCON := CASE WHEN ( select sum(Incon) from _TMP_INCONSIS ) is null 
										THEN 0.00
										ELSE ROUND( _Porcentaje * (case when _CalculoMixto = '0' then _SalarioDiario else _SalarioMixto end) * (select sum(Incon) from _TMP_INCONSIS), 2)
									END;

				DROP TABLE _TMP_INCONSIS;  
			END IF;
		END IF;
	END IF;

	truncate table _TMP_PERMISOS RESTART IDENTITY;
	--truncate table _TMP_CALCULO_NOMINA_ASIST

	RETURN QUERY
	SELECT	_TOTAL_FLT, _TOTAL_IXM, _TOTAL_IXE, _TOTAL_IXA, _result,
			_IMPDESC, _HRS_DESC, _HRS_FLT, _HRS_IXM, _HRS_IXE, _HRS_IXA,
			_IMPPORINCON, _IMPSINPAG, _HRS_SINPAG;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_calculo_nomina_faltas(smallint, bit, smallint, smallint, numeric, character, timestamp without time zone, timestamp without time zone, numeric, numeric, smallint, bit, numeric, timestamp without time zone, bit)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_diario_cierre(
    _compania_sucursal character varying,
    _fechadesde timestamp without time zone,
    _fechahasta timestamp without time zone)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_status smallint; _err int; _result varchar(8000); _dia smallint; _contdia smallint; _cont smallint;
	_ID_Compania smallint; _ID_Sucursal smallint; _ID_FechaMovimiento timestamp;
BEGIN
	_ID_Compania := (select ID_Compania from TBL_COMPANIAS where Descripcion = _Compania_Sucursal);
	_ID_Sucursal := (select ID_Sucursal from TBL_COMPANIAS where Descripcion = _Compania_Sucursal);
	_err := 0;
	_result := 'Se han cerrado los dias satisfactoriamente';
	--SET DATEFIRST 1
	_contdia := DATE_PART('day', _FechaHasta - _FechaDesde);
	_cont := 0;

	-- Revisa por los movimientos b?sicos de sistema.
	IF (select count(*) from TBL_NOM_MOVIMIENTOS where ID_Movimiento between -32 and -1 ) < 29
	THEN
		_err := 3;
		_result := 'ERROR: No existen todos los movimientos básicos para permisos. Primero debe darlos de alta para poder cerrar el dia'; 
	END IF;
	IF( select count(*) from TBL_NOM_DIARIO_CAB where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and Cerrado = '1'
			and ID_FechaMovimiento between _FechaDesde and _FechaHasta ) > 0
	THEN
		_err := 3;
		_result := 'ERROR: Existen dias cerrados y protegidos en este rango de fechas. No se puede calcular el cierre';
	END IF;
	IF _contdia > 31
	THEN
		_err := 3;
		_result := 'ERROR: Existen demasiados días en este rango de fechas. Solo es posible calcular hasta un máximo de 31 días en una sola operación de cierre. No se puede calcular';
	END IF;
			
	IF _err = 0
	THEN
		-- Primero crea tabla temporal de asistencias con turno del dia para hacer mas facil el c?lculo
		CREATE LOCAL TEMPORARY TABLE _TMP_ASISTENCIAS (
			ID_Empleado char(6) NOT NULL ,
			Asistio bit NOT NULL,
			Desde timestamp NULL ,
			Hasta timestamp NULL ,
			Entrada timestamp NULL ,
			Salida timestamp NULL ,
			HNA numeric(4, 2) NULL ,
			HEA numeric(4, 2) NULL ,
			Indicador smallint NOT NULL,
			Entrada2 timestamp NULL ,
			Salida2 timestamp NULL 
			/*CONSTRAINT TMP_ TBL_NOM_ASISTENCIAS PRIMARY KEY  CLUSTERED 
			(
				ID_Empleado
			)*/
		);
	
		WHILE _cont <= _contdia
		LOOP
			-- primero genera el dia del movimiento
			_ID_FechaMovimiento := _FechaDesde + (cast(_cont as text) || ' days')::interval;	--DATEADD(day, _cont, _FechaDesde)
			_dia := date_part( 'dow', _ID_FechaMovimiento); --('weekday', _ID_FechaMovimiento);
			 if _dia = 0 then _dia = 7; end if;
			 
			_err := 0;
			_status := 0; -- Se presume que no se ha creado el dia
	
			raise notice 'FECHA DE MOVIMIENTO : % DIA %', cast(_ID_FechaMovimiento as varchar), _dia;

			IF (select count(*) from TBL_NOM_DIARIO_CAB where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and ID_FechaMovimiento = _ID_FechaMovimiento and Cerrado = '0' ) > 0
			THEN
				-- como ya existe este dia, lo borra para volverlo a cerrar
				--raise notice ' Dia Borrado porque ya existia';
				_status := 1; -- El dia esta creado pero no cerrado
				DELETE FROM TBL_NOM_DIARIO_DET
				WHERE ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and ID_FechaMovimiento = _ID_FechaMovimiento;
				
			END IF;
			
			/* AQUI ELIMINA EMPLEADOS QUE TIENEN CIERRES DUPLICADOS; OSEA SI UN EMPLEADO SE LE CAMBIO SU COMPA?IA DE LA CUAL YA TENIA CIERRE, SE ELIMINA ESE CIERRE PARA QUE
			-- EL CIERRE QUE SE ESTA CALCULANDO SAE UNICO PARA ESTE EMPLEADO (No soportada aun en postgresql)
			DELETE TBL_DIARIO_DET 
			FROM TBL_DIARIO_DET a INNER JOIN TBL_NOM_MASEMP m ON
				a.ID_Empleado = m.ID_Empleado
			WHERE a.ID_FechaMovimiento = @ID_FechaMovimiento and ( m.Status = 0 or (m.Status = 2 and m.Fecha_para_Liquidaciones >= @ID_FechaMovimiento)) and
									m.Tipo_de_Nomina = 1 and m.ID_Compania = @ID_Compania and m.ID_Sucursal = @ID_Sucursal */

			IF _err = 0
			THEN
				-- Primero genera el calculo del dia de asistencias chequeos a asistencias
				-- ejecutando el procedimiento del dia. Este debe de manejar los errores y corregirlos directos
				-- en el procedimiento para pasar libre al seguiente enunciado de este cierre

				execute sp_nom_asistencias_server_calcular( _ID_FechaMovimiento, _dia, _ID_Compania, _ID_Sucursal);

				--raise notice 'Status %', _status;

				-- Ahora aplica el dia
				IF _status = 0 -- si el dia no existe
				THEN
					INSERT INTO TBL_NOM_DIARIO_CAB
					VALUES(_ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, '0');
				END IF;

				--INSERTA LOS QUE REGISTRARON Y ESTAN CONSISTENTESEN EL REGISTRO ( REGISTRO DE ENTRADA Y SALIDA, NO SOLO ENTRADA POR EJEMPLO )
				INSERT INTO _TMP_ASISTENCIAS
				SELECT a.ID_Empleado, '1',
						case 	when _dia = 1 then getfechamashora(_ID_FechaMovimiento, (select ELunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)) 
								when _dia = 2 then getfechamashora(_ID_FechaMovimiento, (select EMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 3 then getfechamashora(_ID_FechaMovimiento, (select EMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 4 then getfechamashora(_ID_FechaMovimiento, (select EJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 5 then getfechamashora(_ID_FechaMovimiento, (select EViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 6 then getfechamashora(_ID_FechaMovimiento, (select ESabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 7 then getfechamashora(_ID_FechaMovimiento, (select EDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)) end as desde,  
						
						case 	when _dia = 1 and ((select ELunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(  (_ID_FechaMovimiento + '1 day'::interval), (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 1 and ((select ELunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  
													
								when _dia = 2 and ((select EMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 2 and ((select EMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
																					then getfechamashora(_ID_FechaMovimiento, (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))					 
													
								when _dia = 3 and ((select EMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 3 and ((select EMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  		 

								when _dia = 4 and ((select EJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 4 and ((select EJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  			 
					
								when _dia = 5 and ((select EViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 5 and ((select EViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  	 

								when _dia = 6 and ((select ESabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 6 and ((select ESabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  		 
					
								when _dia = 7 and ((select EDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 7 and ((select EDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)) end as hasta, 			 

							a.Entrada, a.Salida,		 
							case 	when _dia = 1 then (select HNALunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 2 then (select HNAMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 3 then (select HNAMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 4 then (select HNAJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 5 then (select HNAViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 6 then (select HNASabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 7 then (select HNADomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) end as HNA,
													 
							case 	when _dia = 1 then (select HEALunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 2 then (select HEAMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 3 then (select HEAMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 4 then (select HEAJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 5 then (select HEAViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 6 then (select HEASabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 7 then (select HEADomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) end as HEA, 
							a.Indicador, a.Entrada2, a.Salida2							
						
					FROM  TBL_NOM_ASISTENCIAS a INNER JOIN TBL_NOM_MASEMP m ON
						a.ID_Empleado = m.ID_Empleado 
					WHERE ( a.Indicador = 2 or a.Indicador = 4 ) and 
										a.ID_FechaMovimiento = _ID_FechaMovimiento and ( m.Status = 0 or (m.Status = 2 and m.Fecha_para_Liquidaciones >= _ID_FechaMovimiento)) and m.Fecha_de_Ingreso <= _ID_FechaMovimiento and 
																											m.ID_Compania = _ID_Compania and m.ID_Sucursal = _ID_Sucursal;
		
				
					-- INSERTA LOS QUE NO REGISTRARON
					INSERT INTO _TMP_ASISTENCIAS
					SELECT m.ID_Empleado, '0', 
						case 	when _dia = 1 then getfechamashora(_ID_FechaMovimiento, (select ELunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)) 
								when _dia = 2 then getfechamashora(_ID_FechaMovimiento, (select EMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 3 then getfechamashora(_ID_FechaMovimiento, (select EMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 4 then getfechamashora(_ID_FechaMovimiento, (select EJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 5 then getfechamashora(_ID_FechaMovimiento, (select EViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 6 then getfechamashora(_ID_FechaMovimiento, (select ESabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 7 then getfechamashora(_ID_FechaMovimiento, (select EDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)) end as desde,  
						
						case 	when _dia = 1 and ((select ELunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 1 and ((select ELunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  
													
								when _dia = 2 and ((select EMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 2 and ((select EMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))					 
													
								when _dia = 3 and ((select EMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 3 and ((select EMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  		 
					
								when _dia = 4 and ((select EJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 4 and ((select EJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  			 
					
								when _dia = 5 and ((select EViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 5 and ((select EViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  	 
					
								when _dia = 6 and ((select ESabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 6 and ((select ESabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  		 
					
								when _dia = 7 and ((select EDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 7 and ((select EDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)) end as hasta, 			 
							null as Entrada, null as Salida,		 

							case 	when _dia = 1 then (select HNALunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 2 then (select HNAMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 3 then (select HNAMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 4 then (select HNAJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 5 then (select HNAViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 6 then (select HNASabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 7 then (select HNADomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) end as HNA,
													 
							case 	when _dia = 1 then (select HEALunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 2 then (select HEAMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 3 then (select HEAMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 4 then (select HEAJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 5 then (select HEAViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 6 then (select HEASabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 7 then (select HEADomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) end as HEA,
							0, null as Entrada2, null as Salida2
														
					FROM TBL_NOM_MASEMP m
					WHERE ( m.Status = 0 or (m.Status = 2 and m.Fecha_para_Liquidaciones >= _ID_FechaMovimiento)) and m.Fecha_de_Ingreso <= _ID_FechaMovimiento and m.ID_Compania = _ID_Compania and m.ID_Sucursal = _ID_Sucursal and 
							ID_Empleado NOT IN 
								( select ID_Empleado 
									from  TBL_NOM_ASISTENCIAS 
									where ID_FechaMovimiento = _ID_FechaMovimiento );
														
					--INSERTA LOS QUE REGISTRARON Y ESTAN INCONSISTENTES EN EL REGISTRO (EJ: LOS QUE SOLO TIENEN ENTRADA)		
					INSERT INTO _TMP_ASISTENCIAS
					SELECT m.ID_Empleado, '0', 
						case 	when _dia = 1 then getfechamashora(_ID_FechaMovimiento, (select ELunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)) 
								when _dia = 2 then getfechamashora(_ID_FechaMovimiento, (select EMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 3 then getfechamashora(_ID_FechaMovimiento, (select EMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 4 then getfechamashora(_ID_FechaMovimiento, (select EJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 5 then getfechamashora(_ID_FechaMovimiento, (select EViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 6 then getfechamashora(_ID_FechaMovimiento, (select ESabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
								when _dia = 7 then getfechamashora(_ID_FechaMovimiento, (select EDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)) end as desde,  
						
						case 	when _dia = 1 and ((select ELunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 1 and ((select ELunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SLunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  
													
								when _dia = 2 and ((select EMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 2 and ((select EMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))					 
													
								when _dia = 3 and ((select EMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 3 and ((select EMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
					
								when _dia = 4 and ((select EJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 4 and ((select EJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  			 
					
								when _dia = 5 and ((select EViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 5 and ((select EViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  	 
					
								when _dia = 6 and ((select ESabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 6 and ((select ESabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SSabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  		 
					
								when _dia = 7 and ((select EDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) >= (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora((_ID_FechaMovimiento + '1 day'::interval), (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))  					 
								when _dia = 7 and ((select EDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) < (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno))
									then getfechamashora(_ID_FechaMovimiento, (select SDomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)) end as hasta, 			 

							null as Entrada, null as Salida,		 
							 
							case 	when _dia = 1 then (select HNALunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 2 then (select HNAMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 3 then (select HNAMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 4 then (select HNAJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 5 then (select HNAViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 6 then (select HNASabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 7 then (select HNADomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) end as HNA,
													 
							case 	when _dia = 1 then (select HEALunes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 2 then (select HEAMartes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 3 then (select HEAMiercoles from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 4 then (select HEAJueves from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 5 then (select HEAViernes from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 6 then (select HEASabado from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno)
									when _dia = 7 then (select HEADomingo from TBL_NOM_TURNOS where ID_Turno = m.ID_Turno) end as HEA,
							0, null as Entrada2, null as Salida2
				
					FROM  TBL_NOM_ASISTENCIAS a INNER JOIN TBL_NOM_MASEMP m ON
						a.ID_Empleado = m.ID_Empleado 
					WHERE ( a.Indicador = 1 or a.Indicador = 3 ) and 
										a.ID_FechaMovimiento = _ID_FechaMovimiento and ( m.Status = 0 or (m.Status = 2 and m.Fecha_para_Liquidaciones >= _ID_FechaMovimiento)) and m.Fecha_de_Ingreso <= _ID_FechaMovimiento and 
																											m.ID_Compania = _ID_Compania and m.ID_Sucursal = _ID_Sucursal ;
																											
		
					--select * from _TMP_ASISTENCIAS;
					
					
					-- Ahora aplica los movimientos de nomina
					-- APLICA LAS ASISTENCIAS COMPLETAS ( -1 ) donde si HAYA ASISTIDO, Y NO existan permisos de vacaciones, incapacidad, pagos de tiempo y ni sea dia festivo
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, -1, tmp.Desde, tmp.Hasta, tmp.Entrada, tmp.Salida, tmp.Entrada2, tmp.Salida2, tmp.HNA, 0
					FROM _TMP_ASISTENCIAS tmp 
					WHERE tmp.Asistio = '1' and tmp.Entrada <= tmp.Desde and tmp.Salida >= tmp.Hasta
							and  (	select count(*) from TBL_NOM_PERMISOS_GRUPO
											where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and FechaHora_Desde <= _ID_FechaMovimiento and 
														FechaHora_Hasta > _ID_FechaMovimiento and ID_Movimiento = -20 and 
															tmp.ID_Empleado not in ( 	select ID_Empleado 
																												from TBL_NOM_PERMISOS_GRUPO_EXCLUSIONES
																												where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal	and 
																														ID_Movimiento = -20	and ID_FechaMovimiento = _ID_FechaMovimiento )  ) = 0
							and  ( 	select count(*) from TBL_NOM_PERMISOS 
											where ID_Empleado = tmp.ID_Empleado and FechaHora_Desde <= _ID_FechaMovimiento and FechaHora_Hasta > _ID_FechaMovimiento and 
																							( ID_Movimiento = -5 or ID_Movimiento = -7 or ID_Movimiento = -9 or ID_Movimiento = -11 or  ID_Movimiento = -12 or  ID_Movimiento = -13 ) ) = 0
							and  ( select count(*) from TBL_NOM_PERMISOS 
											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and 
																							( ID_Movimiento = -15 or ID_Movimiento = -16 or ID_Movimiento = -21 or ID_Movimiento = -22 ) ) = 0;
					
					
					-- APLICA LAS ASISTENCIAS PARCIALES ( -2 ) donde si HAYA ASISTIDO, Y NO existan permisos de vacaciones, incapacidad, pagos de tiempo y ni sea dia festivo
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, -2, tmp.Desde, tmp.Hasta, tmp.Entrada, tmp.Salida, tmp.Entrada2, tmp.Salida2, tmp.HNA, 
						 case 	when tmp.Indicador = 2 then
									case	when tmp.Entrada > tmp.Desde and tmp.Salida >= tmp.Hasta 
																		then round((cast(getfechadiff('minute', tmp.Desde, tmp.Entrada) as numeric) / 60),6)
											when tmp.Salida < tmp.Hasta and tmp.Entrada <= tmp.Desde
																		then round((cast(getfechadiff('minute', tmp.Salida, tmp.Hasta) as numeric) / 60),6)
											when tmp.Salida < tmp.Hasta and tmp.Entrada > tmp.Desde
																		then round((cast(getfechadiff('minute', tmp.Salida, tmp.Hasta) as numeric) / 60),6) +	round((cast(getfechadiff('minute', tmp.Desde, tmp.Entrada) as numeric) / 60),6)
											else 0	end
								when tmp.Indicador = 4 then
									case	when tmp.Entrada > tmp.Desde and tmp.Salida2 >= tmp.Hasta
																		then round((cast(getfechadiff('minute', tmp.Salida, tmp.Entrada2) as numeric) / 60),6) + round((cast(getfechadiff('minute', tmp.Desde, tmp.Entrada) as numeric) / 60),6)
											when tmp.Salida2 < tmp.Hasta and tmp.Entrada <= tmp.Desde
																		then round((cast(getfechadiff('minute', tmp.Salida, tmp.Entrada2) as numeric) / 60),6) + round((cast(getfechadiff('minute', tmp.Salida2, tmp.Hasta) as numeric) / 60),6)
											when tmp.Salida2 < tmp.Hasta and tmp.Entrada > tmp.Desde
																		then round((cast(getfechadiff('minute', tmp.Salida, tmp.Entrada2) as numeric) / 60),6) +
																					round((cast(getfechadiff('minute', tmp.Salida2, tmp.Hasta) as numeric) / 60),6) +
																					round((cast(getfechadiff('minute', tmp.Desde, tmp.Entrada) as numeric) / 60),6)
											else round((cast(getfechadiff('minute', tmp.Salida, tmp.Entrada2) as numeric) / 60),6) end
								else 0	end
					FROM _TMP_ASISTENCIAS tmp 
					WHERE tmp.Asistio = '1' and ( tmp.Entrada > tmp.Desde or tmp.Salida < tmp.Hasta )
							and  (	select count(*) from TBL_NOM_PERMISOS_GRUPO
											where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and FechaHora_Desde <= _ID_FechaMovimiento and 
														FechaHora_Hasta > _ID_FechaMovimiento and ID_Movimiento = -20 and 
															tmp.ID_Empleado not in ( 	select ID_Empleado 
																												from TBL_NOM_PERMISOS_GRUPO_EXCLUSIONES
																												where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal	and 
																														ID_Movimiento = -20	and ID_FechaMovimiento = _ID_FechaMovimiento ) ) = 0
							and  ( 	select count(*) from TBL_NOM_PERMISOS 
											where ID_Empleado = tmp.ID_Empleado and FechaHora_Desde <= _ID_FechaMovimiento and FechaHora_Hasta > _ID_FechaMovimiento and 
																							( ID_Movimiento = -5 or ID_Movimiento = -7 or ID_Movimiento = -9 or ID_Movimiento = -11 or  ID_Movimiento = -12 or  ID_Movimiento = -13 ) ) = 0
							and  ( select count(*) from TBL_NOM_PERMISOS 
											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and 
																							( ID_Movimiento = -15 or ID_Movimiento = -16 or ID_Movimiento = -21 or ID_Movimiento = -22 ) ) = 0;

																							
					-- APLICA LAS ASISTENCIAS COMPLETAS O PARCIALES ( TIEMPO POR PAGAR ENTRAR TARDE O SALIR ANTES ) ( -25 ) donde si HAYA ASISTIDO, su indicador sea SALIO Y NO existan permisos de vacaciones, incapacidad y ni sea dia festivo
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, -25, tmp.Desde, tmp.Hasta, tmp.Entrada, tmp.Salida,  tmp.Entrada2, tmp.Salida2,
						tmp.HNA, 
						case 	when tmp.Entrada > tmp.Desde
															then -- Se espera permiso de entrar tarde
																case 	when (select count(*) from TBL_NOM_PERMISOS where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) = 0 -- no existe el permiso aplica el retardo
																				then round((cast(getfechadiff('minute', tmp.Desde, tmp.Entrada) as numeric) / 60),6) -- aplica el retardo
																			else -- existe el permiso, ahora hay que verificar que aplique a todo el tiempo perdido
																				case	when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) <= tmp.Desde ) and 
																									 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) >= tmp.Entrada ) -- Aplica todo el permiso
																								then 0 -- 0 horas perdidas porque aplic? a todo el permiso
																							when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) <= tmp.Desde ) and 
																									 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) < tmp.Entrada ) -- Aplica parte del permiso ( llego mas tarde de lo que el permiso permitia )
																								then round((cast(getfechadiff('minute', (	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																																										where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ), tmp.Entrada ) as numeric) / 60),6)
																							else 0 

																				end	  
																end
													else 0
										end
										+
										case	when tmp.Salida < tmp.Hasta
															then -- Se espera permiso de salir temprano
																case 	when (select count(*) from TBL_NOM_PERMISOS where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) = 0 -- no existe el permiso aplica el salirse temprano
																				then round((cast(getfechadiff('minute', tmp.Salida, tmp.Hasta) as numeric) / 60),6) -- aplica la salida temprano
																			else -- existe el permiso, ahora hay que verificar que aplique a todo el tiempo perdido
																				case	when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) <= tmp.Salida ) and 
																									 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) >= tmp.Hasta ) -- Aplica todo el permiso
																								then 0 -- 0 horas perdidas porque aplic? a todo el permiso
																							when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) > tmp.Salida ) and 
																									 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) >= tmp.Hasta ) -- Aplica parte del permiso ( llego mas tarde de lo que el permiso permitia )
																								then round((cast(getfechadiff('minute', tmp.Salida, (	select FechaHora_Desde from TBL_NOM_PERMISOS 
																																																where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) ) as numeric) / 60),6)
																							else 0 
																				end	  
																end
													else 0
										end
												
					FROM _TMP_ASISTENCIAS tmp 
					WHERE tmp.Asistio = '1' and tmp.Indicador = 2
							and  ( select count(*) from TBL_NOM_PERMISOS 
											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and 
																							( ID_Movimiento = -15 or ID_Movimiento = -21 or ID_Movimiento = -22 ) ) <> 0 
							and  (	select count(*) from TBL_NOM_PERMISOS_GRUPO
											where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and FechaHora_Desde <= _ID_FechaMovimiento and 
														FechaHora_Hasta > _ID_FechaMovimiento and ID_Movimiento = -20 and 
															tmp.ID_Empleado not in ( 	select ID_Empleado 
																												from TBL_NOM_PERMISOS_GRUPO_EXCLUSIONES
																												where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal	and 
																														ID_Movimiento = -20	and ID_FechaMovimiento = _ID_FechaMovimiento ) ) = 0
							and  ( 	select count(*) from TBL_NOM_PERMISOS 
											where ID_Empleado = tmp.ID_Empleado and FechaHora_Desde <= _ID_FechaMovimiento and FechaHora_Hasta > _ID_FechaMovimiento and 
																							( ID_Movimiento = -5 or ID_Movimiento = -7 or ID_Movimiento = -9 or ID_Movimiento = -11 or  ID_Movimiento = -12 or  ID_Movimiento = -13  or  ID_Movimiento = -16 ) ) = 0;
				

					-- APLICA LAS ASISTENCIAS COMPLETAS O PARCIALES ( TIEMPO POR PAGAR ENTRAR / SALIR ) ( -25 ) donde si HAYA ASISTIDO, su indicador sea Salio por segunda ocasion, Y NO existan permisos de vacaciones, incapacidad y ni sea dia festivo
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, -25, tmp.Desde, tmp.Hasta, tmp.Entrada, tmp.Salida, tmp.Entrada2, tmp.Salida2,
						tmp.HNA, 
						case 	when (select count(*) from TBL_NOM_PERMISOS where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) = 0 -- no existe el permiso aplica el descuento del tiempo
														then round((cast(getfechadiff('minute', tmp.Salida, tmp.Entrada2) as numeric) / 60),6) -- aplica el descuento del tiempo
													else -- existe el permiso, ahora hay que verificar que aplique a todo el tiempo perdido
														case	when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																					where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) <= tmp.Salida ) and 
																			 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																					where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) >= tmp.Entrada2 ) -- Aplica todo el permiso
																		then 0 -- 0 horas perdidas porque aplic? a todo el permiso
																	when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																					where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) <= tmp.Salida ) and 
																			 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																					where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) < tmp.Entrada2 ) -- Aplica parte del permiso ( volvio a entrar mas tarde de lo que el permiso permitia )
																		then round((cast(getfechadiff('minute', ( select FechaHora_Hasta from TBL_NOM_PERMISOS 
																																				where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ), tmp.Entrada2 ) as numeric) / 60), 6) 
																	when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																					where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) > tmp.Salida ) and 
																			 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																					where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) >= tmp.Entrada2 ) -- Aplica parte del permiso ( se salio antes de lo que el permiso permitia )
																		then round((cast(getfechadiff('minute', tmp.Salida, ( select FechaHora_Desde from TBL_NOM_PERMISOS 
																																										where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) ) as numeric) / 60), 6) 
																	when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																					where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) > tmp.Salida ) and 
																			 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																					where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) < tmp.Entrada2 ) -- Aplica parte del permiso ( se salio antes de lo que el permiso permitia y volio a entrar despues de lo que el permiso permitia )
																		then round((cast(getfechadiff('minute', tmp.Salida, ( select FechaHora_Desde from TBL_NOM_PERMISOS 
																																										where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ) ) as numeric) / 60), 6) +
																				 round((cast(getfechadiff('minute', (	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																																				where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -15 ), tmp.Entrada2 ) as numeric) / 60), 6) 
																	else 0 

															end
											end
											
											+ 			-- este se suma al proceso de entrar tarde o salir temprano
											
											case 	when tmp.Entrada > tmp.Desde
															then -- Se espera permiso de entrar tarde
																case 	when (select count(*) from TBL_NOM_PERMISOS where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) = 0 -- no existe el permiso aplica el retardo
																				then round((cast(getfechadiff('minute', tmp.Desde, tmp.Entrada) as numeric) / 60),6) -- aplica el retardo
																			else -- existe el permiso, ahora hay que verificar que aplique a todo el tiempo perdido
																				case	when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) <= tmp.Desde ) and 
																									 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) >= tmp.Entrada ) -- Aplica todo el permiso
																								then 0 -- 0 horas perdidas porque aplic? a todo el permiso
																							when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) <= tmp.Desde ) and 
																									 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ) < tmp.Entrada ) -- Aplica parte del permiso ( llego mas tarde de lo que el permiso permitia )
																								then round((cast(getfechadiff('minute', (	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																																										where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -21 ), tmp.Entrada ) as numeric) / 60),6)
																							else 0 
																				end	  
																end
														else 0
											end
											
											+
										
											case	when tmp.Salida2 < tmp.Hasta
															then -- Se espera permiso de salir temprano
																case 	when (select count(*) from TBL_NOM_PERMISOS where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) = 0 -- no existe el permiso aplica el salirse temprano
																				then round((cast(getfechadiff('minute', tmp.Salida2, tmp.Hasta) as numeric) / 60),6) -- aplica la salida temprano
																			else -- existe el permiso, ahora hay que verificar que aplique a todo el tiempo perdido
																				case	when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) <= tmp.Salida2 ) and 
																									 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) >= tmp.Hasta ) -- Aplica todo el permiso
																								then 0 -- 0 horas perdidas porque aplic? a todo el permiso
																							when ((	select FechaHora_Desde from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) > tmp.Salida2 ) and 
																									 ((	select FechaHora_Hasta from TBL_NOM_PERMISOS 
																											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) >= tmp.Hasta ) -- Aplica parte del permiso ( llego mas tarde de lo que el permiso permitia )
																								then round((cast(getfechadiff('minute', tmp.Salida2, (select FechaHora_Desde from TBL_NOM_PERMISOS 
																																																where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and ID_Movimiento = -22 ) ) as numeric) / 60),6)
																							else 0 
																				end	  
																end
														else 0
											end
	

					FROM _TMP_ASISTENCIAS tmp 
					WHERE tmp.Asistio = '1' and tmp.Indicador = 4
							and  ( select count(*) from TBL_NOM_PERMISOS 
											where ID_Empleado = tmp.ID_Empleado and ID_FechaMovimiento = _ID_FechaMovimiento and 
																							( ID_Movimiento = -15 or ID_Movimiento = -21 or ID_Movimiento = -22 ) ) <> 0 
							and  (	select count(*) from TBL_NOM_PERMISOS_GRUPO
											where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and FechaHora_Desde <= _ID_FechaMovimiento and 
														FechaHora_Hasta > _ID_FechaMovimiento and ID_Movimiento = -20  and 
															tmp.ID_Empleado not in ( 	select ID_Empleado 
																												from TBL_NOM_PERMISOS_GRUPO_EXCLUSIONES
																												where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal	and 
																														ID_Movimiento = -20	and ID_FechaMovimiento = _ID_FechaMovimiento ) ) = 0
							and  ( 	select count(*) from TBL_NOM_PERMISOS 
											where ID_Empleado = tmp.ID_Empleado and FechaHora_Desde <= _ID_FechaMovimiento and FechaHora_Hasta > _ID_FechaMovimiento and 
																							( ID_Movimiento = -5 or ID_Movimiento = -7 or ID_Movimiento = -9 or ID_Movimiento = -11 or  ID_Movimiento = -12 or  ID_Movimiento = -13  or  ID_Movimiento = -16) ) = 0;
				






					
					--select * from TBL_NOM_PERMISOS where ID_FechaMovimiento = _ID_FechaMovimiento and ID_Empleado = 'AVIM02' and ( ID_Movimiento = -21 or ID_Movimiento = -22 or ID_Movimiento = -15 )
					
					-- AHORA APLICA LOS STATUS CORRESPONDIENTES DE LOS QUE NO CAPTURARON ASISTENCIA
					-- DEBE APLICAR FALTAS, DIAS NO LABORALES, INCAPACIDADES EN TODAS SUS MODALIDADES, VACACIONES y DIAS CON PAGO DE TIEMPO
					--
					-- APLICA LAS FALTAS ( -3 ) donde no sea dia no laborable ni tampoco existan permisos de vacaciones, incapacidad y dias con pago de tiempo, y ni sea dia festivo
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, -3, tmp.Desde, tmp.Hasta, null, null, null, null, tmp.HNA, tmp.HEA
					FROM _TMP_ASISTENCIAS tmp 
					WHERE tmp.Asistio = '0' and ( tmp.Desde is not null and tmp.Hasta is not null ) 
							and  (	select count(*) from TBL_NOM_PERMISOS_GRUPO
											where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and FechaHora_Desde <= _ID_FechaMovimiento and 
														FechaHora_Hasta > _ID_FechaMovimiento and ID_Movimiento = -20 ) = 0
							and  ( 	select count(*) from TBL_NOM_PERMISOS 
											where ID_Empleado = tmp.ID_Empleado and FechaHora_Desde <= _ID_FechaMovimiento and FechaHora_Hasta > _ID_FechaMovimiento and 
																							( ID_Movimiento = -5 or ID_Movimiento = -7 or ID_Movimiento = -9 or ID_Movimiento = -11 or  ID_Movimiento = -12 or  ID_Movimiento = -13  or ID_Movimiento = -16 ) ) = 0;


					-- APLICA LOS DIAS NO LABORALES ( -4 ) donde sea dia laborable y no existan permisos de vacaciones o incapacidad
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, -4, tmp.Desde, tmp.Hasta, null, null, null, null, tmp.HNA, 0 
					FROM _TMP_ASISTENCIAS tmp 
					WHERE tmp.Asistio = '0' and ( tmp.Desde is null and tmp.Hasta is null )
						and  ( 	select count(*) from TBL_NOM_PERMISOS 
										where ID_Empleado = tmp.ID_Empleado and FechaHora_Desde <= _ID_FechaMovimiento and FechaHora_Hasta > _ID_FechaMovimiento and 
																							( ID_Movimiento = -5 or ID_Movimiento = -7 or ID_Movimiento = -9 or ID_Movimiento = -11 or  ID_Movimiento = -12 or  ID_Movimiento = -13  or ID_Movimiento = -16  ) ) = 0;
	
					-- aplica los permisos de DIAS FESTIVOS ( PERMISOS DE GRUPO ) donde no sea dia no laborable ni tampoco existan permisos de vacaciones o incapacidad
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, p.ID_Movimiento, p.FechaHora_Desde, p.FechaHora_Hasta, null, null, null, null, tmp.HNA, 0
					FROM _TMP_ASISTENCIAS tmp INNER JOIN TBL_NOM_PERMISOS_GRUPO p ON
						p.ID_Compania = _ID_Compania and p.ID_Sucursal = _ID_Sucursal INNER JOIN TBL_NOM_MOVIMIENTOS m ON
						p.ID_Movimiento = m.ID_Movimiento
					WHERE tmp.Asistio = '0' and ( tmp.Desde is not null and tmp.Hasta is not null ) and
							(_ID_FechaMovimiento >= p.FechaHora_Desde and _ID_FechaMovimiento < p.FechaHora_Hasta )
								and m.ID_Movimiento = -20 and tmp.ID_Empleado not in ( 	select ID_Empleado 
															from TBL_NOM_PERMISOS_GRUPO_EXCLUSIONES
															where ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal	and 
																		ID_Movimiento = p.ID_Movimiento	and ID_FechaMovimiento = p.ID_FechaMovimiento ) 
								and  ( 	select count(*) from TBL_NOM_PERMISOS 
												where ID_Empleado = tmp.ID_Empleado and FechaHora_Desde <= _ID_FechaMovimiento and FechaHora_Hasta > _ID_FechaMovimiento and 
																							( ID_Movimiento = -5 or ID_Movimiento = -7 or ID_Movimiento = -9 or ID_Movimiento = -11 or  ID_Movimiento = -12 or  ID_Movimiento = -13 or ID_Movimiento = -16 ) ) = 0;

					-- AHORA APLICA PERMISOS DE DIAS CON PAGO DE TIEMPO donde no sea dia no laborable ni tampoco existan permisos de vacaciones o incapacidad
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, -25, p.FechaHora_Desde, p.FechaHora_Hasta, null, null, null, null, tmp.HNA, 0
					FROM _TMP_ASISTENCIAS tmp INNER JOIN TBL_NOM_PERMISOS p ON
						p.ID_Empleado = tmp.ID_Empleado INNER JOIN TBL_NOM_MOVIMIENTOS m ON
						p.ID_Movimiento = m.ID_Movimiento
					WHERE tmp.Asistio = '0' and ( tmp.Desde is not null and tmp.Hasta is not null ) and
							(_ID_FechaMovimiento >= p.FechaHora_Desde and _ID_FechaMovimiento < p.FechaHora_Hasta )
								and m.ID_Movimiento = -16 
								and  ( 	select count(*) from TBL_NOM_PERMISOS 
												where ID_Empleado = tmp.ID_Empleado and FechaHora_Desde <= _ID_FechaMovimiento and FechaHora_Hasta > _ID_FechaMovimiento and 
																							( ID_Movimiento = -5 or ID_Movimiento = -7 or ID_Movimiento = -9 or ID_Movimiento = -11 or  ID_Movimiento = -12 or  ID_Movimiento = -13 ) ) = 0;
		
					-- APLICA AHORA PERMISOS DE QUE ESTEN CAPTURADOS
					-- PRIMERO APLICA PERMISOS DE QUE REMPLAZAN FALTAS, DIAS NO LABORALES Y DIAS FESTIVOS. ESTOS SON LAS VACACIONES E INCAPACIDADES
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, p.ID_Movimiento, p.FechaHora_Desde, p.FechaHora_Hasta, tmp.Entrada, tmp.Salida, tmp.Entrada2, tmp.Salida2, tmp.HNA, tmp.HEA
									
					FROM _TMP_ASISTENCIAS tmp INNER JOIN TBL_NOM_PERMISOS p ON
						tmp.ID_Empleado = p.ID_Empleado INNER JOIN TBL_NOM_MOVIMIENTOS m ON
						p.ID_Movimiento = m.ID_Movimiento
					WHERE  _ID_FechaMovimiento >= p.FechaHora_Desde and _ID_FechaMovimiento < p.FechaHora_Hasta 
						and ( p.ID_Movimiento = -5 or p.ID_Movimiento = -7 or p.ID_Movimiento = -9 or p.ID_Movimiento = -11 or  p.ID_Movimiento = -12 or  p.ID_Movimiento = -13);	

					-- ahora empieza por aplicar las horas extras ...
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, p.ID_Movimiento, p.FechaHora_Desde, p.FechaHora_Hasta, tmp.Entrada, tmp.Salida, tmp.Entrada2, tmp.Salida2, 
						case 	when tmp.Hasta is not null then -- Dias laborable
														case 	when tmp.Indicador = 2 then	
																		case	when tmp.Salida > tmp.Hasta then 
																						case	when p.FechaHora_Hasta <= tmp.Salida  
																										then round((cast(getfechadiff('minute', p.FechaHora_Desde, p.FechaHora_Hasta) as numeric) / 60),6)
																									when p.FechaHora_Hasta > tmp.Salida and p.FechaHora_Desde < tmp.Salida
																										then round((cast(getfechadiff('minute', p.FechaHora_Desde, tmp.Salida) as numeric) / 60),6)
																									else 0 
																						end	  
																					else 0 
																		end
																	when tmp.Indicador = 4 then
																		case	when tmp.Salida2 > tmp.Hasta then 
																						case	when p.FechaHora_Hasta <= tmp.Salida2  
																										then round((cast(getfechadiff('minute', p.FechaHora_Desde, p.FechaHora_Hasta) as numeric) / 60),6)
																									when p.FechaHora_Hasta > tmp.Salida2 and p.FechaHora_Desde < tmp.Salida2
																										then round((cast(getfechadiff('minute', p.FechaHora_Desde, tmp.Salida2) as numeric) / 60),6)
																									else 0 
																						end	  
																					else 0 
																		end
																	else 0
														end
													else -- dia no laborable
														case 	when tmp.Indicador = 2 then	
																		case 	when p.FechaHora_Hasta <= tmp.Entrada or p.FechaHora_Desde >= tmp.Salida 
																						then 0
																					when p.FechaHora_Hasta > tmp.Salida or p.FechaHora_Desde < tmp.Entrada
																						then round((cast(getfechadiff('minute', tmp.Entrada, tmp.Salida) as numeric) / 60),6)
																					when p.FechaHora_Desde >= tmp.Entrada and p.FechaHora_Hasta <= tmp.Salida 
																						then round((cast(getfechadiff('minute', p.FechaHora_Desde, p.FechaHora_Hasta) as numeric) / 60),6)
																					when p.FechaHora_Desde >= tmp.Entrada and p.FechaHora_Hasta > tmp.Salida 
																						then round((cast(getfechadiff('minute', p.FechaHora_Desde, tmp.Salida) as numeric) / 60),6)
																					when p.FechaHora_Desde < tmp.Entrada and p.FechaHora_Hasta <= tmp.Salida 
																						then round((cast(getfechadiff('minute', tmp.Entrada, p.FechaHora_Hasta) as numeric) / 60),6)
																		else 0 end
																	when tmp.Indicador = 4 then
																		case 	when p.FechaHora_Hasta <= tmp.Entrada or p.FechaHora_Desde >= tmp.Salida2 
																						then 0
																					when p.FechaHora_Hasta > tmp.Salida2 or p.FechaHora_Desde < tmp.Entrada
																						then round((cast(getfechadiff('minute', tmp.Entrada, tmp.Salida2) as numeric) / 60),6)
																					when p.FechaHora_Desde >= tmp.Entrada and p.FechaHora_Hasta <= tmp.Salida2 
																						then round((cast(getfechadiff('minute', p.FechaHora_Desde, p.FechaHora_Hasta) as numeric) / 60),6)
																					when p.FechaHora_Desde >= tmp.Entrada and p.FechaHora_Hasta > tmp.Salida2 
																						then round((cast(getfechadiff('minute', p.FechaHora_Desde, tmp.Salida2) as numeric) / 60),6)
																					when p.FechaHora_Desde < tmp.Entrada and p.FechaHora_Hasta <= tmp.Salida2 
																						then round((cast(getfechadiff('minute', tmp.Entrada, p.FechaHora_Hasta) as numeric) / 60),6)
																		else 0 end

																	else 0
														end	
										end, 0
					FROM _TMP_ASISTENCIAS tmp INNER JOIN TBL_NOM_PERMISOS p ON
						tmp.ID_Empleado = p.ID_Empleado INNER JOIN TBL_NOM_MOVIMIENTOS m ON
						p.ID_Movimiento = m.ID_Movimiento
					WHERE tmp.Asistio = '1' and p.ID_FechaMovimiento = _ID_FechaMovimiento
						and m.ID_Movimiento = -14;

					-- ahora aplica las horas pagadas ...
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, p.ID_Movimiento, p.FechaHora_Desde, p.FechaHora_Hasta, tmp.Entrada, tmp.Salida, tmp.Entrada2, tmp.Salida2, 
						case 	when tmp.Hasta is not null then -- Dias laborable
														case 	when tmp.Indicador = 2 then	
																		case	when tmp.Salida > tmp.Hasta then 
																						case	when p.FechaHora_Hasta <= tmp.Salida  
																										then round((cast(getfechadiff('minute', p.FechaHora_Desde, p.FechaHora_Hasta) as numeric) / 60),6)
																									when p.FechaHora_Hasta > tmp.Salida and p.FechaHora_Desde < tmp.Salida
																										then round((cast(getfechadiff('minute', p.FechaHora_Desde, tmp.Salida) as numeric) / 60),6)
																									else 0 
																						end	  
																					else 0 
																		end
																	when tmp.Indicador = 4 then
																		case	when tmp.Salida2 > tmp.Hasta then 
																						case	when p.FechaHora_Hasta <= tmp.Salida2  
																										then round((cast(getfechadiff('minute', p.FechaHora_Desde, p.FechaHora_Hasta) as numeric) / 60),6)
																									when p.FechaHora_Hasta > tmp.Salida2 and p.FechaHora_Desde < tmp.Salida2
																										then round((cast(getfechadiff('minute', p.FechaHora_Desde, tmp.Salida2) as numeric) / 60),6)
																									else 0 
																						end	  
																					else 0 
																		end
																	else 0
														end
													else -- dia no laborable
														case 	when tmp.Indicador = 2 then	
																		case 	when p.FechaHora_Hasta <= tmp.Entrada or p.FechaHora_Desde >= tmp.Salida 
																						then 0
																					when p.FechaHora_Hasta > tmp.Salida or p.FechaHora_Desde < tmp.Entrada
																						then round((cast(getfechadiff('minute', tmp.Entrada, tmp.Salida) as numeric) / 60),6)
																					when p.FechaHora_Desde >= tmp.Entrada and p.FechaHora_Hasta <= tmp.Salida 
																						then round((cast(getfechadiff('minute', p.FechaHora_Desde, p.FechaHora_Hasta) as numeric) / 60),6)
																					when p.FechaHora_Desde >= tmp.Entrada and p.FechaHora_Hasta > tmp.Salida 
																						then round((cast(getfechadiff('minute', p.FechaHora_Desde, tmp.Salida) as numeric) / 60),6)
																					when p.FechaHora_Desde < tmp.Entrada and p.FechaHora_Hasta <= tmp.Salida 
																						then round((cast(getfechadiff('minute', tmp.Entrada, p.FechaHora_Hasta) as numeric) / 60),6)
																		else 0 end
																	when tmp.Indicador = 4 then
																		case 	when p.FechaHora_Hasta <= tmp.Entrada or p.FechaHora_Desde >= tmp.Salida2 
																						then 0
																					when p.FechaHora_Hasta > tmp.Salida2 or p.FechaHora_Desde < tmp.Entrada
																						then round((cast(getfechadiff('minute', tmp.Entrada, tmp.Salida2) as numeric) / 60),6)
																					when p.FechaHora_Desde >= tmp.Entrada and p.FechaHora_Hasta <= tmp.Salida2 
																						then round((cast(getfechadiff('minute', p.FechaHora_Desde, p.FechaHora_Hasta) as numeric) / 60),6)
																					when p.FechaHora_Desde >= tmp.Entrada and p.FechaHora_Hasta > tmp.Salida2 
																						then round((cast(getfechadiff('minute', p.FechaHora_Desde, tmp.Salida2) as numeric) / 60),6)
																					when p.FechaHora_Desde < tmp.Entrada and p.FechaHora_Hasta <= tmp.Salida2 
																						then round((cast(getfechadiff('minute', tmp.Entrada, p.FechaHora_Hasta) as numeric) / 60),6)
																		else 0 end

																	else 0
														end	
										end, 0
					FROM _TMP_ASISTENCIAS tmp INNER JOIN TBL_NOM_PERMISOS p ON
						tmp.ID_Empleado = p.ID_Empleado INNER JOIN TBL_NOM_MOVIMIENTOS m ON
						p.ID_Movimiento = m.ID_Movimiento
					WHERE tmp.Asistio = '1' and p.ID_FechaMovimiento = _ID_FechaMovimiento
						and ( m.ID_Movimiento = -19 or m.ID_Movimiento = -30 or m.ID_Movimiento = -31 or m.ID_Movimiento = -32 );
				

 					-- aplica los permisos ( salir-entrar, entrar tarde, salir antes) SIN PAGO de tiempo 
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, p.ID_Movimiento, p.FechaHora_Desde, p.FechaHora_Hasta, tmp.Entrada, tmp.Salida, tmp.Entrada2, tmp.Salida2,
									round((cast(getfechadiff('minute', p.FechaHora_Desde, p.FechaHora_Hasta) as numeric) / 60),6),
									0
					FROM _TMP_ASISTENCIAS tmp INNER JOIN TBL_NOM_PERMISOS p ON
						tmp.ID_Empleado = p.ID_Empleado INNER JOIN TBL_NOM_MOVIMIENTOS m ON
						p.ID_Movimiento = m.ID_Movimiento
					WHERE tmp.Asistio = '1' and p.ID_FechaMovimiento = _ID_FechaMovimiento
						and ( m.ID_Movimiento = -17 OR m.ID_Movimiento = -23 OR m.ID_Movimiento = -24 );
				
					-- aplica los permisos de DIAS SIN PAGO de tiempo 
					INSERT INTO TBL_NOM_DIARIO_DET
					SELECT _ID_Compania, _ID_Sucursal, _ID_FechaMovimiento, tmp.ID_Empleado, p.ID_Movimiento, p.FechaHora_Desde, p.FechaHora_Hasta, tmp.Entrada, tmp.Salida, tmp.Entrada2, tmp.Salida2, 
									tmp.HNA,
									0
					FROM _TMP_ASISTENCIAS tmp INNER JOIN TBL_NOM_PERMISOS p ON
						tmp.ID_Empleado = p.ID_Empleado INNER JOIN TBL_NOM_MOVIMIENTOS m ON
						p.ID_Movimiento = m.ID_Movimiento
					WHERE tmp.Asistio = '0' and (_ID_FechaMovimiento >= p.FechaHora_Desde and _ID_FechaMovimiento < p.FechaHora_Hasta )
						and m.ID_Movimiento = -18;
		
					-- Por ultimo borra la tabla temporal de asistencias					
					TRUNCATE TABLE _TMP_ASISTENCIAS;
		
					--SELECT d.ID_Empleado, d.ID_FechaMovimiento, d.ID_Movimiento, m.Descripcion, d.Desde, d.Hasta, d.Entrada, d.Salida, d.HNA, d.HNP
					--FROM TBL_NOM_DIARIO_DET d INNER JOIN TBL_NOM_MOVIMIENTOS m ON
					--	d.ID_Movimiento = m.ID_Movimiento 
					--WHERE (ID_Empleado = 'ALRM02' or ID_Empleado = 'AMGE02') and ID_Compania = _ID_Compania and 
					--		ID_Sucursal = _ID_Sucursal and 
					--		ID_FechaMovimiento = _ID_FechaMovimiento
			
			END IF;
						
			_cont := _cont + 1;

		END LOOP;-- fin de while

		--RETURN QUERY SELECT * FROM  _TMP_ASISTENCIAS;
		DROP TABLE _TMP_ASISTENCIAS;
	
	END IF;

	RETURN QUERY SELECT _err, _result, _Compania_Sucursal;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_diario_cierre(character varying, timestamp without time zone, timestamp without time zone)
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_nom_permisos_agregar(character, smallint, timestamp without time zone, bit, timestamp without time zone, timestamp without time zone, smallint, numeric, numeric, character varying);

CREATE OR REPLACE FUNCTION sp_nom_permisos_agregar(
    _id_empleado character,
    _id_movimiento smallint,
    _id_fechamovimiento timestamp without time zone,
    _diascompletos bit,
    _fechahora_desde timestamp without time zone,
    _fechahora_hasta timestamp without time zone,
    _num_de_dias smallint,
    _num_de_horas numeric,
    _tiempo_por_pagar numeric,
    _obs character varying,
    _prmgrp bit,
    _id_sucursal smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_Sistema smallint; _ID_Registro varchar(255); 
	_ID_Fecha timestamp; _ID_HoraDesde timestamp; _ID_HoraHasta timestamp;
	_Dif smallint;
BEGIN
	_err := 0;
	
	if _prmgrp = '0' --No es de grupo
	then
		_result := 'El permiso de empleado se agregó satisfactoriamente';
		_ID_Registro := _ID_Empleado::varchar || '_' || cast(_ID_Movimiento as varchar) || '_' || date_part('day', _ID_FechaMovimiento)::varchar || '/' || date_part('month', _ID_FechaMovimiento)::varchar || '/' || date_part('year', _ID_FechaMovimiento)::varchar; 
	else
		_result := 'El permiso de grupo se agregó satisfactoriamente';
		_ID_Registro := 'FSINOMINA-' || cast(_ID_Sucursal as varchar) || '_' || cast(_ID_Movimiento as varchar) || '_' || date_part('day', _ID_FechaMovimiento)::varchar || '/' || date_part('month', _ID_FechaMovimiento)::varchar || '/' || date_part('year', _ID_FechaMovimiento)::varchar; 
	end if;
	
	_ID_Sistema := ( select ID_Sistema from TBL_NOM_MOVIMIENTOS where ID_Movimiento = _ID_Movimiento );

	IF _prmgrp = '0' --No es de grupo
	THEN
		-- si ya existe este permiso marcar? error
		IF _DiasCompletos = '1' -- revisa por los de dias completos, es decir revisa solo los que la fecha de movimiento sean igual a la de movimiento y no los que se insertan uno a uno cuando son de horas y no de dias completos, pero se especifico hasta una fecha con dias despues
		THEN
			IF(select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado 
							and ID_Movimiento = _ID_Movimiento and ID_FechaMovimiento = _ID_FechaMovimiento) > 0
			THEN
				_err := 3;
				_result := 'ERROR: Ya existe la clave del movimiento con la fecha especific a da y para este empleado ';
			END IF;
			-- si ya existe este permiso de sistema marcar? error
			IF(select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado 
							and ID_Movimiento = _ID_Sistema and ID_FechaMovimiento = _ID_FechaMovimiento) > 0
			THEN
				_err := 3;
				_result := 'ERROR: Ya existe este permiso. Pudo generarse en un cierre de dia, o estar  vinculado a este mismo ID de sistema ';		
			END IF;
		
			IF(( ( select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado and 
				ID_Movimiento = _ID_Sistema and 
				_ID_FechaMovimiento >= FechaHora_Desde and 
				_ID_FechaMovimiento < FechaHora_Hasta  ) > 0 ) OR (
				( select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado and 
				ID_Movimiento = _ID_Sistema  and 
				_FechaHora_Hasta > FechaHora_Desde and 
				_FechaHora_Hasta <= FechaHora_Hasta  ) > 0 ) 	OR	(
				( select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado and 
				ID_Movimiento = _ID_Sistema and 
				ID_FechaMovimiento >= _ID_FechaMovimiento and  
				ID_FechaMovimiento < _FechaHora_Hasta ) > 0 ))
			THEN
				_err := 1;
				_result := 'ERROR: Este permiso se intercala con otro. No se puede insertar este permiso porque se duplicaría en algun cierre de dia.';
			END IF;
		ELSE
			-- Revisa que los id fecha movimiento en el rango en el que estan desde ID_FechaMovimiento hasta ID_FechaHora_Hasta
			_ID_Fecha := _ID_FechaMovimiento;
			
			WHILE _ID_Fecha <= _FechaHora_Hasta
			LOOP
				IF(select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado 
							and ID_Movimiento = _ID_Movimiento and ID_FechaMovimiento = _ID_Fecha) > 0
				THEN
					_err := 3;
					_result := 'ERROR: Ya existe la clave del movimiento con esta fecha para este empleado ';
					EXIT;
				END IF;
				-- si ya existe este permiso de sistema marcar? error
				IF(select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado 
							and ID_Movimiento = _ID_Sistema and ID_FechaMovimiento = _ID_Fecha) > 0
				THEN
					_err := 3;
					_result :=  'ERROR: Ya existe este permiso. Pudo generarse en un cierre de dia, o estar  vinculado a este mismo ID de sistema ';
					EXIT;
				END IF;

				_ID_Fecha = _ID_Fecha + '1 day'::interval;
			END LOOP;

		END IF;
	ELSE -- es permiso de grupo
		IF(select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal 
						and ID_Movimiento = _ID_Movimiento and ID_FechaMovimiento = _ID_FechaMovimiento) > 0
		THEN
			_err := 3;
			_result := 'ERROR: Ya existe la clave del movimiento de grupo con la fecha especific a da y para esta nómina';
		END IF;
		-- si ya existe este permiso de sistema marcar? error
		IF(select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal 
						and ID_Movimiento = _ID_Sistema and ID_FechaMovimiento = _ID_FechaMovimiento) > 0
		THEN
			_err := 3;
			_result := 'ERROR: Ya existe este permiso de grupo. Pudo generarse en un cierre de dia, o estar  vinculado a este mismo ID de sistema ';		
		END IF;
		
		IF(( ( select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and 
				ID_Movimiento = _ID_Sistema and 
				_ID_FechaMovimiento >= FechaHora_Desde and 
				_ID_FechaMovimiento < FechaHora_Hasta  ) > 0 ) OR (
				( select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and 
				ID_Movimiento = _ID_Sistema  and 
				_FechaHora_Hasta > FechaHora_Desde and 
				_FechaHora_Hasta <= FechaHora_Hasta  ) > 0 ) 	OR	(
				( select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and 
				ID_Movimiento = _ID_Sistema and 
				ID_FechaMovimiento >= _ID_FechaMovimiento and  
				ID_FechaMovimiento < _FechaHora_Hasta ) > 0 ))
		THEN
			_err := 3;
			_result := 'ERROR: Este permiso de grupo se intercala con otro. No se puede insertar este permiso porque se duplicaría en algun cierre de dia.';
		END IF;
	END IF;
	
	IF _err = 0
	THEN
		IF _prmgrp = '0' -- No es permiso de grupo
		THEN
			IF _DiasCompletos = '1'
			THEN
				INSERT INTO TBL_NOM_PERMISOS
				VALUES( _ID_Empleado, _ID_Movimiento, _ID_FechaMovimiento, _DiasCompletos, _FechaHora_Desde, 
							_FechaHora_Hasta, _Num_de_Dias, _Num_de_Horas, _Tiempo_por_Pagar, _Obs );
							
				INSERT INTO TBL_NOM_PERMISOS
				VALUES( _ID_Empleado, _ID_Sistema, _ID_FechaMovimiento, _DiasCompletos, _FechaHora_Desde, 
							_FechaHora_Hasta, _Num_de_Dias, _Num_de_Horas, _Tiempo_por_Pagar, _Obs );
			ELSE
				_ID_Fecha := _ID_FechaMovimiento;
				_ID_HoraDesde := _FechaHora_Desde;
				_ID_HoraHasta := _FechaHora_Hasta;
				--_Dif = DATEDIFF(day,_ID_FechaMovimiento,_FechaHora_Hasta)
				_Dif := DATE_PART('day', _FechaHora_Hasta - _ID_FechaMovimiento); 
				--_ID_HoraHasta = DATEADD(day, (-_Dif), _ID_HoraHasta)
				_ID_HoraHasta := _ID_HoraHasta - (_Dif::varchar || ' day')::interval;
						
				WHILE _ID_Fecha <= _FechaHora_Hasta
				LOOP
					INSERT INTO TBL_NOM_PERMISOS
					VALUES( _ID_Empleado, _ID_Movimiento, _ID_Fecha, _DiasCompletos, _ID_HoraDesde, 
								_ID_HoraHasta, 0, _Num_de_Horas, _Num_de_Horas, _Obs  );
					
					INSERT INTO TBL_NOM_PERMISOS
					VALUES( _ID_Empleado, _ID_Sistema, _ID_Fecha, _DiasCompletos, _ID_HoraDesde, 
								_ID_HoraHasta, 0, _Num_de_Horas, _Num_de_Horas, _Obs  );
					
					_ID_Fecha := _ID_Fecha + '1 day'::interval;
					_ID_HoraDesde := _ID_HoraDesde + '1 day'::interval;
					_ID_HoraHasta := _ID_HoraHasta + '1 day'::interval;

				END LOOP;

			END IF;
		ELSE -- Es de grupo
			INSERT INTO TBL_NOM_PERMISOS_GRUPO
			VALUES( 0, _ID_Sucursal, _ID_Movimiento, _ID_FechaMovimiento, _DiasCompletos, _FechaHora_Desde, 
							_FechaHora_Hasta, _Num_de_Dias, _Num_de_Horas, _Tiempo_por_Pagar );
							
			INSERT INTO TBL_NOM_PERMISOS_GRUPO
			VALUES( 0, _ID_Sucursal, _ID_Sistema, _ID_FechaMovimiento, _DiasCompletos, _FechaHora_Desde, 
							_FechaHora_Hasta, _Num_de_Dias, _Num_de_Horas, _Tiempo_por_Pagar );
		END IF;
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_registro;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_permisos_agregar(character, smallint, timestamp without time zone, bit, timestamp without time zone, timestamp without time zone, smallint, numeric, numeric, character varying, bit, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_nom_permisos_grupo AS 
 SELECT p.id_compania, p.id_sucursal, com.descripcion AS scompania_sucursal, p.id_movimiento, p.id_fechamovimiento, m.descripcion, p.fechahora_desde AS desde, 
        CASE
            WHEN p.diascompletos = B'1'::"bit" THEN p.fechahora_hasta - '1 day'::interval
            ELSE p.fechahora_hasta
        END AS hasta, p.diascompletos, p.num_de_dias, p.num_de_horas, p.tiempo_por_pagar
   FROM tbl_nom_permisos_grupo p
   JOIN tbl_nom_movimientos m ON p.id_movimiento = m.id_movimiento
   JOIN tbl_companias com ON p.id_compania = com.id_compania AND p.id_sucursal = com.id_sucursal
  WHERE p.id_movimiento > 0;

ALTER TABLE view_nom_permisos_grupo
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_nom_permisos_eliminar(character, smallint, timestamp without time zone);

CREATE OR REPLACE FUNCTION sp_nom_permisos_eliminar(
    _id_empleado character,
    _id_movimiento smallint,
    _id_fechamovimiento timestamp without time zone,
    _prmgrp bit,
    _id_sucursal smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_Sistema smallint; _ID_Registro varchar(255); 
BEGIN
	_err := 0;

	if _prmgrp = '0' --No es de grupo
	then
		_result := 'El permiso de empleado se eliminó de la base de datos';
		_ID_Registro := _ID_Empleado::varchar || '_' || cast(_ID_Movimiento as varchar) || '_' || date_part('day', _ID_FechaMovimiento)::varchar || '-' || date_part('month', _ID_FechaMovimiento)::varchar || '-' || date_part('year', _ID_FechaMovimiento)::varchar; 
	else
		_result := 'El permiso de grupo se eliminó de la base de datos';
		_ID_Registro := 'FSINOMINA-' || cast(_ID_Sucursal as varchar) || '_' || cast(_ID_Movimiento as varchar) || '_' || date_part('day', _ID_FechaMovimiento)::varchar || '/' || date_part('month', _ID_FechaMovimiento)::varchar || '/' || date_part('year', _ID_FechaMovimiento)::varchar; 
	end if;
	
	_ID_Sistema := ( select ID_Sistema from TBL_NOM_MOVIMIENTOS where ID_Movimiento = _ID_Movimiento );
	
	-- si no existe este permiso marcar? error
	IF _prmgrp = '0'
	THEN
		IF(select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado 
							and ID_Movimiento = _ID_Movimiento and ID_FechaMovimiento = _ID_FechaMovimiento) < 1
		THEN
			_err := 3;
			_result := 'ERROR: No existe la clave del movimiento con esta fecha para este empleado. No se puede eliminar';
		END IF;
	ELSE -- es de grupo
		IF(select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal 
							and ID_Movimiento = _ID_Movimiento and ID_FechaMovimiento = _ID_FechaMovimiento) < 1
		THEN
			_err := 3;
			_result := 'ERROR: No existe la clave del movimiento con esta fecha para este grupo. No se puede eliminar';
		END IF;
	END IF;
	
	IF _err = 0
	THEN
		IF _prmgrp = '0'
		THEN
			DELETE FROM TBL_NOM_PERMISOS
			WHERE ID_Empleado = _ID_Empleado and 
					ID_Movimiento = _ID_Movimiento and 
					ID_FechaMovimiento = _ID_FechaMovimiento;

			DELETE FROM TBL_NOM_PERMISOS
			WHERE ID_Empleado = _ID_Empleado and 
					ID_Movimiento = _ID_Sistema and 
					ID_FechaMovimiento = _ID_FechaMovimiento;
		ELSE
			DELETE FROM TBL_NOM_PERMISOS_GRUPO
			WHERE ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and 
					ID_Movimiento = _ID_Movimiento and 
					ID_FechaMovimiento = _ID_FechaMovimiento;

			DELETE FROM TBL_NOM_PERMISOS_GRUPO
			WHERE ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and 
					ID_Movimiento = _ID_Sistema and 
					ID_FechaMovimiento = _ID_FechaMovimiento;
		END IF;
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_registro;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_permisos_eliminar(character, smallint, timestamp without time zone, bit, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP FUNCTION sp_nom_permisos_cambiar(character, smallint, timestamp without time zone, bit, timestamp without time zone, timestamp without time zone, smallint, numeric, numeric, character varying);

CREATE OR REPLACE FUNCTION sp_nom_permisos_cambiar(
    _id_empleado character,
    _id_movimiento smallint,
    _id_fechamovimiento timestamp without time zone,
    _diascompletos bit,
    _fechahora_desde timestamp without time zone,
    _fechahora_hasta timestamp without time zone,
    _num_de_dias smallint,
    _num_de_horas numeric,
    _tiempo_por_pagar numeric,
    _obs character varying,
    _prmgrp bit,
    _id_sucursal smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255); _ID_Sistema smallint; _ID_Registro varchar(255); 
BEGIN
	_err := 0;

	if _prmgrp = '0' --No es de grupo
	then
		_result := 'El permiso de empleado se cambió satisfactoriamente';
		_ID_Registro := _ID_Empleado::varchar || '_' || cast(_ID_Movimiento as varchar) || '_' || date_part('day', _ID_FechaMovimiento)::varchar || '/' || date_part('month', _ID_FechaMovimiento)::varchar || '/' || date_part('year', _ID_FechaMovimiento)::varchar; 
	else
		_result := 'El permiso de grupo se cambió satisfactoriamente';
		_ID_Registro := 'FSINOMINA-' || cast(_ID_Sucursal as varchar) || '_' || cast(_ID_Movimiento as varchar) || '_' || date_part('day', _ID_FechaMovimiento)::varchar || '/' || date_part('month', _ID_FechaMovimiento)::varchar || '/' || date_part('year', _ID_FechaMovimiento)::varchar; 
	end if;
	
	_ID_Sistema := ( select ID_Sistema from TBL_NOM_MOVIMIENTOS where ID_Movimiento = _ID_Movimiento );

	IF _prmgrp = '0' --No es de grupo
	THEN
		-- si no existe este permiso marcar? error
		IF(select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado 
							and ID_Movimiento = _ID_Movimiento and ID_FechaMovimiento = _ID_FechaMovimiento) < 1
		THEN
			_err := 3;
			_result := 'ERROR: No existe la clave del movimiento con esta fecha para este empleado. No se puede cambiar';
		END IF;
		
		IF(select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado 
							and ID_Movimiento = _ID_Sistema and ID_FechaMovimiento = _ID_FechaMovimiento) < 1
		THEN
			_err := 3;
			_result := 'ERROR: No existe la concordancia del ID de sistema con esta fecha para este empleado. No se puede cambiar ';
		END IF;

		IF _DiasCompletos = '1' --Si es permiso de dias completos, verifica que no se empalmen las fechas
		THEN
			IF(( ( select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado and 
				ID_Movimiento = _ID_Sistema and ID_FechaMovimiento <> _ID_FechaMovimiento and 
				_ID_FechaMovimiento >= FechaHora_Desde and 
				_ID_FechaMovimiento < FechaHora_Hasta  ) > 0 ) OR (
				( select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado and 
				ID_Movimiento = _ID_Sistema  and ID_FechaMovimiento <> _ID_FechaMovimiento and 
				_FechaHora_Hasta > FechaHora_Desde and 
				_FechaHora_Hasta <= FechaHora_Hasta  ) > 0 ) 	OR	(
				( select count(*) from TBL_NOM_PERMISOS where ID_Empleado = _ID_Empleado and 
				ID_Movimiento = _ID_Sistema and ID_FechaMovimiento <> _ID_FechaMovimiento and 
				ID_FechaMovimiento >= _ID_FechaMovimiento and  
				ID_FechaMovimiento < _FechaHora_Hasta ) > 0 ))
			THEN
				_err := 3;
				_result := 'ERROR: Este permiso se intercala con otro. No se puede cambiar este permiso porque se duplicaría en algun cierre de dia.';
			END IF;
		END IF;
	ELSE -- Es permiso de grupo
		IF(select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal 
						and ID_Movimiento = _ID_Movimiento and ID_FechaMovimiento = _ID_FechaMovimiento) < 1
		THEN
			_err := 3;
			_result := 'ERROR: No existe la clave del movimiento de grupo con la fecha especific a da y para esta nómina';
		END IF;
		-- si no existe este permiso de sistema marcar? error
		IF(select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal 
						and ID_Movimiento = _ID_Sistema and ID_FechaMovimiento = _ID_FechaMovimiento) < 1
		THEN
			_err := 3;
			_result := 'ERROR: No existe la concordancia del ID de sistema con esta fecha para esta nómina. No se puede cambiar ';		
		END IF;
		
		IF(( ( select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and 
				ID_Movimiento = _ID_Sistema and ID_FechaMovimiento <> _ID_FechaMovimiento and 
				_ID_FechaMovimiento >= FechaHora_Desde and 
				_ID_FechaMovimiento < FechaHora_Hasta  ) > 0 ) OR (
				( select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and 
				ID_Movimiento = _ID_Sistema  and ID_FechaMovimiento <> _ID_FechaMovimiento and 
				_FechaHora_Hasta > FechaHora_Desde and 
				_FechaHora_Hasta <= FechaHora_Hasta  ) > 0 ) 	OR	(
				( select count(*) from TBL_NOM_PERMISOS_GRUPO where ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and 
				ID_Movimiento = _ID_Sistema and ID_FechaMovimiento <> _ID_FechaMovimiento and 
				ID_FechaMovimiento >= _ID_FechaMovimiento and  
				ID_FechaMovimiento < _FechaHora_Hasta ) > 0 ))
			THEN
				_err := 3;
				_result := 'ERROR: Este permiso se intercala con otro. No se puede cambiar este permiso porque se duplicaría en algun cierre de dia.';
			END IF;
	END IF;
	
	IF _err = 0
	THEN
		IF _prmgrp = '0' -- No es permiso de grupo
		THEN
			UPDATE TBL_NOM_PERMISOS
			SET DiasCompletos = _DiasCompletos, FechaHora_Desde = _FechaHora_Desde, FechaHora_Hasta = _FechaHora_Hasta, 
					Num_de_Dias = _Num_de_Dias, Num_de_Horas = _Num_de_Horas, Tiempo_por_Pagar = _Tiempo_por_Pagar, Obs = _Obs 
			WHERE ID_Empleado = _ID_Empleado and 
						ID_Movimiento = _ID_Movimiento and 
						ID_FechaMovimiento = _ID_FechaMovimiento;
			
			UPDATE TBL_NOM_PERMISOS
			SET DiasCompletos = _DiasCompletos, FechaHora_Desde = _FechaHora_Desde, FechaHora_Hasta = _FechaHora_Hasta, 
					Num_de_Dias = _Num_de_Dias, Num_de_Horas = _Num_de_Horas, Tiempo_por_Pagar = _Tiempo_por_Pagar, Obs = _Obs 
			WHERE ID_Empleado = _ID_Empleado and 
						ID_Movimiento = _ID_Sistema and 
						ID_FechaMovimiento = _ID_FechaMovimiento;
		ELSE
			UPDATE TBL_NOM_PERMISOS_GRUPO
			SET DiasCompletos = _DiasCompletos, FechaHora_Desde = _FechaHora_Desde, FechaHora_Hasta = _FechaHora_Hasta, 
					Num_de_Dias = _Num_de_Dias, Num_de_Horas = _Num_de_Horas, Tiempo_por_Pagar = _Tiempo_por_Pagar 
			WHERE ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and 
						ID_Movimiento = _ID_Movimiento and 
						ID_FechaMovimiento = _ID_FechaMovimiento;
			
			UPDATE TBL_NOM_PERMISOS_GRUPO
			SET DiasCompletos = _DiasCompletos, FechaHora_Desde = _FechaHora_Desde, FechaHora_Hasta = _FechaHora_Hasta, 
					Num_de_Dias = _Num_de_Dias, Num_de_Horas = _Num_de_Horas, Tiempo_por_Pagar = _Tiempo_por_Pagar 
			WHERE ID_Compania = 0 and ID_Sucursal = _ID_Sucursal and  
						ID_Movimiento = _ID_Sistema and 
						ID_FechaMovimiento = _ID_FechaMovimiento;
		END IF;
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_registro;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_permisos_cambiar(character, smallint, timestamp without time zone, bit, timestamp without time zone, timestamp without time zone, smallint, numeric, numeric, character varying, bit, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
DROP VIEW view_nom_plantillas_modulo;

CREATE OR REPLACE VIEW view_nom_plantillas_modulo AS 
 SELECT p.id_plantilla, p.bid_empleado, p.bnomina, p.btipo_nomina, p.bcompania_sucursal, p.bnivel_confianza, 
        CASE
            WHEN p.bcompania_sucursal = B'0'::"bit" AND p.bid_empleado = B'0'::"bit" THEN ''::character varying
            WHEN p.bcompania_sucursal = B'1'::"bit" AND p.bid_empleado = B'0'::"bit" THEN ( SELECT tbl_companias.descripcion
               FROM tbl_companias
              WHERE tbl_companias.id_compania = p.id_compania AND tbl_companias.id_sucursal = p.id_sucursal)
            WHEN p.bcompania_sucursal = B'0'::"bit" AND p.bid_empleado = B'1'::"bit" THEN ( SELECT sc.descripcion
               FROM tbl_companias sc
          JOIN tbl_nom_masemp sm ON sc.id_compania = sm.id_compania AND sc.id_sucursal = sm.id_sucursal
         WHERE sm.id_empleado = p.id_empleado)
            ELSE ''::character varying
        END AS compania, p.fecha, p.id_movimiento, 
        CASE
            WHEN p.bid_empleado = B'1'::"bit" THEN p.id_empleado
            ELSE ''::bpchar
        END AS id_empleado, m.descripcion AS movimiento, p.descripcion, (((((
        CASE
            WHEN p.btipo_nomina = B'1'::"bit" THEN '  TIPO DE NOMINA: '::text || (( SELECT 
                    CASE
                        WHEN p.tipo_de_nomina = 1 OR p.tipo_de_nomina = 2 OR p.tipo_de_nomina = 12 THEN '<b>NORMAL</b> '::text
                        WHEN p.tipo_de_nomina = 3 OR p.tipo_de_nomina = 4 OR p.tipo_de_nomina = 34 THEN '<b>ESPECIAL</b> '::text
                        WHEN p.tipo_de_nomina = 5 OR p.tipo_de_nomina = 6 OR p.tipo_de_nomina = 56 THEN '<b>AGUINALDO</b> '::text
                        WHEN p.tipo_de_nomina = 7 OR p.tipo_de_nomina = 8 OR p.tipo_de_nomina = 78 THEN '<b>VALES</b> '::text
                        ELSE '<b>DESCONOCIDA</b> '::text
                    END AS "case"))
            ELSE ''::text
        END || 
        CASE
            WHEN p.bid_empleado = B'1'::"bit" THEN '  EMPLEADO: <b>'::text || (( SELECT ((((tbl_nom_masemp.nombre::text || ' '::text) || tbl_nom_masemp.apellido_paterno::text) || ' '::text) || tbl_nom_masemp.apellido_materno::text) || '</b>'::text
               FROM tbl_nom_masemp
              WHERE tbl_nom_masemp.id_empleado = p.id_empleado))
            ELSE ''::text
        END) || 
        CASE
            WHEN p.bnomina = B'1'::"bit" THEN ((('  NUMERO: <b>'::text || p.numero_nomina::character varying::text) || ' '::text) || p.ano::character varying::text) || '</b>'::text
            ELSE ''::text
        END) || 
        CASE
            WHEN p.bcompania_sucursal = B'1'::"bit" THEN '  NOMINA: <b>'::text || (( SELECT tbl_companias.descripcion::text || '</b>'::text
               FROM tbl_companias
              WHERE tbl_companias.id_compania = p.id_compania AND tbl_companias.id_sucursal = p.id_sucursal))
            ELSE ''::text
        END) || 
        CASE
            WHEN p.bnivel_confianza = B'1'::"bit" THEN '  TIPO DE EMPLEADOS: '::text || (( SELECT 
                    CASE
                        WHEN p.nivel_de_confianza = 1 THEN '<b>SINDICALIZADOS</b>'::text
                        ELSE '<b>NO SINDICALIZADOS</b>'::text
                    END AS "case"))
            ELSE ''::text
        END) || 
        CASE
            WHEN p.aplicacion = 0 THEN ('  HORAS: <b>'::text || p.horas::character varying::text) || '</b>'::text
            WHEN p.aplicacion = 1 THEN (('  DIAS DE SALARIO: <b>'::text || p.dias::character varying::text) || 
            CASE
                WHEN p.mixto = B'1'::"bit" THEN ' MIXTO '::text
                ELSE ''::text
            END) || '</b>'::text
            ELSE ('  IMPORTE: <b>'::text || p.importe::character varying::text) || '</b>'::text
        END) || 
        CASE
            WHEN p.bexento = B'1'::"bit" THEN ('  EXENTO: <b>'::text || p.exento::character varying::text) || '</b>'::text
            ELSE ''::text
        END AS aplicacion, p.calcular
   FROM tbl_nom_plantillas p
   JOIN tbl_nom_movimientos_nomina m ON p.id_movimiento = m.id_movimiento;

ALTER TABLE view_nom_plantillas_modulo
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_calculo_nomina_fonacot(
    _id_empleado character,
    _fecha_hasta timestamp without time zone,
    _periodo smallint,
    _fonaj smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_FONAC numeric(19,4); _TMP record;
	_ID_Credito varchar(10); _Neto numeric(19,4); 
BEGIN 

	--raise notice 'Calculo del fonacot';
						
	CREATE LOCAL TEMPORARY TABLE _TMP_FONACOT (
		ID_Num serial NOT NULL , 
		ID_Credito varchar(10) NOT NULL ,
		Descuento numeric(19,4) NOT NULL ,
		Total numeric(19,4) NOT NULL ,
		Pagos numeric(19,4) NOT NULL 
	);

	insert into _TMP_FONACOT (ID_Credito,Descuento,Total,Pagos)
	select c.ID_Credito,  round((c.Retencion / 30) * _Periodo,1), c.Importe,
		(select case when sum(Descuento) is null 
					then 0 
					else sum(Descuento) 
				end 
		from TBL_NOM_FONACOT_DET 
		where ID_Credito = c.ID_Credito and Fecha < _Fecha_Hasta) 
	from TBL_NOM_FONACOT_CAB c 
	where c.ID_Empleado = _ID_Empleado and c.Fecha <= _Fecha_Hasta  and
		( c.Importe > (	select case when sum(Descuento) is null then 0 else sum(Descuento) end 
											from TBL_NOM_FONACOT_DET 
											where ID_Credito = c.ID_Credito and Fecha < _Fecha_Hasta) );


	_FONAC := (	select sum( (case when Total - Pagos > Descuento then Descuento else Total - Pagos end ) ) 
					from _TMP_FONACOT );

	for _TMP in (select * from _TMP_FONACOT)
	loop
		_ID_Credito := _TMP.ID_Credito;
		_Neto :=  case when _TMP.Total - _TMP.Pagos > _TMP.Descuento then _TMP.Descuento else _TMP.Total - _TMP.Pagos end;

		raise notice 'FonAJ: %, Credito: %,  Total %, Pagos %, Descuento %, Neto %', _FonAj, _TMP.ID_Credito, _TMP.Total, _TMP.Pagos, _TMP.Descuento, _Neto;
											
		if(select count(*) from TBL_NOM_FONACOT_DET where ID_Credito = _ID_Credito and Fecha = _Fecha_Hasta) > 0
		then -- Existe ya
			update TBL_NOM_FONACOT_DET
			set Descuento = _Neto
			where ID_Credito = _ID_Credito and Fecha = _Fecha_Hasta;
		else
			if _Neto > 0.00 --Solo agregará el descuento cuando realmente se necesite descontar
			then
				insert into TBL_NOM_FONACOT_DET
				select _ID_Credito, _Fecha_Hasta, _Neto;
			end if;
		end if;
	end loop;

	drop table _TMP_FONACOT;

	if _FONAC is null
	then
		_FONAC := 0.0;
	end if;

	RETURN QUERY SELECT _FONAC;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_calculo_nomina_fonacot(character, timestamp without time zone, smallint, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_calculo_nomina_dinamicos(
    _id_empleado character,
    _id_compania smallint,
    _id_sucursal smallint,
    _ano smallint,
    _numero_nomina smallint,
    _tipo smallint,
    _nivel_de_confianza smallint,
    _salario_por_hora numeric,
    _salario_diario numeric,
    _salario_mixto numeric,
    _calculomixto bit)
  RETURNS void AS
$BODY$
BEGIN
	-- CALCULA LAS DE EMPLEADO TAL
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
												when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
												when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
												when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
												when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
												when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '1' and ID_Empleado = _ID_Empleado and 
		bNomina = '0' and bCompania_Sucursal = '0' and bNivel_Confianza = '0';
	
	-- CALCULA LAS DE EMPLEADO TAL SOLO NOMINA XX
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '1' and ID_Empleado = _ID_Empleado and 
		bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and bCompania_Sucursal = '0' and bNivel_Confianza = '0';
	
	--CALCULA LAS DE NOMINA XX 
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '0' and bNivel_Confianza = '0'
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '0' and bNivel_Confianza = '0'
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	--CALCULA LAS DE NOMINA XX COMPANIA_SUCURSAL XX 
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and 
		bNivel_Confianza = '0'
		and  p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and 
		bNivel_Confianza = '0'
		and p.Inclusiones = '1' and  _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	--CALCULA LAS DE NOMINA XX COMPANIA_SUCURSAL XX NO SINDICALIZADOS
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and 
		bNivel_Confianza = '1' and Nivel_de_Confianza = 0 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and 
		bNivel_Confianza = '1' and Nivel_de_Confianza = 0 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	--CALCULA LAS DE NOMINA XX COMPANIA_SUCURSAL XX SINDICALIZADOS
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round(_Salario_Diario * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and 
		bNivel_Confianza = '1' and Nivel_de_Confianza = 1 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round(_Salario_Diario * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal and 
		bNivel_Confianza = '1' and Nivel_de_Confianza = 1 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	--CALCULA LAS DE NOMINA XX NO SINDICALIZADOS
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK EMPIEZA AQUI 
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '0' and 
		bNivel_Confianza = '1' and Nivel_de_Confianza = 0 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK EMPIEZA AQUI 
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '0' and 
		bNivel_Confianza = '1' and Nivel_de_Confianza = 0 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	--CALCULA LAS DE NOMINA XX SINDICALIZADOS
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '0' and 
		bNivel_Confianza = '1' and Nivel_de_Confianza = 1 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES -- OK
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '1' and Ano = _Ano and Numero_Nomina = _Numero_Nomina and 
		bCompania_Sucursal = '0' and 
		bNivel_Confianza = '1' and Nivel_de_Confianza = 1 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	--CALCULA LAS DE COMPANIA_SUCURSAL XX 
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal
		and bNivel_Confianza = '0'
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal
		and bNivel_Confianza = '0'
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	--CALCULA LAS DE COMPANIA_SUCURSAL XX NO_SINDICALIZADOS
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal
		and bNivel_Confianza = '1' and Nivel_de_Confianza = 0 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal
		and bNivel_Confianza = '1' and Nivel_de_Confianza = 0 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	--CALCULA LAS DE COMPANIA_SUCURSAL XX SINDICALIZADOS
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal
		and bNivel_Confianza = '1' and Nivel_de_Confianza = 1 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '1' and ID_Compania = _ID_Compania and ID_Sucursal = _ID_Sucursal
		and bNivel_Confianza = '1' and Nivel_de_Confianza = 1 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	-- CALCULO TODO NO SINDICALIZADO
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '0'
		and bNivel_Confianza = '1' and Nivel_de_Confianza = 0 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '0'
		and bNivel_Confianza = '1' and Nivel_de_Confianza = 0 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	--CALCULA TODO SINDICALIZADO
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '0'
		and bNivel_Confianza = '1' and Nivel_de_Confianza = 1 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '0' and _ID_Empleado not in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
	INSERT INTO _TMP_PLANTILLAS_TOTALES
	select _Tipo, _ID_Empleado, p.ID_Movimiento,  
	/*TOTAL_GRAVADO*/ case when Deduccion = '1' or ISPT = '0' then 0.00 else
										case 	when	Aplicacion = 0 and bExento = '0' then round(_Salario_por_Hora * Horas, 2)
													when 	Aplicacion = 1 and bExento = '0' then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
													when 	Aplicacion = 2 and bExento = '0' then (Veces_Importe * Importe)
													when	Aplicacion = 0 and bExento = '1' then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then round(_Salario_por_Hora * Horas, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 1 and bExento = '1' then case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) - Exento
																																					else 0.00 end
													when 	Aplicacion = 2 and bExento = '1' then case	when (Veces_Importe * Importe) > Exento then (Veces_Importe * Importe) - Exento 
																																					else 0.00 end
											else 0.00 end end,
	/*TOTAL_EXENTO*/ 	case 	when Deduccion = '1' or (ISPT = '1' and bExento = '0') then 0.00 
								when Deduccion = '0' and ISPT = '0' then -- todo lo mandará al exento (ignora si hay un importe máximo exento o no)
									case 	when Aplicacion = 0 then round(_Salario_por_Hora * Horas, 2)
											when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
											when Aplicacion = 2 then (Veces_Importe * Importe) end  
								else -- Es percepción que Incluye un importe máximo exento...... La parte gravada ya quedo en lo gravado en la columna anterior

									case 	when	Aplicacion = 0 	then	case	when round(_Salario_por_Hora * Horas, 2) > Exento then Exento
																					else round(_Salario_por_Hora * Horas, 2) end
											when 	Aplicacion = 1 	then 	case	when round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) > Exento then Exento
																					else round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2) end
											when 	Aplicacion = 2 	then 	case	when (Veces_Importe * Importe) > Exento then Exento
																					else (Veces_Importe * Importe) end
											else 0.00 
									end
							 	end,
	/*TOTAL_DEDUCCION*/ case 	when Deduccion = '0' then	0.00
													 	else	case 	when Aplicacion = 0	then round(_Salario_por_Hora * Horas, 2)
																				when Aplicacion = 1 then round((case when Mixto = '0' then _Salario_Diario else (case when _CalculoMixto = '0' then _Salario_Diario else _Salario_Mixto end) end) * Dias, 2)
																				when Aplicacion = 2 then (Veces_Importe * Importe) end  
														end, mn.ISPT 
	from TBL_NOM_PLANTILLAS p inner join TBL_NOM_MOVIMIENTOS_NOMINA mn ON p.ID_Movimiento = mn.ID_Movimiento 
	where Calcular = '1' and Tipo_de_Nomina = _Tipo and bID_Empleado = '0' and bNomina = '0'
		and bCompania_Sucursal = '0'
		and bNivel_Confianza = '1' and Nivel_de_Confianza = 1 and Nivel_de_Confianza = _Nivel_de_Confianza 
		and p.Inclusiones = '1' and _ID_Empleado in ( select ID_Empleado from TBL_NOM_PLANTILLAS_EXCLUSIONES where ID_Plantilla = p.ID_Plantilla );
	
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_calculo_nomina_dinamicos(character, smallint, smallint, smallint, smallint, smallint, smallint, numeric, numeric, numeric, bit)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_nom_diario_cierre AS 
 SELECT com.descripcion AS compania, dd.id_compania, dd.id_sucursal, dd.id_fechamovimiento, dd.id_empleado, (((m.nombre::text || ' '::text) || m.apellido_paterno::text) || ' '::text) || m.apellido_materno::text AS nombre, dd.id_movimiento, mov.descripcion, 
        CASE
            WHEN date_part('hour'::text, dd.desde) = 0::double precision AND date_part('minute'::text, dd.desde) = 0::double precision THEN NULL::timestamp without time zone
            ELSE dd.desde
        END AS desde, 
        CASE
            WHEN date_part('hour'::text, dd.hasta) = 0::double precision AND date_part('minute'::text, dd.hasta) = 0::double precision THEN NULL::timestamp without time zone
            ELSE dd.hasta
        END AS hasta, dd.entrada, dd.salida, dd.hna, dd.hnp, dd.entrada2, dd.salida2
   FROM tbl_nom_diario_det dd
   JOIN tbl_nom_masemp m ON dd.id_empleado = m.id_empleado
   JOIN tbl_nom_movimientos mov ON dd.id_movimiento = mov.id_movimiento
   JOIN tbl_companias com ON dd.id_compania = com.id_compania AND dd.id_sucursal = com.id_sucursal;

ALTER TABLE view_nom_diario_cierre
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_nom_nominas_modulo AS 
 SELECT cn.id_nomina, cn.id_compania, cn.id_sucursal, com.descripcion AS compania, cn.ano, cn.tipo, 
        CASE
            WHEN cn.tipo = 1 OR cn.tipo = 2 THEN 'Normal'::text
            WHEN cn.tipo = 3 OR cn.tipo = 4 THEN 'Especial'::text
            WHEN cn.tipo = 5 OR cn.tipo = 6 THEN 'Aguinaldo'::text
            WHEN cn.tipo = 7 OR cn.tipo = 8 THEN 'Vales'::text
            ELSE 'OTRA'::text
        END AS tipo_nomina, cn.numero_nomina, cn.fecha_desde, cn.fecha_hasta, cn.dias, cn.cerrado, 
        CASE
            WHEN cn.cerrado = B'0'::"bit" THEN 'ABIERTA'::text
            ELSE 'PROTEGIDA'::text
        END AS proteccion, cn.status, cn.id_pol, ''::text AS pol, cn.formapago, cn.id_mov, 
        CASE
            WHEN cn.formapago = 'N'::bpchar THEN ''::text
            ELSE (((( SELECT tbl_bancos_cuentas.cuenta
               FROM tbl_bancos_cuentas
              WHERE tbl_bancos_cuentas.tipo = (( SELECT tbl_bancos_movimientos.tipo
                       FROM tbl_bancos_movimientos
                      WHERE tbl_bancos_movimientos.id = cn.id_mov)) AND tbl_bancos_cuentas.clave = (( SELECT tbl_bancos_movimientos.clave
                       FROM tbl_bancos_movimientos
                      WHERE tbl_bancos_movimientos.id = cn.id_mov))))::text) || ' '::text) || ((( SELECT tbl_bancos_movimientos.num
               FROM tbl_bancos_movimientos
              WHERE tbl_bancos_movimientos.id = cn.id_mov))::text)
        END AS pago
   FROM tbl_nom_calculo_nomina cn
   JOIN tbl_companias com ON cn.id_compania = com.id_compania AND cn.id_sucursal = com.id_sucursal;

ALTER TABLE view_nom_nominas_modulo
  OWNER TO [[owner]];

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
		--RAISE NOTICE 'TOTAL BAN: %',  ( select SUM(Debe)  from _TMP_CONT_POLIZAS_DETALLE2 );
		if _TotalBAN is null or _TotalBAN <= 0.0
		then 
			_err = 3;
			_result := 'ERROR: Al parecer esta nómina no contiene ningun recibo o el total del pago de la nómina es un importe de cero o un importe negativo. No se puede generar el pago de esta nómina.'; 
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
	SELECT _TOTAL_HE, _TOTAL_HF, _TOTAL_HD, TOTAL_HT, _DiasHorasExtras;
	
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_calculo_nomina_horas_extras(smallint, character, timestamp without time zone, timestamp without time zone, bit, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
ALTER TABLE tbl_invserv_costos_conceptos
DROP CONSTRAINT ck_tbl_invserv_costos_conceptos_tipo,
ADD CONSTRAINT ck_tbl_invserv_costos_conceptos_tipo CHECK (tipo = 'ENT'::bpchar OR tipo = 'SAL'::bpchar OR tipo = 'MIX'::bpchar);

--@FIN_BLOQUE
INSERT INTO tbl_invserv_costos_conceptos(id_concepto, descripcion, desistema, recalcularcosto, tipo, cc)
VALUES (-1, 'sistema. utensilios entradas', '1', '0', 'MIX', null);

INSERT INTO tbl_invserv_costos_conceptos(id_concepto, descripcion, desistema, recalcularcosto, tipo, cc)
VALUES (-2, 'sistema. utensilios salidas', '1', '0', 'MIX', null);

--@FIN_BLOQUE
INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ALM_UTENSILIOS');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ALM_UTENSILIOS_ENTRADA');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ALM_UTENSILIOS_SALIDA');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ALM_UTENSILIOS_CANCELAR');

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
        END AS pol, m.ref
   FROM tbl_invserv_almacen_movim_cab m
   JOIN tbl_invserv_costos_conceptos c ON m.id_concepto = c.id_concepto
   JOIN tbl_invserv_bodegas b ON m.id_bodega = b.id_bodega;

ALTER TABLE view_invserv_almacen_movim_modulo
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_invserv_costos_conceptos AS 
 SELECT e.id_concepto, 
        CASE
            WHEN e.desistema = B'1'::"bit" THEN ( SELECT m.msj1
               FROM tbl_msj m
              WHERE m.alc::text = 'CEF'::text AND m.mod::text = 'ALM_ENLACES'::text AND m.sub::text = 'CATALOGO'::text AND m.elm::text = e.id_concepto::text)
            ELSE e.descripcion
        END AS descripcion, e.desistema, e.recalcularcosto, e.tipo, e.cc
   FROM tbl_invserv_costos_conceptos e
  WHERE e.id_concepto >= 0;

ALTER TABLE view_invserv_costos_conceptos
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalog_gastos_utensilios AS 
 SELECT tbl_invserv_inventarios.id_prod AS clave, tbl_invserv_inventarios.descripcion, tbl_invserv_inventarios.id_linea AS especial
   FROM tbl_invserv_inventarios
  WHERE tbl_invserv_inventarios.id_tipo = 'G'::bpchar AND tbl_invserv_inventarios.nosevende = B'1'::"bit";

ALTER TABLE view_catalog_gastos_utensilios
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_invserv_alm_utensilios_agregar(
    _fecha timestamp without time zone,
    _id_bodega smallint,
    _status character,
    _id_concepto smallint,
    _concepto character varying,
    _reference character varying,
    _tipomov character varying,
    _claseref character varying,
    cr_pri character,
    cr_sec integer)
  RETURNS SETOF record AS
$BODY$
DECLARE  
	_errpart int; _resultpart varchar(255); _err int; _result varchar(255); _mes smallint; _ano smallint; _ID_Movimiento int; _Numero int; _Ref varchar(25);  
	--Iteracion
	_REC_INVSERV_ALMACEN_MOVIM_DETFIN RECORD; _contPart smallint;
	--De costos
	_prod varchar(20); _cantdet numeric(9,3); _exist numeric(9,3); _existBod numeric(9,3); _descripcion varchar(80);
	_entrada numeric(9,3); _salida numeric(9,3); 
	_existAcum numeric(9,3); _existAcumBod numeric(9,3);  
BEGIN
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_MOVIM' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'El movimiento al almac?n se registr? correctamente';
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	_Numero := (SELECT Numero FROM TBL_INVSERV_BODEGAS WHERE ID_Bodega = _ID_Bodega);
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF (select count(*) from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Bodega = _ID_Bodega and Numero = _Numero) > 0
	THEN
		_err = 3;
		_result = (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_MOVIM' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: La clave del movimiento a este almacen ya existe, No se puede duplicar'
	END IF;

	IF _err = 0
	THEN	
		-- procede a crear la tabla temporal de tarjetas de almacen
		CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_COSTOS_DETALLE (
			ID_Prod varchar(20) NOT NULL ,
			Parcial numeric(9, 3) NOT NULL ,
			Entrada numeric(9, 3) NOT NULL ,
			Salida numeric(9, 3) NOT NULL ,
			Existencia numeric(9, 3) NOT NULL ,
			ExistBod numeric(9, 3) NOT NULL ,
			UltimoCosto numeric(19,4) NOT NULL ,
			CostoPromedio numeric(19,4) NOT NULL ,
			Debe numeric(19,4) NOT NULL ,
			Haber numeric(19,4) NOT NULL ,
			Saldo numeric(19,4) NOT NULL , 
			Partida smallint NOT NULL
		);
		-- crea la tabla temporal final de movimientos a partir de la tabla principal
		CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DETFIN (
			Partida serial NOT NULL ,
			ID_Prod varchar(20) NOT NULL ,
			Cantidad numeric(9, 3) NOT NULL ,
			Costo numeric(19,4) NULL 
		);
		-- Fin de creacion de tablas

		INSERT INTO TBL_INVSERV_ALMACEN_MOVIM_CAB
		VALUES(default, _Fecha, _ID_Bodega, _Numero, _Status, _ID_Concepto, _Concepto, _Reference, -1, _claseref, cr_pri, cr_sec)
		RETURNING currval(pg_get_serial_sequence('TBL_INVSERV_ALMACEN_MOVIM_CAB', 'id_movimiento')) INTO _ID_Movimiento;
		
		UPDATE TBL_INVSERV_BODEGAS
		SET Numero = _Numero + 1
		WHERE ID_Bodega = _ID_Bodega;						
			
		_Ref := 'UALM|' || cast(_ID_Movimiento as varchar) || '|' || cast(_ID_Bodega as varchar) || '||';

		-- Aqui empieza la forma de actuar con la poliza. 
		-- procede a agregar en la tarjeta de almacen y a actualizar el producto
		INSERT INTO _TMP_INVSERV_ALMACEN_MOVIM_DETFIN(ID_Prod, Cantidad, Costo)
		SELECT ID_Prod, Cantidad, Costo
		FROM _TMP_INVSERV_ALMACEN_MOVIM_DET;
		--GROUP BY ID_Prod;

		_contPart := 1;

		FOR _REC_INVSERV_ALMACEN_MOVIM_DETFIN IN ( select * from _TMP_INVSERV_ALMACEN_MOVIM_DETFIN ) 
		LOOP
			_prod := _REC_INVSERV_ALMACEN_MOVIM_DETFIN.ID_Prod;
			_cantdet := _REC_INVSERV_ALMACEN_MOVIM_DETFIN.Cantidad;
			_descripcion := (select Descripcion from TBL_INVSERV_INVENTARIOS where ID_Prod = _prod);
			_exist := (select CantidadAcum from TBL_INVSERV_INVENTARIOS where ID_Prod = _prod);
			_existBod := (select Existencia from TBL_INVSERV_EXISTENCIAS where ID_Prod = _prod and ID_Bodega = _ID_Bodega);
			_entrada := 0.000;
			_salida := 0.000;
			
			IF _TipoMov = 'ENTRADA'
			THEN
				_entrada := _cantdet;
				_existAcum := _exist + _cantdet; 
				_existAcumBod := _existBod + _cantdet;
			ELSE -- _TipoMov = 'SALIDAS'
				_salida := _cantdet;
				_existAcum := _exist - _cantdet; 
				_existAcumBod := _existBod - _cantdet;
			END IF;

			INSERT INTO _TMP_INVSERV_COSTOS_DETALLE
			VALUES(_prod, _cantdet, _entrada, _salida, _existAcum, _existAcumBod, 0.00, 0.00, 0.00, 0.00, 0.00, _contPart);
			
			UPDATE TBL_INVSERV_INVENTARIOS
			SET CantidadAcum = _existAcum 
			WHERE ID_Prod = _prod;
				
			_contPart := _contPart + 1;
				
		END LOOP;
	
		-- Procede a registrar los costos
		IF _err = 0
		THEN
			INSERT INTO TBL_INVSERV_COSTOS_DETALLE(id_movimiento,fecha,id_prod,id_concepto,status,parcial,entrada,salida,existencia,existbod,ultimocosto,costopromedio,debe,haber,saldo,ref)
			SELECT _ID_Movimiento, _Fecha, ID_Prod, _ID_Concepto, _Status, Parcial, Entrada, Salida, Existencia, ExistBod, UltimoCosto, CostoPromedio, Debe, Haber, Saldo, _Ref || cast(Partida as varchar)
			FROM _TMP_INVSERV_COSTOS_DETALLE;
		END IF;

		-- Procede a actualizar las existencias por bodega
		IF _err = 0
		THEN
			--RAISE NOTICE 'ACUALIZACION EXISTENCIAS: %, %, %.', _TipoMov, _claseref, _AuditarAlm;
			IF _TipoMov = 'ENTRADA' 
			THEN
				UPDATE TBL_INVSERV_EXISTENCIAS
				SET Existencia = e.Existencia + ( 	
									SELECT SUM(Entrada)
									FROM _TMP_INVSERV_COSTOS_DETALLE
									WHERE ID_Prod = e.ID_Prod
									GROUP BY ID_Prod 	)
				FROM TBL_INVSERV_EXISTENCIAS e, _TMP_INVSERV_COSTOS_DETALLE tm 
				WHERE e.ID_Prod = tm.ID_Prod AND e.ID_Bodega = _ID_Bodega 
					AND e.ID_Prod = TBL_INVSERV_EXISTENCIAS.ID_Prod
					AND e.ID_Bodega = TBL_INVSERV_EXISTENCIAS.ID_Bodega;
					
			ELSE -- _TipoMov = 'SALIDA'
				UPDATE TBL_INVSERV_EXISTENCIAS
				SET Existencia = e.Existencia - ( 	
							SELECT SUM(Salida) 
							FROM _TMP_INVSERV_COSTOS_DETALLE 
							WHERE ID_Prod = e.ID_Prod
							GROUP BY ID_Prod 	)
				FROM TBL_INVSERV_EXISTENCIAS e, _TMP_INVSERV_COSTOS_DETALLE tm 
				WHERE e.ID_Prod = tm.ID_Prod AND e.ID_Bodega = _ID_Bodega
					AND e.ID_Prod = TBL_INVSERV_EXISTENCIAS.ID_Prod
					AND e.ID_Bodega = TBL_INVSERV_EXISTENCIAS.ID_Bodega;
				
			END IF;
		END IF;
		
		DROP TABLE _TMP_INVSERV_COSTOS_DETALLE;
		DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DETFIN;
		
	END IF;
	
	RETURN QUERY SELECT _err, _result, _ID_Movimiento;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_invserv_alm_utensilios_agregar(timestamp without time zone, smallint, character, smallint, character varying, character varying, character varying, character varying, character, integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_invserv_alm_utensilios_cancelar(_id_movimiento integer)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_Numero int; _Fecha timestamp; _err int; _result varchar(255); _errpart int; _resultpart varchar(255); _Ref varchar(25);
	_mes smallint; _ano smallint; _ID_Bodega smallint; _ID_Concepto smallint;  
BEGIN
	_err := 0;
	_result := (select msj5 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_MOVIM' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'El movimiento se canceló satisfactoriamente';
	_Fecha := (select Fecha from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Movimiento = _ID_Movimiento);
	_ID_Bodega := (select ID_Bodega from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Movimiento = _ID_Movimiento);
	_Ref := (select Ref from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Movimiento = _ID_Movimiento);
	_ID_Concepto := ( select ID_Concepto from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Movimiento = _ID_Movimiento );
	
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_INVSERV_ALMACEN_MOVIM_CAB
		SET Status = 'C'
		WHERE ID_Movimiento = _ID_Movimiento;
		
		-- procede a cancelar los detalles de costos
		UPDATE TBL_INVSERV_COSTOS_DETALLE
		SET Status = 'C' 
		WHERE ID_Movimiento = _ID_Movimiento;
		
		-- Procede a actualizar las existencias en bodegas
		--RAISE NOTICE 'ACUALIZACION EXISTENCIAS: %, %, %.', _TipoMov, _Ref, _AuditarAlm;

		IF _ID_Concepto = -1 -- Es entrada 
		THEN
			UPDATE TBL_INVSERV_EXISTENCIAS
			SET Existencia = e.Existencia - ( 	
									SELECT SUM(Entrada)
									FROM TBL_INVSERV_COSTOS_DETALLE 
									WHERE ID_Prod = e.ID_Prod AND ID_Movimiento = _ID_Movimiento
									GROUP BY ID_Prod )
			FROM TBL_INVSERV_EXISTENCIAS e, TBL_INVSERV_COSTOS_DETALLE dg 
			WHERE e.ID_Prod = dg.ID_Prod AND dg.ID_Movimiento = _ID_Movimiento AND e.ID_Bodega = _ID_Bodega
					AND e.ID_Prod = TBL_INVSERV_EXISTENCIAS.ID_Prod
					AND e.ID_Bodega = TBL_INVSERV_EXISTENCIAS.ID_Bodega;
								
			
		ELSE --Es salida
			UPDATE TBL_INVSERV_EXISTENCIAS
			SET Existencia = e.Existencia + ( 	
								SELECT SUM(Salida)
								FROM TBL_INVSERV_COSTOS_DETALLE 
								WHERE ID_Prod = e.ID_Prod AND ID_Movimiento = _ID_Movimiento
								GROUP BY ID_Prod )
			FROM TBL_INVSERV_EXISTENCIAS e, TBL_INVSERV_COSTOS_DETALLE dg 
			WHERE e.ID_Prod = dg.ID_Prod AND dg.ID_Movimiento = _ID_Movimiento AND e.ID_Bodega = _ID_Bodega
				AND e.ID_Prod = TBL_INVSERV_EXISTENCIAS.ID_Prod
				AND e.ID_Bodega = TBL_INVSERV_EXISTENCIAS.ID_Bodega;
				
		END IF;
		
	END IF;
		
	RETURN QUERY SELECT _err, _result, _ID_Movimiento;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_invserv_alm_utensilios_cancelar(integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_invserv_almacen_movim_detalles_utensilios_modulo AS 
 SELECT d.id_costo, d.id_movimiento, c.numero AS num, c.id_concepto, 
        CASE
            WHEN c.id_concepto = (-1) THEN 'Entrada'::text
            ELSE 'Salida'::text
        END AS tipo, c.fecha, c.concepto, c.status, c.ref, c.id_bodega, d.id_prod, p.descripcion, d.entrada, d.salida, p.id_unidadsalida AS unidad
   FROM tbl_invserv_costos_detalle d
   JOIN tbl_invserv_inventarios p ON d.id_prod::text = p.id_prod::text
   JOIN tbl_invserv_almacen_movim_cab c ON d.id_movimiento = c.id_movimiento
   JOIN tbl_invserv_bodegas b ON c.id_bodega = b.id_bodega
  WHERE c.id_concepto = (-1) OR c.id_concepto = (-2);

ALTER TABLE view_invserv_almacen_movim_detalles_utensilios_modulo
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_invserv_almacen_movim_detalles_utensilios_modulo_exist AS 
 SELECT b.id_bodega, e.existencia AS cantidad, p.id_prod, p.descripcion, p.id_unidadsalida AS unidad, p.status, p.id_linea, l.descripcion AS linea
   FROM tbl_invserv_inventarios p
   JOIN tbl_invserv_existencias e ON p.id_prod::text = e.id_prod::text
   JOIN tbl_invserv_bodegas b ON b.id_bodega = e.id_bodega
   JOIN tbl_invserv_lineas l ON p.id_linea::text = l.id_linea::text
  WHERE p.id_tipo = 'G'::bpchar AND p.nosevende = B'1'::"bit";

ALTER TABLE view_invserv_almacen_movim_detalles_utensilios_modulo_exist
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
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  _IVANoDed numeric(19,6); _IVA_Deducible numeric(19,6);
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_DCAF char(19);
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
	ELSE -- Es a crédito, por lo tanto revisa si el total de esta factura mas el total de la deuda con el proveedor rebasa el límite de crédito, de ser asi, rechazará la compra
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

	IF _IEPS > 0.0 AND ( _CC_IEPS = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS no existe o no se ha enlazado';
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
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);	
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
CREATE OR REPLACE FUNCTION sp_invserv_actualizar_existencias()
  RETURNS SETOF record AS
$BODY$
DECLARE 	
	_err int; _Mes smallint; _Ano smallint; _Notice varchar(255); _id_partida smallint; 
	_entrada numeric(9,3); _salida numeric(9,3);    
	_existenciainicial numeric(9,3); _existenciaini numeric(9,3); _existenciafin numeric(9,3);  
	_PER RECORD; _MOV RECORD; _PRD RECORD; _BOD RECORD;
BEGIN
	_err := 0;
	_Notice := 'AA';
	-- crea la tabla temporal de saldos de la cual se actualizar?n los saldos finales
	CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_COSTOS_DETALLE (
		id_costo integer NOT NULL,
		id_movimiento integer NOT NULL,
		id_prod character varying(20) NOT NULL,
		id_concepto smallint NOT NULL,
		existbod numeric(9,3) NOT NULL
	);

	CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_EXISTENCIAS_PERIODOS
	(
		mes smallint NOT NULL,
		ano smallint NOT NULL,
		id_bodega smallint NOT NULL,
		id_prod character varying(20) NOT NULL,
		existenciaini numeric(9,3) NOT NULL,
		existenciafin numeric(9,3) NOT NULL
	);	

	_Mes := (	select Mes -- el ultimo mes cerrado ( si no hay meses cerrados sera nulo )
				from TBL_CONT_CATALOGO_PERIODOS
				where Mes <> 13 and Cerrado = '1' 
				order by Ano Desc, Mes Desc limit 1);
	_Ano := (	select Ano -- igual en el a?o
				from TBL_CONT_CATALOGO_PERIODOS 
				where Mes <> 13 and Cerrado = '1' 
				order by Ano Desc, Mes Desc limit 1);

	--raise notice 'Ultimo Cerrado: mes % ano %', _Mes, _Ano;
	FOR _PRD IN
		( 	SELECT ID_Prod, TipoCosteo 
			FROM TBL_INVSERV_INVENTARIOS 
			WHERE ID_Tipo = 'P' OR (ID_Tipo = 'G' AND NoSeVende = '1')  --NoSeVende en gastos aplica a consumibles para existencias en bodegas de utensilios
			ORDER BY  SeProduce Asc, NivelProd Asc, ID_Prod Asc  )
	LOOP
		--if _PRD.ID_Prod = _Notice then raise notice 'PRODUCTO: %', _PRD.ID_Prod; end if;
		FOR _BOD IN 
			( SELECT ID_Bodega FROM TBL_INVSERV_BODEGAS ORDER BY ID_Bodega ASC )
		LOOP
			--if _PRD.ID_Prod = _Notice then raise notice 'BODEGA: %', _BOD.ID_Bodega; end if;
			_existenciaini := (select ExistenciaFin from TBL_INVSERV_EXISTENCIAS_PERIODOS where Mes = _Mes and Ano = _Ano and ID_Prod = _PRD.ID_Prod and ID_Bodega = _BOD.ID_Bodega);
			_existenciainicial := (select ExistenciaFin from TBL_INVSERV_EXISTENCIAS_PERIODOS where Mes = _Mes and Ano = _Ano and ID_Prod = _PRD.ID_Prod and ID_Bodega = _BOD.ID_Bodega);
			_existenciafin := (select ExistenciaFin from TBL_INVSERV_EXISTENCIAS_PERIODOS where Mes = _Mes and Ano = _Ano and ID_Prod = _PRD.ID_Prod and ID_Bodega = _BOD.ID_Bodega);
			
			FOR _PER IN (select * from TBL_CONT_CATALOGO_PERIODOS where Cerrado = '0' order by Ano Asc, Mes Asc)
			LOOP
				IF _PER.Mes = 13
				THEN
					CONTINUE;
				END IF;	
				--if _PRD.ID_Prod = _Notice then raise notice 'Actual: %/%:%', _PER.Mes, _PER.Ano, _BOD.ID_Bodega; end if;
				FOR _MOV IN 
						(	SELECT c.ID_Costo,c.ID_Movimiento,c.ID_Prod,c.ID_Concepto,c.Entrada,c.Salida,c.Existencia,c.ExistBod,con.Tipo
							FROM TBL_INVSERV_COSTOS_DETALLE c JOIN TBL_INVSERV_COSTOS_CONCEPTOS con ON
							c.ID_Concepto = con.ID_Concepto JOIN TBL_INVSERV_ALMACEN_MOVIM_CAB cab ON
							c.ID_Movimiento = cab.ID_Movimiento
							WHERE cab.ID_Bodega = _BOD.ID_Bodega and c.ID_Prod = _PRD.ID_Prod AND date_part('Month',c.Fecha) = _PER.Mes and date_part('Year',c.Fecha) = _PER.Ano and c.Status <> 'C'
							ORDER BY c.Fecha ASC, c.ID_Concepto ASC, c.ID_Costo ASC		
						)
				LOOP
					IF _MOV.Tipo = 'ENT' OR (_MOV.Tipo = 'MIX' AND _MOV.ID_Concepto = -1) -- entradas 
					THEN
						_entrada := _MOV.Entrada;
						_salida := 0.000;
						_existenciafin := _existenciaini + _entrada; 
					ELSIF _MOV.Tipo = 'SAL' OR (_MOV.Tipo = 'MIX' AND _MOV.ID_Concepto = -2) -- salidas
					THEN
						_entrada := 0.000;
						_salida := _MOV.Salida;
						_existenciafin := _existenciaini - _salida; 
					END IF;

					INSERT INTO _TMP_INVSERV_COSTOS_DETALLE (id_costo,id_movimiento,id_prod,id_concepto,existbod)
					VALUES(_MOV.ID_Costo, _MOV.ID_Movimiento, _PRD.ID_Prod, _MOV.ID_Concepto, _existenciafin); 	 

					_existenciaini := _existenciafin;
					--if _PRD.ID_Prod = _Notice then raise notice 'Ent % Sal % ExBod %',_entrada,_salida,_existenciafin; end if;
				END LOOP;
				--if _PRD.ID_Prod = _Notice then raise notice '%/%:% ExistenciaIni % ExistenciaFin %',_PER.Mes, _PER.Ano, _BOD.ID_Bodega, _ExistenciaInicial, _ExistenciaFin; end if;
				--Agrega la existencia del producto de este periodo
				INSERT INTO _tmp_invserv_existencias_periodos(mes,ano,id_bodega,id_prod,existenciaini,existenciafin)
				VALUES(_PER.Mes, _PER.Ano, _BOD.ID_Bodega,_PRD.ID_Prod, _ExistenciaInicial, _ExistenciaFin);
					
				_existenciaini := _existenciafin;
				_existenciainicial := _existenciafin;
		
			END LOOP;

		END LOOP;

	END LOOP;	
		
	-- Actualiza las existencias con las tablas temporales	
	UPDATE TBL_INVSERV_COSTOS_DETALLE
	SET existbod = tm.existbod
	FROM _TMP_INVSERV_COSTOS_DETALLE tm, TBL_INVSERV_COSTOS_DETALLE c 
	WHERE	tm.ID_Costo = c.ID_Costo and 
			c.ID_Costo = TBL_INVSERV_COSTOS_DETALLE.ID_Costo;

	-- Actualiza las existencias finales del mes actual
	UPDATE TBL_INVSERV_EXISTENCIAS_PERIODOS
	SET	ExistenciaIni = tm.ExistenciaIni,
		ExistenciaFin = tm.ExistenciaFin
	FROM _TMP_INVSERV_EXISTENCIAS_PERIODOS tm,  TBL_INVSERV_EXISTENCIAS_PERIODOS c
	WHERE tm.ID_Prod = c.ID_Prod and tm.Mes = c.Mes and tm.Ano = c.Ano and tm.ID_Bodega = c.ID_Bodega and
		c.ID_Prod = TBL_INVSERV_EXISTENCIAS_PERIODOS.ID_Prod and c.Mes = TBL_INVSERV_EXISTENCIAS_PERIODOS.Mes and c.Ano = TBL_INVSERV_EXISTENCIAS_PERIODOS.Ano and c.ID_Bodega = TBL_INVSERV_EXISTENCIAS_PERIODOS.ID_Bodega;

	_Mes := (	select Mes -- el ultimo mes abierto ( si no hay meses abiertos sera nulo )
				from TBL_CONT_CATALOGO_PERIODOS
				where Mes <> 13 and Cerrado = '0' 
				order by Ano Desc, Mes Desc limit 1);
	_Ano := (	select Ano -- igual en el a?o
				from TBL_CONT_CATALOGO_PERIODOS 
				where Mes <> 13 and Cerrado = '0' 
				order by Ano Desc, Mes Desc limit 1);

	IF _Mes is not null AND _ano is not null
	THEN
		--raise notice 'FINAL DEL PERIODO: mes % ano %', _Mes, _Ano;
	
		UPDATE TBL_INVSERV_EXISTENCIAS
		SET Existencia = c.ExistenciaFin
		FROM  TBL_INVSERV_EXISTENCIAS e, TBL_INVSERV_EXISTENCIAS_PERIODOS c 
		WHERE
			e.ID_Prod = c.ID_Prod and  
			c.ID_Prod = TBL_INVSERV_EXISTENCIAS.ID_Prod and 
			e.ID_Bodega = c.ID_Bodega and
			c.ID_Bodega = TBL_INVSERV_EXISTENCIAS.ID_Bodega and 
			c.Ano = _Ano and c.Mes = _Mes;
	END IF;

	DROP TABLE _TMP_INVSERV_COSTOS_DETALLE;
	DROP TABLE _TMP_INVSERV_EXISTENCIAS_PERIODOS;
	
	RETURN QUERY 
	SELECT 0 as err, 'LAS EXISTENCIAS DE ALMACEN SE ACTUALIZARON SATISFACTORIAMENTE'::varchar as res;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_invserv_actualizar_existencias()
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_invserv_actualizar_polcant()
  RETURNS SETOF record AS
$BODY$
DECLARE 	
	_err int; _Mes smallint; _Ano smallint; _Notice varchar(255); _Fecha timestamp;
	_MOV RECORD; _DET RECORD; _POL TBL_CONT_POLIZAS_DETALLE%ROWTYPE;
	_Part smallint; _Parcial numeric(19,4); _Debe numeric(19,4); _Haber numeric(19,4);
BEGIN
	_err := 0;
	--_Notice := 'PT2';
	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS
	(
		id integer NOT NULL,
		total numeric(19,4) NOT NULL
	);
	
	CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE
	(
		id integer NOT NULL,
		part smallint NOT NULL,
		cuenta character(19) NOT NULL,
		concepto character varying(80) NOT NULL,
		parcial numeric(19,4) NOT NULL,
		moneda smallint NOT NULL,
		tc numeric(19,4) NOT NULL,
		debe numeric(19,4) NOT NULL,
		haber numeric(19,4) NOT NULL
	);

	_Mes := (	select Mes 
				from TBL_CONT_CATALOGO_PERIODOS 
				where Mes <> 13 and Cerrado = '0' 
				order by Ano Asc, Mes Asc limit 1);
	_Ano := (	select Ano 
				from TBL_CONT_CATALOGO_PERIODOS 
				where Mes <> 13 and Cerrado = '0' 
				order by Ano Asc, Mes Asc limit 1);

	_Fecha := getfecha(1, _Mes, _Ano);

	FOR _MOV IN 
				(	SELECT cab.ID_Movimiento, con.Tipo, cab.Fecha, cab.ID_Pol, cab.CR_Pri, cab.CR_Sec, con.Descripcion, cab.Concepto, con.ID_Concepto, con.CC
					FROM TBL_INVSERV_ALMACEN_MOVIM_CAB cab JOIN TBL_INVSERV_COSTOS_CONCEPTOS con ON
						cab.ID_Concepto = con.ID_Concepto JOIN TBL_CONT_POLIZAS pol ON
						cab.ID_Pol = pol.ID
					WHERE cab.Fecha >= _Fecha AND cab.Status <> 'C' AND cab.ID_Pol IS NOT NULL AND con.Tipo <> 'MIX' --MIX son movimientos de almacén de utensilios
					ORDER BY cab.Fecha ASC, con.ID_Concepto ASC, cab.ID_Movimiento		
				)
	LOOP
		IF _MOV.CC IS NOT NULL
		THEN
			--raise notice '/////////////////////////////////// %', _Fecha;
			--raise notice '%:%  /  % - %  /  %:%', _MOV.Fecha, _MOV.ID_Pol, _MOV.Descripcion, _MOV.Concepto, _MOV.ID_Concepto, _MOV.CC;
			_Part := 1;
			_Parcial := 0.0;
			_Debe := 0.0;
			_Haber := 0.0;
			FOR _DET IN	(	
								SELECT det.id_prod, i.id_cc, i.descripcion, det.entrada, det.salida, det.ultimocosto, det.costopromedio, det.debe, det.haber
								FROM TBL_INVSERV_COSTOS_DETALLE det JOIN TBL_INVSERV_INVENTARIOS i ON 
									det.ID_Prod = i.ID_Prod
								WHERE det.ID_Movimiento = _MOV.ID_Movimiento 
								ORDER BY det.ID_Costo 
							)
			LOOP		
				--raise notice '% / % %: % - % / % - % / % - %', _part, _DET.ID_CC, _DET.ID_Prod, _DET.Entrada, _DET.Salida, _DET.UltimoCosto, _DET.CostoPromedio, _DET.Debe, _DET.Haber;

				_Haber := _Haber + _DET.Haber;
				_Debe := _Debe + _DET.Debe;

				INSERT INTO _TMP_CONT_POLIZAS_DETALLE(id,part,cuenta,concepto,parcial,moneda,tc,debe,haber) 
				VALUES( _MOV.ID_Pol, _part, _DET.ID_CC, _DET.Descripcion, (case when  _MOV.Tipo = 'ENT' then _DET.Debe - _DET.Haber else _DET.Haber - _DET.Debe end), 1, 1.0, _DET.Debe, _DET.Haber);  
				_Part := _Part + 1;
			END LOOP;

			_Parcial := case when _Debe >= _Haber then _Debe - _Haber else _Haber - _Debe end;
			
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE(id,part,cuenta,concepto,parcial,moneda,tc,debe,haber) 
			VALUES( _MOV.ID_Pol, _part, _MOV.CC, (substring(_MOV.Concepto from 1 for 79)), 
					_Parcial, 1, 1.0, (case when _Debe >= _Haber then 0.0 else _Parcial end), (case when  _Debe >= _Haber then _Parcial else 0.0 end));  

			INSERT INTO _TMP_CONT_POLIZAS ( ID, Total )
			VALUES( _MOV.ID_Pol, (case when _Debe >= _Haber then _Debe else _Haber end));
			
			/*  raise notice '---------------------------';
			FOR _DET IN	(	
								SELECT *
								FROM TBL_CONT_POLIZAS_DETALLE det
								WHERE det.ID = _MOV.ID_Pol 
								ORDER BY det.Part 
							)
			LOOP		
				raise notice '% / % % / % - %', _DET.Part, _DET.Cuenta, _DET.Concepto, _DET.Debe, _DET.Haber;
			END LOOP; */
		
		END IF;
	END LOOP;
		
	-- Actualiza las polizas con las tablas temporales
	-- Primero borra los detalles. Estos no se perderan porque aunque fallara, al volver a actualizar, los vuelve a tomar de los movimientos al almacen, insertandolos otra vez 	
	DELETE FROM TBL_CONT_POLIZAS_DETALLE
	WHERE ID IN (SELECT ID FROM _TMP_CONT_POLIZAS);
	--Ahora inserta los borrados desde _TMP_CONT_POLIZAS_DETALLE
	INSERT INTO TBL_CONT_POLIZAS_DETALLE(id,part,cuenta,concepto,parcial,moneda,tc,debe,haber)
	SELECT id, part, cuenta, concepto, parcial, moneda, tc, debe, haber
	FROM _TMP_CONT_POLIZAS_DETALLE;
	--Por ultimo, actualiza los cabeceros desde _TMP_CONT_POLIZAS
	UPDATE TBL_CONT_POLIZAS
	SET Total = tm.Total
	FROM _TMP_CONT_POLIZAS tm, TBL_CONT_POLIZAS p 
	WHERE	tm.ID = p.ID AND p.ID = TBL_CONT_POLIZAS.ID;
		
	DROP TABLE _TMP_CONT_POLIZAS_DETALLE;
	DROP TABLE _TMP_CONT_POLIZAS;
	
	RETURN QUERY 
	SELECT 0 as err, 'LAS POLIZAS SE MODIFICARON CONFORME A LOS MOVIMIENTO AL ALMACEN ACTUALES'::varchar as res;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_invserv_actualizar_polcant()
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION getcliprosldtotmn(
    _clpr character,
    _cve integer)
  RETURNS numeric AS
$BODY$
DECLARE
	_saldo numeric(19,4);
	_resultado numeric(19,4);
BEGIN
	_resultado = 0.0;
	IF _clpr = 'CL'
	THEN
		FOR _saldo 
		IN 
			select s.saldo * m.tc
			from tbl_cont_monedas m 
				join tbl_client_saldos_monedas s on m.clave = s.id_moneda
			where s.saldo <> 0.00 and s.id_tipo = 'CL' and s.id_clave = _cve
			order by m.clave asc 
		LOOP
			_resultado := _resultado + round(_saldo,2); 
		END LOOP;
	ELSIF _clpr = 'PR'
	THEN
		FOR _saldo 
		IN 
			select s.saldo * m.tc 
			from tbl_cont_monedas m 
				join tbl_provee_saldos_monedas s on m.clave = s.id_moneda
			where s.saldo <> 0.00 and s.id_tipo = 'PR' and s.id_clave = _cve
			order by m.clave asc 
		LOOP
			_resultado := _resultado + round(_saldo,2); 
		END LOOP;
	END IF;
		
	return _resultado;
END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION getcliprosldtotmn(character, integer)
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
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_DCAF char(19);
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
	ELSE -- Es a crédito, por lo tanto revisa si el total de esta factura mas el total de la deuda con el proveedor rebasa el límite de crédito, de ser asi, rechazará la compra
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
		_result := 'ERROR: La cuenta de iva acreditable efectivamente pagado o la de iva acreditable pendiente de pagar, no existe o no se ha enlazado';
	END IF;

	IF _IEPS > 0.0 AND ( _CC_IEPS = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS no existe o no se ha enlazado';
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
			ELSE -- de credito... envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVAPN, 'Impuesto de la compra', _IVA, _Moneda, _TC, _IVAPesos, 0.0);
			END IF;
		END IF;

		IF _IEPS > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, _IEPSPesos, 0.0);	
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
	_CC_IVA char(19); _CC_IVAPN char(19); _CC_IEPS char(19); _CC_IVARet char(19); _CC_ISRRet char(19); _ImporteTotalPesos numeric(19,4); 
	_IVAPesos numeric(19,4); _IEPSPesos numeric(19,4); _IVARetPesos numeric(19,4); _ISRRetPesos numeric(19,4); _DescPesos numeric(19,4); _TotalPesos numeric(19,4);  
	_Fija bit; _FijaBAN bit; _CC_Desc char(19); _CC_DCAF char(19); 
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
	--_FijaBAN := case when _ID_FormaPago = 0 then (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 1 and Clave = _ID_BanCaj)
		--				else (select Fijo from TBL_BANCOS_CUENTAS where Tipo = 0 and Clave = _ID_BanCaj) end
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
	ELSE -- Es a crédito, por lo tanto revisa si el total de esta factura mas el total de la deuda del cliente rebasa el límite de crédito, de ser asi, rechazará la venta
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

	IF _IEPS > 0.0 AND ( _CC_IEPS = '' )
	THEN
		_err := 3;
		_result := 'ERROR: La cuenta del IEPS no existe o no se ha enlazado';
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
			_result := 'ERROR: La cuenta para ventas a cr?dito no existe o no se ha enlazado';	
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
			ELSE -- de credito... envia a pendiente de pagar
				INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
				VALUES(_contPart, _CC_IVAPN, 'Impuesto de la venta', _IVA, _Moneda, _TC, 0.0, _IVAPesos);	
			END IF;
		END IF;

		IF _IEPS > 0.0
		THEN
			_contPart := _contPart + 1;
			INSERT INTO _TMP_CONT_POLIZAS_DETALLE_TMP
			VALUES(_contPart, _CC_IEPS, 'Impuesto Especial sobre Producción y Servicio', _IEPS, _Moneda, _TC, 0.0, _IEPSPesos);	
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
INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('NOM_AGUINALDO_ELIMINAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('NOM_VACACIONES_ELIMINAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('NOM_ISR_ELIMINAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('NOM_IMSS_ELIMINAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('NOM_CREDSAL_ELIMINAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('NOM_FONACOT_ELIMINAR');

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_aguinaldo_eliminar(_id_aguinaldo smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'El movimiento de Aguinaldo se eliminó de la base de datos';
	
	-- si existe marcar? error
	IF(select count(*) from TBL_NOM_AGUINALDO where ID_Aguinaldo = _ID_Aguinaldo) < 1
	THEN
		_err := 3;
		_result := 'ERROR: No existe un movimiento con esta clave, no se puede eliminar';
	END IF;
	
	IF _err = 0
	THEN
		DELETE FROM TBL_NOM_AGUINALDO
		WHERE ID_Aguinaldo = _ID_Aguinaldo;
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_aguinaldo;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_aguinaldo_eliminar(smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_vacaciones_eliminar(_id_vacaciones smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'El movimiento de vacaciones se eliminó de la base de datos';
	
	-- si existe marcar? error
	IF(select count(*) from TBL_NOM_VACACIONES where ID_Vacaciones = _ID_Vacaciones) < 1
	THEN
		_err := 3;
		_result := 'ERROR: No existe un movimiento con esta clave, no se puede eliminar';
	END IF;
	
	IF _err = 0
	THEN
		DELETE FROM TBL_NOM_VACACIONES
		WHERE ID_Vacaciones = _ID_Vacaciones;
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_vacaciones;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_vacaciones_eliminar(smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_isr_eliminar(_id_isr smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE  
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'El movimiento de isr se eliminó de la base de datos';
	
	-- si existe marcar? error
	IF(select count(*) from TBL_NOM_ISR where ID_Isr = _ID_Isr) < 1
	THEN
		_err := 3;
		_result := 'ERROR: No existe un movimiento con esta clave, no se puede eliminar';
	END IF;
	
	IF _err = 0
	THEN
		DELETE FROM TBL_NOM_ISR
		WHERE ID_Isr = _ID_Isr;
	
		DELETE FROM TBL_NOM_ISR_ANUALIZADO
		WHERE ID_Isr = _ID_Isr;
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_isr;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_isr_eliminar(smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_imss_eliminar(_id_imss smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE  
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'El movimiento de IMSS se eliminó de la base de datos';
	
	-- si existe marcar? error
	IF(select count(*) from TBL_NOM_IMSS where ID_Imss = _ID_Imss) < 1
	THEN
		_err := 3;
		_result := 'ERROR: No existe un movimiento con esta clave, no se puede eliminar';
	END IF;
	
	IF _err = 0
	THEN
		DELETE FROM TBL_NOM_IMSS
		WHERE ID_Imss = _ID_Imss;
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_imss;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_imss_eliminar(smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_nom_credito_salario_eliminar(_id_cs smallint)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'El movimiento de crédito al salario se eliminó de la base de datos';
	
	IF(select count(*) from TBL_NOM_CREDITO_SALARIO where ID_CS = _ID_CS) < 1
	THEN
		_err := 3;
		_result := 'ERROR: No existe un movimiento con esta clave, no se puede eliminar';
	END IF;
	
	IF _err = 0
	THEN
		DELETE FROM TBL_NOM_CREDITO_SALARIO
		WHERE ID_CS = _ID_CS;		
	END IF;
		
	RETURN QUERY SELECT _err, _result, _id_cs;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_nom_credito_salario_eliminar(smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE tbl_sat_pyd_nomina
(
  deduccion bit(1) NOT NULL,
  clave character(3) NOT NULL,
  descripcion character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_sat_pyd_nomina PRIMARY KEY (deduccion, clave)
);
ALTER TABLE tbl_sat_pyd_nomina
  OWNER TO [[owner]];

--@FIN_BLOQUE
-- Primero percepciones
insert into tbl_sat_pyd_nomina
values('0','001','Sueldos, Salarios Rayas y Jornales');
insert into tbl_sat_pyd_nomina
values('0','002','Gratificación Anual (Aguinaldo)');
insert into tbl_sat_pyd_nomina
values('0','003','Participación de los Trabajadores en las Utilidades PTU');
insert into tbl_sat_pyd_nomina
values('0','004','Reembolso de Gastos Médicos Dentales y Hospitalarios');
insert into tbl_sat_pyd_nomina
values('0','005','Fondo de Ahorro');
insert into tbl_sat_pyd_nomina
values('0','006','Caja de ahorro');
insert into tbl_sat_pyd_nomina
values('0','009','Contribuciones a Cargo del Trabajador Pagadas por el Patrón');
insert into tbl_sat_pyd_nomina
values('0','010','Premios por puntualidad');
insert into tbl_sat_pyd_nomina
values('0','011','Prima de Seguro de vida');
insert into tbl_sat_pyd_nomina
values('0','012','Seguro de Gastos Médicos Mayores');
insert into tbl_sat_pyd_nomina
values('0','013','Cuotas Sindicales Pagadas por el Patrón');
insert into tbl_sat_pyd_nomina
values('0','014','Subsidios por incapacidad');
insert into tbl_sat_pyd_nomina
values('0','015','Becas para trabajadores y/o hijos');
insert into tbl_sat_pyd_nomina
values('0','016','Otros');
insert into tbl_sat_pyd_nomina
values('0','017','Subsidio para el empleo');
insert into tbl_sat_pyd_nomina
values('0','019','Horas extra');
insert into tbl_sat_pyd_nomina
values('0','020','Prima dominical');
insert into tbl_sat_pyd_nomina
values('0','021','Prima vacacional');
insert into tbl_sat_pyd_nomina
values('0','022','Prima por antigüedad');
insert into tbl_sat_pyd_nomina
values('0','023','Pagos por separación');
insert into tbl_sat_pyd_nomina
values('0','024','Seguro de retiro');
insert into tbl_sat_pyd_nomina
values('0','025','Indemnizaciones');
insert into tbl_sat_pyd_nomina
values('0','026','Reembolso por funeral');
insert into tbl_sat_pyd_nomina
values('0','027','Cuotas de seguridad social pagadas por el patrón');
insert into tbl_sat_pyd_nomina
values('0','028','Comisiones');
insert into tbl_sat_pyd_nomina
values('0','029','Vales de despensa');
insert into tbl_sat_pyd_nomina
values('0','030','Vales de restaurante');
insert into tbl_sat_pyd_nomina
values('0','031','Vales de gasolina');
insert into tbl_sat_pyd_nomina
values('0','032','Vales de ropa');
insert into tbl_sat_pyd_nomina
values('0','033','Ayuda para renta');
insert into tbl_sat_pyd_nomina
values('0','034','Ayuda para artículos escolares');
insert into tbl_sat_pyd_nomina
values('0','035','Ayuda para anteojos');
insert into tbl_sat_pyd_nomina
values('0','036','Ayuda para transporte');
insert into tbl_sat_pyd_nomina
values('0','037','Ayuda para gastos de funeral');
insert into tbl_sat_pyd_nomina
values('0','038','Otros ingresos por salarios');
insert into tbl_sat_pyd_nomina
values('0','039','Jubilaciones, pensiones o haberes de retiro');
--Ahora deducciones
insert into tbl_sat_pyd_nomina
values('1','001','Seguridad social');
insert into tbl_sat_pyd_nomina
values('1','002','ISR');
insert into tbl_sat_pyd_nomina
values('1','003','Aportaciones a retiro, cesantía en edad avanzada y vejez.');
insert into tbl_sat_pyd_nomina
values('1','004','Otros');
insert into tbl_sat_pyd_nomina
values('1','005','Aportaciones a Fondo de vivienda');
insert into tbl_sat_pyd_nomina
values('1','006','Descuento por incapacidad');
insert into tbl_sat_pyd_nomina
values('1','007','Pensión alimenticia');
insert into tbl_sat_pyd_nomina
values('1','008','Renta');
insert into tbl_sat_pyd_nomina
values('1','009','Préstamos provenientes del Fondo Nacional de la Vivienda para los Trabajadores');
insert into tbl_sat_pyd_nomina
values('1','010','Pago por crédito de vivienda');
insert into tbl_sat_pyd_nomina
values('1','011','Pago de abonos INFONACOT');
insert into tbl_sat_pyd_nomina
values('1','012','Anticipo de salarios');
insert into tbl_sat_pyd_nomina
values('1','013','Pagos hechos con exceso al trabajador');
insert into tbl_sat_pyd_nomina
values('1','014','Errores');
insert into tbl_sat_pyd_nomina
values('1','015','Pérdidas');
insert into tbl_sat_pyd_nomina
values('1','016','Averías');
insert into tbl_sat_pyd_nomina
values('1','017','Adquisición de artículos producidos por la empresa o establecimiento');
insert into tbl_sat_pyd_nomina
values('1','018','Cuotas para la constitución y fomento de sociedades cooperativas y de cajas de ahorro');
insert into tbl_sat_pyd_nomina
values('1','019','Cuotas sindicales');
insert into tbl_sat_pyd_nomina
values('1','020','Ausencia (Ausentismo)');
insert into tbl_sat_pyd_nomina
values('1','021','Cuotas obrero patronales');

--@FIN_BLOQUE
CREATE TABLE tbl_ventas_cierres_facturas
(
  id_cierre integer NOT NULL,
  id_entidad integer NOT NULL,
  numero integer NOT NULL,
  total numeric(19,4),
  CONSTRAINT pk_tbl_ventas_cierres_facturas PRIMARY KEY (id_cierre, id_entidad, numero),
  CONSTRAINT fk_tbl_ventas_cierres_facturas_tbl_ventas_cierres_cab FOREIGN KEY (id_cierre)
      REFERENCES tbl_ventas_cierres_cab (id_cierre) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE tbl_ventas_cierres_facturas
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
				FROM sp_bancos_trans_agregar('1',_claveBAN,_Fecha,_Concepto,_Beneficiario,_Traspaso,'RET','G','1',1.0,'','','1',_claveDEST,'000',_RFC,'01','','','') AS ( err integer, res varchar, clave integer);
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

		--Procede a la cancelacion de la CXC o PAGOS
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
	
		ELSE
			SELECT * INTO _errpart, _resultpart, _id_pol FROM sp_client_cxc_cancelar(_id_cxc) as ( err integer, res varchar, clave integer );
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

	RETURN QUERY SELECT _err, _result, _ID_Factura;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_ventas_facturas_cancelar(integer, smallint)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_invserv_alm_movs_auditar(_id_movimiento integer)
  RETURNS SETOF record AS
$BODY$
DECLARE  
	_Numero int; _Fecha timestamp; _Ref varchar(35); _err int; _result varchar(255); _ID_Bodega smallint; 
	_ID_Concepto smallint; _Tipo char(3); _Status char(1); _StatusFinal char(1); _AuditarAlm bit; _AuditarAlmESP bit; _mes smallint; _ano smallint; _MVCF int;  
	--Internas
	_SERIE varchar(8); _NumeroExt int; _BodExt smallint; _StatusEnt char(1); _StatusSal char(1); _NumFact int; _EntFact smallint;
	
BEGIN		
	_err := 0;
	_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_MOVIM' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');--'El movimiento se ha auditado / revertido';
	_ID_Concepto := (select ID_Concepto from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Movimiento = _ID_Movimiento);
	_Fecha := (select Fecha from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Movimiento = _ID_Movimiento);
	_Ref := (select Ref from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Movimiento = _ID_Movimiento);
	_ID_Bodega := (select ID_Bodega from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Movimiento = _ID_Movimiento);
	_Tipo := (select Tipo from TBL_INVSERV_COSTOS_CONCEPTOS where ID_Concepto = _ID_Concepto);
	_Status := (select Status from TBL_INVSERV_ALMACEN_MOVIM_CAB where ID_Movimiento = _ID_Movimiento);
	_AuditarAlm := (select AuditarAlm from TBL_INVSERV_BODEGAS where ID_Bodega = _ID_Bodega);
	_mes := date_part('month',_Fecha);
	_ano := date_part('year',_Fecha);
	
	_MVCF = ( select ventero from TBL_VARIABLES where ID_Variable = 'MVCF' );	

	raise notice 'Con:%Tipo:%Status:%,',_ID_Concepto,_Tipo,_Status;
	
	IF(select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano and Cerrado = 1) > 0
	OR (select count(*) from  TBL_CONT_CATALOGO_PERIODOS where Mes = _mes and Ano = _ano) < 1
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'CONT_POLIZAS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La fecha de poliza pertenece a un periodo cerrado o inexistente'
	END IF;

	IF split_part(_Ref, '|', 1) = 'CHFI' 
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_MOVIM' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--ERROR: No se pueden revertir movimientos creados desde un chequeo f?sico, cancela el chequeo directamente';
	ELSE
		IF _MVCF = 1
		THEN
			IF (select count(*) from TBL_INVSERV_CHFIS_CAB where ID_Bodega = _ID_Bodega and ( Cerrado = '1' or Generado = '1') and Status <> 'C' and Fecha >= _Fecha) > 0
			THEN
				_err := 3;
				_result := 'ERROR: No se puede auditar/revertir el movimiento al almacen porque ya existe un chequeo físico cerrado en esta bodega, con fecha posterior a este movimiento';
			END IF;
		END IF;

		-- Si se trata de una factura y esta se intenta revertir, buscará cierres de caja que la contemplen. En caso de encontrar alguno, rechazará la reversión
		IF split_part(_Ref, '|', 1) = 'VFAC' AND _Status = 'U' --Cuando el estaus es U de auditado, el proceso intentará revertir el movimiento 
		THEN
			_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
			_EntFact := (select ID_Entidad from tbl_ventas_facturas_cab where ID_VC = _NumeroExt);
			_NumFact := (select Numero from tbl_ventas_facturas_cab where ID_VC = _NumeroExt);

			IF (select count(*) from tbl_ventas_cierres_facturas where id_entidad = _EntFact and numero = _NumFact) > 0
			THEN
				_err := 3;
				_result := 'ERROR: No se pueden revertir movimientos pertenecientes a facturas que ya están dentro de algún cierre de caja';
			END IF;
		END IF;
	END IF;
	
	IF _err = 0 AND _Ref <> '' -- solo se audita o revierten movimientos creados por procesos externos com Producir, Facturar etc.
	THEN
		IF _AuditarAlm = '1' -- esta bodega es auditable
		THEN
			IF _Tipo = 'ENT' -- es entrada
			THEN
				IF _Status = 'U' -- esta auditado, tiene que revertir el almacen
				THEN
					_StatusFinal := 'R';

					UPDATE TBL_INVSERV_ALMACEN_MOVIM_CAB 
					SET Status = 'R'
					WHERE ID_Movimiento = _ID_Movimiento;
				
					UPDATE TBL_INVSERV_COSTOS_DETALLE
					SET Status = 'R'
					WHERE ID_Movimiento = _ID_Movimiento;
					
					UPDATE TBL_INVSERV_EXISTENCIAS
					SET Existencia = e.Existencia - ( 	SELECT SUM(Entrada)
										FROM TBL_INVSERV_COSTOS_DETALLE 
										WHERE ID_Movimiento = _ID_Movimiento and ID_Prod = e.ID_Prod
										GROUP BY ID_Prod 	)
					FROM TBL_INVSERV_EXISTENCIAS e, TBL_INVSERV_COSTOS_DETALLE tm 
					WHERE tm.ID_Movimiento = _ID_Movimiento and e.ID_Prod = tm.ID_Prod AND e.ID_Bodega = _ID_Bodega
						AND e.ID_Prod = TBL_INVSERV_EXISTENCIAS.ID_Prod
						AND e.ID_Bodega = TBL_INVSERV_EXISTENCIAS.ID_Bodega;	
				ELSIF _Status = 'G' -- Esta guardado, tiene que auditar y agregar al almacen
				THEN
					_StatusFinal := 'U';

					UPDATE TBL_INVSERV_ALMACEN_MOVIM_CAB 
					SET Status = 'U'
					WHERE ID_Movimiento = _ID_Movimiento;
					
					UPDATE TBL_INVSERV_COSTOS_DETALLE
					SET Status = 'U'
					WHERE ID_Movimiento = _ID_Movimiento;
				
					UPDATE TBL_INVSERV_EXISTENCIAS
					SET Existencia = e.Existencia + (	SELECT SUM(Entrada)
										FROM TBL_INVSERV_COSTOS_DETALLE 
										WHERE ID_Movimiento = _ID_Movimiento and ID_Prod = e.ID_Prod
										GROUP BY ID_Prod 	)
					FROM TBL_INVSERV_EXISTENCIAS e, TBL_INVSERV_COSTOS_DETALLE tm 
					WHERE tm.ID_Movimiento = _ID_Movimiento and e.ID_Prod = tm.ID_Prod AND e.ID_Bodega = _ID_Bodega
						AND e.ID_Prod = TBL_INVSERV_EXISTENCIAS.ID_Prod
						AND e.ID_Bodega = TBL_INVSERV_EXISTENCIAS.ID_Bodega;
				
				ELSE
					_err := 3;
					_result := (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_MOVIM' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Este movimiento esta revertido o cancelado. No puede auditar otra vez.';
				END IF;
			
 			ELSE -- Es una salida
			
				IF _Status = 'U' -- esta auditado, tiene que revertir pero no aumenta el almacen hasta que se cancela la factura o el movimiento que lo cre?
				THEN
					_StatusFinal := 'R';

					UPDATE TBL_INVSERV_ALMACEN_MOVIM_CAB 
					SET Status = 'R'
					WHERE ID_Movimiento = _ID_Movimiento;
					
					UPDATE TBL_INVSERV_COSTOS_DETALLE
					SET Status = 'R'
					WHERE ID_Movimiento = _ID_Movimiento;
					
				ELSIF _Status = 'P' -- Esta pendiente, tiene que auditar ( no descuenta el almacen porque quedo descontado al momento de facturar etc )
				THEN
					_StatusFinal := 'U';

					UPDATE TBL_INVSERV_ALMACEN_MOVIM_CAB 
					SET Status = 'U'
					WHERE ID_Movimiento = _ID_Movimiento;
					
					UPDATE TBL_INVSERV_COSTOS_DETALLE
					SET Status = 'U'
					WHERE ID_Movimiento = _ID_Movimiento;

				ELSE
					_err := 3;
					_result := (select msj3 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_MOVIM' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: Este movimiento esta revertido o cancelado. No puede auditar otra vez.';
				END IF;	
			END IF;
		
		ELSE
			_err := 3;
			_result := (select msj4 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ALM_MOVIM' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');--'ERROR: Esta bodega no es auditable.';
		END IF; 
	END IF;
		
	--Ahora el proceso largo de modificar el status del proceso que los creó
	IF _err = 0 and _Ref <> '' and _Ref is not null
	THEN
		IF split_part(_Ref, '|', 1) = 'VDEV' -- Se trata de una Devolucion s/venta
		THEN
			_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
			
			UPDATE TBL_VENTAS_DEVOLUCIONES_CAB
			SET Status = ( case when _StatusFinal = 'U' then 'E' else 'R' end )
			WHERE ID_VC = _NumeroExt;

		ELSIF split_part(_Ref, '|', 1) = 'VFAC' -- Se trata de una factura
		THEN
			_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
			
			UPDATE TBL_VENTAS_FACTURAS_CAB
			SET Status = ( case when _StatusFinal = 'U' then 'E' else 'R' end )
			WHERE ID_VC = _NumeroExt;
					
		ELSIF split_part(_Ref, '|', 1) = 'VREM' -- Se trata de una remision
		THEN
			_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
			
			UPDATE TBL_VENTAS_REMISIONES_CAB
			SET Status = ( case when _StatusFinal = 'U' then 'E' else 'R' end )
			WHERE ID_VC = _NumeroExt;
					
		ELSIF split_part(_Ref, '|', 1) = 'CDEV' -- Se trata de una Devolucion s/compra
		THEN
			_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
			
			UPDATE TBL_COMPRAS_DEVOLUCIONES_CAB
			SET Status = ( case when _StatusFinal = 'U' then 'E' else 'R' end )
			WHERE ID_VC = _NumeroExt;

		ELSIF split_part(_Ref, '|', 1) = 'CFAC' -- Se trata de una compra factura
		THEN
			_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
			
			UPDATE TBL_COMPRAS_FACTURAS_CAB
			SET Status = ( case when _StatusFinal = 'U' then 'E' else 'R' end )
			WHERE ID_VC = _NumeroExt;

		ELSIF split_part(_Ref, '|', 1) = 'CREC' -- Se trata de una recepcion
		THEN
			_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
			
			UPDATE TBL_COMPRAS_RECEPCIONES_CAB
			SET Status = ( case when _StatusFinal = 'U' then 'E' else 'R' end )
			WHERE ID_VC = _NumeroExt;
					
		ELSIF split_part(_Ref, '|', 1) = 'PALM' -- Se trata de una plantilla
		THEN
			_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
			
			UPDATE TBL_INVSERV_ALMACEN_MOVIM_PLANT_CAB
			SET Status = ( case when _StatusFinal = 'U' then 'E' else 'R' end )
			WHERE ID_MovimPlant = _NumeroExt;
					
		ELSIF split_part(_Ref, '|', 1) = 'TALM' -- Se trata de un TRASPASO DE MATERIAL
		THEN
			IF split_part(_Ref, '|', 5) = 'ENT' -- Se trata de una ENTRADA de material
			THEN
				_SERIE := split_part(_Ref, '|', 3);
				_BodExt := cast(_SERIE as smallint);
				_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
				_StatusSal := ( select Status from TBL_INVSERV_ALMACEN_MOVIM_CAB where Ref = ('TALM|' || cast(_NumeroExt as varchar) || '|' || _SERIE || '||SAL') ); 
				_AuditarAlmESP = (select AuditarAlm from TBL_INVSERV_BODEGAS where ID_Bodega = ( select ID_Bodega from TBL_INVSERV_ALMACEN_MOVIM_CAB where Ref = ('TALM|' || cast(_NumeroExt as varchar) || '|' || _SERIE || '||SAL') ) );  

				UPDATE TBL_INVSERV_ALMACEN_BOD_MOV_CAB
				SET	Status = ( case when _StatusFinal = 'U' 
							then ( case 	when _AuditarAlmESP = '1'
									then ( case 	when _StatusSal = 'P' 
											then 'P' 
											when _StatusSal = 'R' 
											then 'P' else 'E' end )
									else 'E' end )
							else ( case 	when _AuditarAlmESP = '1'
									then ( case 	when _StatusSal = 'P' 
											then 'P' 
											when _StatusSal = 'U' 
											then 'P' else 'R' end ) 
									else 'R' end )
							end )
				WHERE ID_Movimiento = _NumeroExt;
						
			ELSIF split_part(_Ref, '|', 5) = 'SAL'  -- Se trata de una salida hacia bodega
			THEN
				_SERIE := split_part(_Ref, '|', 3);
				_BodExt := cast(_SERIE as smallint);
				_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
				_StatusEnt := ( select Status from TBL_INVSERV_ALMACEN_MOVIM_CAB where Ref = ('TALM|' || cast(_NumeroExt as varchar) || '|' || _SERIE || '||ENT') ); 
				_AuditarAlmESP = (select AuditarAlm from TBL_INVSERV_BODEGAS where ID_Bodega = ( select ID_Bodega from TBL_INVSERV_ALMACEN_MOVIM_CAB where Ref = ('TALM|' || cast(_NumeroExt as varchar) || '|' || _SERIE || '||ENT') ) );  

			
				UPDATE TBL_INVSERV_ALMACEN_BOD_MOV_CAB
				SET	Status = ( case 	when _StatusFinal = 'U' 
								then ( case 	when _AuditarAlmESP = '1'
										then ( case 	when _StatusEnt = 'G' 
												then 'P' 
												when _StatusEnt = 'R' 
												then 'P' else 'E' end )
										else 'E' end )
								else ( case 	when _AuditarAlmESP = '1'
										then ( case 	when _StatusEnt = 'G' 
												then 'P' 
												when _StatusEnt = 'U' 
												then 'P' else 'R' end ) 
										else 'R' end )
								end )
				WHERE ID_Movimiento = _NumeroExt;
				
			END IF;
		ELSIF split_part(_Ref, '|', 1) = 'PPRD' -- Se trata de un reporte de produccion
		THEN
			_NumeroExt := cast( split_part(_Ref, '|', 2) as int);
	
			-- sea el status final U o R del movimiento al almacen, el reporte quedará pendiente porque no se cierra aun
			IF (select Cerrada from TBL_PRODUCCION_REPORTES_CAB where ID_Reporte = _NumeroExt) = '0'
			THEN
				UPDATE TBL_PRODUCCION_REPORTES_CAB 
				SET	Status = 'P'
				WHERE ID_Reporte = _NumeroExt;
			ELSE 
				-- Si esta cerrado, entonces verifica los demás status de los movimientos al almacen revertir o emitir el reporte
				IF _StatusFinal = 'U'
				THEN
					IF (	select count(*) from TBL_INVSERV_ALMACEN_MOVIM_CAB 
						where ID_Movimiento in (	select ID_Pol  from tbl_produccion_reportes_det 
													where ID_Reporte = _NumeroExt and ID_Pol is not null) and Status = 'U') = (	select count(*)  from tbl_produccion_reportes_det
																																where ID_Reporte = _NumeroExt and ID_Pol is not null ) 	AND
					     (	select count(*) from TBL_INVSERV_ALMACEN_MOVIM_CAB 
						where ID_Movimiento in ( 	select ID_Pol from tbl_produccion_reportes_procesos
													where ID_Reporte = _NumeroExt and ID_Pol is not null) and Status = 'U') = (	select count(*) from tbl_produccion_reportes_procesos
																																where ID_Reporte = _NumeroExt and ID_Pol is not null) 	AND
					     (	select count(*) from TBL_INVSERV_ALMACEN_MOVIM_CAB 
						where ID_Movimiento in ( 	select ID_PolSP from tbl_produccion_reportes_procesos
													where ID_Reporte = _NumeroExt and ID_PolSP is not null) and Status = 'U') = (	select count(*) from tbl_produccion_reportes_procesos
																																	where ID_Reporte = _NumeroExt and ID_PolSP is not null) 
					THEN
						UPDATE TBL_PRODUCCION_REPORTES_CAB 
						SET	Status = 'E'
						WHERE ID_Reporte = _NumeroExt;
					ELSE
						UPDATE TBL_PRODUCCION_REPORTES_CAB 
						SET	Status = 'P'
						WHERE ID_Reporte = _NumeroExt;
					END IF;
				ELSE -- _StausFinal = R
					IF (	select count(*) from TBL_INVSERV_ALMACEN_MOVIM_CAB 
						where ID_Movimiento in (	select ID_Pol  from tbl_produccion_reportes_det 
													where ID_Reporte = _NumeroExt and ID_Pol is not null) and Status = 'R') = (	select count(*)  from tbl_produccion_reportes_det
																															where ID_Reporte = _NumeroExt and ID_Pol is not null ) 	AND
					     (	select count(*) from TBL_INVSERV_ALMACEN_MOVIM_CAB 
						where ID_Movimiento in ( 	select ID_Pol from tbl_produccion_reportes_procesos
													where ID_Reporte = _NumeroExt and ID_Pol is not null) and Status = 'R') = (	select count(*) from tbl_produccion_reportes_procesos
																															where ID_Reporte = _NumeroExt and ID_Pol is not null) 	AND
					     (	select count(*) from TBL_INVSERV_ALMACEN_MOVIM_CAB 
						where ID_Movimiento in ( 	select ID_PolSP from tbl_produccion_reportes_procesos
													where ID_Reporte = _NumeroExt and ID_PolSP is not null) and Status = 'R') = (	select count(*) from tbl_produccion_reportes_procesos
																																where ID_Reporte = _NumeroExt and ID_PolSP is not null) 
					THEN
						UPDATE TBL_PRODUCCION_REPORTES_CAB 
						SET	Status = 'R'
						WHERE ID_Reporte = _NumeroExt;
					ELSE
						UPDATE TBL_PRODUCCION_REPORTES_CAB 
						SET	Status = 'P'
						WHERE ID_Reporte = _NumeroExt;
					END IF;
				END IF;
			END IF;
			 
		END IF;

	END IF;
	
	RETURN QUERY SELECT _err, _result, _ID_Movimiento;

END
$BODY$
  LANGUAGE plpgsql;
ALTER FUNCTION sp_invserv_alm_movs_auditar(integer)
  OWNER TO [[owner]];

--@FIN_BLOQUE
CREATE TABLE tbl_sat_codagrup
(
  codigo character varying(10) NOT NULL,
  descripcion character varying(254) NOT NULL,
  nivel smallint NOT NULL,
  CONSTRAINT pk_tbl_sat_codagrup PRIMARY KEY (codigo)
);
ALTER TABLE tbl_sat_codagrup
  OWNER TO [[owner]];

--@FIN_BLOQUE
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','101','Caja');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','101.01','Caja y efectivo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','102','Bancos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','102.01','Bancos nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','102.02','Bancos extranjeros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','103','Inversiones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','103.01','Inversiones temporales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','103.02','Inversiones en fideicomisos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','103.03','Otras inversiones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','104','Otros instrumentos financieros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','104.01','Otros instrumentos financieros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','105','Clientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','105.01','Clientes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','105.02','Clientes extranjeros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','105.03','Clientes nacionales parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','105.04','Clientes extranjeros parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','106','Cuentas y documentos por cobrar a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.01','Cuentas y documentos por cobrar a corto plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.02','Cuentas y documentos por cobrar a corto plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.03','Cuentas y documentos por cobrar a corto plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.04','Cuentas y documentos por cobrar a corto plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.05','Intereses por cobrar a corto plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.06','Intereses por cobrar a corto plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.07','Intereses por cobrar a corto plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.08','Intereses por cobrar a corto plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.09','Otras cuentas y documentos por cobrar a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','106.10','Otras cuentas y documentos por cobrar a corto plazo parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','107','Deudores diversos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','107.01','Funcionarios y empleados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','107.02','Socios y accionistas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','107.03','Partes relacionadas nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','107.04','Partes relacionadas extranjeros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','107.05','Otros deudores diversos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','108','Estimación de cuentas incobrables');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','108.01','Estimación de cuentas incobrables nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','108.02','Estimación de cuentas incobrables extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','108.03','Estimación de cuentas incobrables nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','108.04','Estimación de cuentas incobrables extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','109','Pagos anticipados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.01','Seguros y fianzas pagados por anticipado nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.02','Seguros y fianzas pagados por anticipado extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.03','Seguros y fianzas pagados por anticipado nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.04','Seguros y fianzas pagados por anticipado extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.05','Rentas pagados por anticipado nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.06','Rentas pagados por anticipado extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.07','Rentas pagados por anticipado nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.08','Rentas pagados por anticipado extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.09','Intereses pagados por anticipado nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.10','Intereses pagados por anticipado extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.11','Intereses pagados por anticipado nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.12','Intereses pagados por anticipado extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.13','Factoraje financiero pagados por anticipado nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.14','Factoraje financiero pagados por anticipado extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.15','Factoraje financiero pagados por anticipado nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.16','Factoraje financiero pagados por anticipado extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.17','Arrendamiento financiero pagados por anticipado nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.18','Arrendamiento financiero pagados por anticipado extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.19','Arrendamiento financiero pagados por anticipado nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.20','Arrendamiento financiero pagados por anticipado extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.21','Pérdida por deterioro de pagos anticipados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.22','Derechos fiduciarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','109.23','Otros pagos anticipados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','110','Subsidio al empleo por aplicar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','110.01','Subsidio al empleo por aplicar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','111','Crédito al diesel por acreditar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','111.01','Crédito al diesel por acreditar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','112','Otros estímulos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','112.01','Otros estímulos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','113','Impuestos a favor');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','113.01','IVA a favor');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','113.02','ISR a favor');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','113.03','IETU a favor');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','113.04','IDE a favor');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','113.05','IA a favor');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','113.06','Subsidio al empleo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','113.07','Pago de lo indebido');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','113.08','Otros impuestos a favor');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','114','Pagos provisionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','114.01','Pagos provisionales de ISR');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','115','Inventario');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','115.01','Inventario');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','115.02','Materia prima y materiales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','115.03','Producción en proceso');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','115.04','Productos terminados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','115.05','Mercancías en tránsito');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','115.06','Mercancías en poder de terceros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','115.07','Otros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','116','Estimación de inventarios obsoletos y de lento movimiento');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','116.01','Estimación de inventarios obsoletos y de lento movimiento');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','117','Obras en proceso de inmuebles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','117.01','Obras en proceso de inmuebles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','118','Impuestos acreditables pagados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','118.01','IVA acreditable pagado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','118.02','IVA acreditable de importación pagado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','118.03','IEPS acreditable pagado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','118.04','IEPS pagado en importación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','119','Impuestos acreditables por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','119.01','IVA pendiente de pago');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','119.02','IVA de importación pendiente de pago');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','119.03','IEPS pendiente de pago');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','119.04','IEPS pendiente de pago en importación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','120','Anticipo a proveedores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','120.01','Anticipo a proveedores nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','120.02','Anticipo a proveedores extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','120.03','Anticipo a proveedores nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','120.04','Anticipo a proveedores extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','121','Otros activos a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','121.01','Otros activos a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','151','Terrenos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','151.01','Terrenos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','152','Edificios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','152.01','Edificios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','153','Maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','153.01','Maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','154','Automóviles, autobuses, camiones de carga, tractocamiones, montacargas y remolques');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','154.01','Automóviles, autobuses, camiones de carga, tractocamiones, montacargas y remolques');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','155','Mobiliario y equipo de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','155.01','Mobiliario y equipo de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','156','Equipo de cómputo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','156.01','Equipo de cómputo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','157','Equipo de comunicación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','157.01','Equipo de comunicación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','158','Activos biológicos, vegetales y semovientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','158.01','Activos biológicos, vegetales y semovientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','159','Obras en proceso de activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','159.01','Obras en proceso de activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','160','Otros activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','160.01','Otros activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','161','Ferrocarriles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','161.01','Ferrocarriles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','162','Embarcaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','162.01','Embarcaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','163','Aviones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','163.01','Aviones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','164','Troqueles, moldes, matrices y herramental');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','164.01','Troqueles, moldes, matrices y herramental');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','165','Equipo de comunicaciones telefónicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','165.01','Equipo de comunicaciones telefónicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','166','Equipo de comunicación satelital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','166.01','Equipo de comunicación satelital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','167','Equipo de adaptaciones para personas con capacidades diferentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','167.01','Equipo de adaptaciones para personas con capacidades diferentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','168','Maquinaria y equipo de generación de energía de fuentes renovables o de sistemas de cogeneración de electricidad eficiente');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','168.01','Maquinaria y equipo de generación de energía de fuentes renovables o de sistemas de cogeneración de electricidad eficiente');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','169','Otra maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','169.01','Otra maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','170','Adaptaciones y mejoras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','170.01','Adaptaciones y mejoras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','171','Depreciación acumulada de activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.01','Depreciación acumulada de edificios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.02','Depreciación acumulada de maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.03','Depreciación acumulada de automóviles, autobuses, camiones de carga, tractocamiones, montacargas y remolques');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.04','Depreciación acumulada de mobiliario y equipo de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.05','Depreciación acumulada de equipo de cómputo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.06','Depreciación acumulada de equipo de comunicación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.07','Depreciación acumulada de activos biológicos, vegetales y semovientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.08','Depreciación acumulada de otros activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.09','Depreciación acumulada de ferrocarriles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.10','Depreciación acumulada de embarcaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.11','Depreciación acumulada de aviones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.12','Depreciación acumulada de troqueles, moldes, matrices y herramental');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.13','Depreciación acumulada de equipo de comunicaciones telefónicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.14','Depreciación acumulada de equipo de comunicación satelital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.15','Depreciación acumulada de equipo de adaptaciones para personas con capacidades diferentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.16','Depreciación acumulada de maquinaria y equipo de generación de energía de fuentes renovables o de sistemas de cogeneración de electricidad eficiente');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.17','Depreciación acumulada de adaptaciones y mejoras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','171.18','Depreciación acumulada de otra maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','172','Pérdida por deterioro acumulado de activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.01','Pérdida por deterioro acumulado de edificios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.02','Pérdida por deterioro acumulado de maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.03','Pérdida por deterioro acumulado de automóviles, autobuses, camiones de carga, tractocamiones, montacargas y remolques');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.04','Pérdida por deterioro acumulado de mobiliario y equipo de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.05','Pérdida por deterioro acumulado de equipo de cómputo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.06','Pérdida por deterioro acumulado de equipo de comunicación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.07','Pérdida por deterioro acumulado de activos biológicos, vegetales y semovientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.08','Pérdida por deterioro acumulado de otros activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.09','Pérdida por deterioro acumulado de ferrocarriles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.10','Pérdida por deterioro acumulado de embarcaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.11','Pérdida por deterioro acumulado de aviones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.12','Pérdida por deterioro acumulado de troqueles, moldes, matrices y herramental');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.13','Pérdida por deterioro acumulado de equipo de comunicaciones telefónicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.14','Pérdida por deterioro acumulado de equipo de comunicación satelital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.15','Pérdida por deterioro acumulado de equipo de adaptaciones para personas con capacidades diferentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.16','Pérdida por deterioro acumulado de maquinaria y equipo de generación de energía de fuentes renovables o de sistemas de cogeneración de electricidad eficiente');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.17','Pérdida por deterioro acumulado de adaptaciones y mejoras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','172.18','Pérdida por deterioro acumulado de otra maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','173','Gastos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','173.01','Gastos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','174','Gastos pre operativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','174.01','Gastos pre operativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','175','Regalías, asistencia técnica y otros gastos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','175.01','Regalías, asistencia técnica y otros gastos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','176','Activos intangibles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','176.01','Activos intangibles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','177','Gastos de organización');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','177.01','Gastos de organización');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','178','Investigación y desarrollo de mercado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','178.01','Investigación y desarrollo de mercado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','179','Marcas y patentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','179.01','Marcas y patentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','180','Crédito mercantil');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','180.01','Crédito mercantil');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','181','Gastos de instalación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','181.01','Gastos de instalación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','182','Otros activos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','182.01','Otros activos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','183','Amortización acumulada de activos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.01','Amortización acumulada de gastos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.02','Amortización acumulada de gastos pre operativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.03','Amortización acumulada de regalías, asistencia técnica y otros gastos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.04','Amortización acumulada de activos intangibles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.05','Amortización acumulada de gastos de organización');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.06','Amortización acumulada de investigación y desarrollo de mercado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.07','Amortización acumulada de marcas y patentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.08','Amortización acumulada de crédito mercantil');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.09','Amortización acumulada de gastos de instalación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','183.10','Amortización acumulada de otros activos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','184','Depósitos en garantía');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','184.01','Depósitos de fianzas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','184.02','Depósitos de arrendamiento de bienes inmuebles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','184.03','Otros depósitos en garantía');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','185','Impuestos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','185.01','Impuestos diferidos ISR');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','186','Cuentas y documentos por cobrar a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.01','Cuentas y documentos por cobrar a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.02','Cuentas y documentos por cobrar a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.03','Cuentas y documentos por cobrar a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.04','Cuentas y documentos por cobrar a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.05','Intereses por cobrar a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.06','Intereses por cobrar a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.07','Intereses por cobrar a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.08','Intereses por cobrar a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.09','Otras cuentas y documentos por cobrar a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','186.10','Otras cuentas y documentos por cobrar a largo plazo parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','187','Participación de los trabajadores en las utilidades diferidas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','187.01','Participación de los trabajadores en las utilidades diferidas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','188','Inversiones permanentes en acciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','188.01','Inversiones a largo plazo en subsidiarias');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','188.02','Inversiones a largo plazo en asociadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','188.03','Otras inversiones permanentes en acciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','189','Estimación por deterioro de inversiones permanentes en acciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','189.01','Estimación por deterioro de inversiones permanentes en acciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','190','Otros instrumentos financieros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','190.01','Otros instrumentos financieros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','191','Otros activos a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','191.01','Otros activos a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','201','Proveedores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','201.01','Proveedores nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','201.02','Proveedores extranjeros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','201.03','Proveedores nacionales parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','201.04','Proveedores extranjeros parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','202','Cuentas por pagar a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.01','Documentos por pagar bancario y financiero nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.02','Documentos por pagar bancario y financiero extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.03','Documentos y cuentas por pagar a corto plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.04','Documentos y cuentas por pagar a corto plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.05','Documentos y cuentas por pagar a corto plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.06','Documentos y cuentas por pagar a corto plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.07','Intereses por pagar a corto plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.08','Intereses por pagar a corto plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.09','Intereses por pagar a corto plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.10','Intereses por pagar a corto plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.11','Dividendo por pagar nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','202.12','Dividendo por pagar extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','203','Cobros anticipados a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.01','Rentas cobradas por anticipado a corto plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.02','Rentas cobradas por anticipado a corto plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.03','Rentas cobradas por anticipado a corto plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.04','Rentas cobradas por anticipado a corto plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.05','Intereses cobrados por anticipado a corto plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.06','Intereses cobrados por anticipado a corto plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.07','Intereses cobrados por anticipado a corto plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.08','Intereses cobrados por anticipado a corto plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.09','Factoraje financiero cobrados por anticipado a corto plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.10','Factoraje financiero cobrados por anticipado a corto plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.11','Factoraje financiero cobrados por anticipado a corto plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.12','Factoraje financiero cobrados por anticipado a corto plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.13','Arrendamiento financiero cobrados por anticipado a corto plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.14','Arrendamiento financiero cobrados por anticipado a corto plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.15','Arrendamiento financiero cobrados por anticipado a corto plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.16','Arrendamiento financiero cobrados por anticipado a corto plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.17','Derechos fiduciarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','203.18','Otros cobros anticipados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','204','Instrumentos financieros a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','204.01','Instrumentos financieros a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','205','Acreedores diversos a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','205.01','Socios, accionistas o representante legal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','205.02','Acreedores diversos a corto plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','205.03','Acreedores diversos a corto plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','205.04','Acreedores diversos a corto plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','205.05','Acreedores diversos a corto plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','205.06','Otros acreedores diversos a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','206','Anticipo de cliente');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','206.01','Anticipo de cliente nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','206.02','Anticipo de cliente extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','206.03','Anticipo de cliente nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','206.04','Anticipo de cliente extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','206.05','Otros anticipos de clientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','207','Impuestos trasladados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','207.01','IVA trasladado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','207.02','IEPS trasladado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','208','Impuestos trasladados cobrados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','208.01','IVA trasladado cobrado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','208.02','IEPS trasladado cobrado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','209','Impuestos trasladados no cobrados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','209.01','IVA trasladado no cobrado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','209.02','IEPS trasladado no cobrado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','210','Provisión de sueldos y salarios por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','210.01','Provisión de sueldos y salarios por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','210.02','Provisión de vacaciones por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','210.03','Provisión de aguinaldo por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','210.04','Provisión de fondo de ahorro por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','210.05','Provisión de asimilados a salarios por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','210.06','Provisión de anticipos o remanentes por distribuir');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','210.07','Provisión de otros sueldos y salarios por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','211','Provisión de contribuciones de seguridad social por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','211.01','Provisión de IMSS patronal por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','211.02','Provisión de SAR por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','211.03','Provisión de infonavit por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','212','Provisión de impuesto estatal sobre nómina por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','212.01','Provisión de impuesto estatal sobre nómina por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','213','Impuestos y derechos por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','213.01','IVA por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','213.02','IEPS por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','213.03','ISR por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','213.04','Impuesto estatal sobre nómina por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','213.05','Impuesto estatal y municipal por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','213.06','Derechos por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','213.07','Otros impuestos por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','214','Dividendos por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','214.01','Dividendos por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','215','PTU por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','215.01','PTU por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','215.02','PTU por pagar de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','215.03','Provisión de PTU por pagar');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','216','Impuestos retenidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.01','Impuestos retenidos de ISR por sueldos y salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.02','Impuestos retenidos de ISR por asimilados a salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.03','Impuestos retenidos de ISR por arrendamiento');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.04','Impuestos retenidos de ISR por servicios profesionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.05','Impuestos retenidos de ISR por dividendos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.06','Impuestos retenidos de ISR por intereses');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.07','Impuestos retenidos de ISR por pagos al extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.08','Impuestos retenidos de ISR por venta de acciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.09','Impuestos retenidos de ISR por venta de partes sociales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.10','Impuestos retenidos de IVA');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.11','Retenciones de IMSS a los trabajadores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','216.12','Otras impuestos retenidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','217','Pagos realizados por cuenta de terceros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','217.01','Pagos realizados por cuenta de terceros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','218','Otros pasivos a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','218.01','Otros pasivos a corto plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','251','Acreedores diversos a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','251.01','Socios, accionistas o representante legal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','251.02','Acreedores diversos a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','251.03','Acreedores diversos a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','251.04','Acreedores diversos a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','251.05','Acreedores diversos a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','251.06','Otros acreedores diversos a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','252','Cuentas por pagar a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.01','Documentos bancarios y financieros por pagar a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.02','Documentos bancarios y financieros por pagar a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.03','Documentos y cuentas por pagar a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.04','Documentos y cuentas por pagar a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.05','Documentos y cuentas por pagar a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.06','Documentos y cuentas por pagar a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.07','Hipotecas por pagar a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.08','Hipotecas por pagar a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.09','Hipotecas por pagar a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.10','Hipotecas por pagar a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.11','Intereses por pagar a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.12','Intereses por pagar a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.13','Intereses por pagar a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.14','Intereses por pagar a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.15','Dividendos por pagar nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.16','Dividendos por pagar extranjeros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','252.17','Otras cuentas y documentos por pagar a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','253','Cobros anticipados a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.01','Rentas cobradas por anticipado a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.02','Rentas cobradas por anticipado a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.03','Rentas cobradas por anticipado a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.04','Rentas cobradas por anticipado a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.05','Intereses cobrados por anticipado a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.06','Intereses cobrados por anticipado a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.07','Intereses cobrados por anticipado a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.08','Intereses cobrados por anticipado a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.09','Factoraje financiero cobrados por anticipado a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.10','Factoraje financiero cobrados por anticipado a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.11','Factoraje financiero cobrados por anticipado a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.12','Factoraje financiero cobrados por anticipado a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.13','Arrendamiento financiero cobrados por anticipado a largo plazo nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.14','Arrendamiento financiero cobrados por anticipado a largo plazo extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.15','Arrendamiento financiero cobrados por anticipado a largo plazo nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.16','Arrendamiento financiero cobrados por anticipado a largo plazo extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.17','Derechos fiduciarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','253.18','Otros cobros anticipados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','254','Instrumentos financieros a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','254.01','Instrumentos financieros a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','255','Pasivos por beneficios a los empleados a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','255.01','Pasivos por beneficios a los empleados a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','256','Otros pasivos a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','256.01','Otros pasivos a largo plazo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','257','Participación de los trabajadores en las utilidades diferida');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','257.01','Participación de los trabajadores en las utilidades diferida');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','258','Obligaciones contraídas de fideicomisos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','258.01','Obligaciones contraídas de fideicomisos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','259','Impuestos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','259.01','ISR diferido');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','259.02','ISR por dividendo diferido');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','259.03','Otros impuestos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','260','Pasivos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','260.01','Pasivos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','301','Capital social');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','301.01','Capital fijo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','301.02','Capital variable');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','301.03','Aportaciones para futuros aumentos de capital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','301.04','Prima en suscripción de acciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','301.05','Prima en suscripción de partes sociales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','302','Patrimonio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','302.01','Patrimonio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','302.02','Aportación patrimonial');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','302.03','Déficit o remanente del ejercicio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','303','Reserva legal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','303.01','Reserva legal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','304','Resultado de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','304.01','Utilidad de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','304.02','Pérdida de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','304.03','Resultado integral de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','304.04','Déficit o remanente de ejercicio anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','305','Resultado del ejercicio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','305.01','Utilidad del ejercicio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','305.02','Pérdida del ejercicio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','305.03','Resultado integral');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','306','Otras cuentas de capital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','306.01','Otras cuentas de capital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','401','Ingresos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.01','Ventas y/o servicios gravados a la tasa general');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.02','Ventas y/o servicios gravados a la tasa general de contado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.03','Ventas y/o servicios gravados a la tasa general a crédito');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.04','Ventas y/o servicios gravados al 0%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.05','Ventas y/o servicios gravados al 0% de contado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.06','Ventas y/o servicios gravados al 0% a crédito');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.07','Ventas y/o servicios exentos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.08','Ventas y/o servicios exentos de contado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.09','Ventas y/o servicios exentos a crédito');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.10','Ventas y/o servicios gravados a la tasa general nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.11','Ventas y/o servicios gravados a la tasa general extranjeros partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.12','Ventas y/o servicios gravados al 0% nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.13','Ventas y/o servicios gravados al 0% extranjeros partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.14','Ventas y/o servicios exentos nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.15','Ventas y/o servicios exentos extranjeros partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.16','Ingresos por servicios administrativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.17','Ingresos por servicios administrativos nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.18','Ingresos por servicios administrativos extranjeros partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.19','Ingresos por servicios profesionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.20','Ingresos por servicios profesionales nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.21','Ingresos por servicios profesionales extranjeros partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.22','Ingresos por arrendamiento');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.23','Ingresos por arrendamiento nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.24','Ingresos por arrendamiento extranjeros partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.25','Ingresos por exportación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.26','Ingresos por comisiones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.27','Ingresos por maquila');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.28','Ingresos por coordinados');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.29','Ingresos por regalías');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.30','Ingresos por asistencia técnica');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.31','Ingresos por donativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.32','Ingresos por intereses (actividad propia)');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.33','Ingresos de copropiedad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.34','Ingresos por fideicomisos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.35','Ingresos por factoraje financiero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.36','Ingresos por arrendamiento financiero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.37','Ingresos de extranjeros con establecimiento en el país');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','401.38','Otros ingresos propios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','402','Devoluciones, descuentos o bonificaciones sobre ingresos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','402.01','Devoluciones, descuentos o bonificaciones sobre ventas y/o servicios a la tasa general');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','402.02','Devoluciones, descuentos o bonificaciones sobre ventas y/o servicios al 0%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','402.03','Devoluciones, descuentos o bonificaciones sobre ventas y/o servicios exentos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','402.04','Devoluciones, descuentos o bonificaciones de otros ingresos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','403','Otros ingresos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','403.01','Otros Ingresos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','403.02','Otros ingresos nacionales parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','403.03','Otros ingresos extranjeros parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','403.04','Ingresos por operaciones discontinuas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','403.05','Ingresos por condonación de adeudo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','501','Costo de venta y/o servicio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','501.01','Costo de venta');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','501.02','Costo de servicios (Mano de obra)');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','501.03','Materia prima directa utilizada para la producción');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','501.04','Materia prima consumida en el proceso productivo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','501.05','Mano de obra directa consumida');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','501.06','Mano de obra directa');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','501.07','Cargos indirectos de producción');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','501.08','Otros conceptos de costo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','502','Compras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','502.01','Compras nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','502.02','Compras nacionales parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','502.03','Compras de Importación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','502.04','Compras de Importación partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','503','Devoluciones, descuentos o bonificaciones sobre compras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','503.01','Devoluciones, descuentos o bonificaciones sobre compras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','504','Otras cuentas de costos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.01','Gastos indirectos de fabricación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.02','Gastos indirectos de fabricación de partes relacionadas nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.03','Gastos indirectos de fabricación de partes relacionadas extranjeras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.04','Otras cuentas de costos incurridos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.05','Otras cuentas de costos incurridos con partes relacionadas nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.06','Otras cuentas de costos incurridos con partes relacionadas extranjeras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.07','Depreciación de edificios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.08','Depreciación de maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.09','Depreciación de automóviles, autobuses, camiones de carga, tractocamiones, montacargas y remolques');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.10','Depreciación de mobiliario y equipo de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.11','Depreciación de equipo de cómputo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.12','Depreciación de equipo de comunicación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.13','Depreciación de activos biológicos, vegetales y semovientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.14','Depreciación de otros activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.15','Depreciación de ferrocarriles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.16','Depreciación de embarcaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.17','Depreciación de aviones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.18','Depreciación de troqueles, moldes, matrices y herramental');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.19','Depreciación de equipo de comunicaciones telefónicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.20','Depreciación de equipo de comunicación satelital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.21','Depreciación de equipo de adaptaciones para personas con capacidades diferentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.22','Depreciación de maquinaria y equipo de generación de energía de fuentes renovables o de sistemas de cogeneración de electricidad eficiente');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.23','Depreciación de adaptaciones y mejoras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.24','Depreciación de otra maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','504.25','Otras cuentas de costos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','505','Costo de activo fijo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','505.01','Costo por venta de activo fijo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','505.02','Costo por baja de activo fijo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','601',' Gastos generales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.01',' Sueldos y salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.02',' Compensaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.03',' Tiempos extras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.04',' Premios de asistencia');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.05',' Premios de puntualidad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.06',' Vacaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.07',' Prima vacacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.08',' Prima dominical');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.09',' Días festivos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.10',' Gratificaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.11',' Primas de antigüedad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.12',' Aguinaldo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.13',' Indemnizaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.14',' Destajo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.15',' Despensa');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.16',' Transporte');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.17',' Servicio médico');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.18',' Ayuda en gastos funerarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.19',' Fondo de ahorro');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.20',' Cuotas sindicales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.21',' PTU');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.22',' Estímulo al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.23',' Previsión social');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.24',' Aportaciones para el plan de jubilación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.25',' Otras prestaciones al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.26',' Cuotas al IMSS');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.27',' Aportaciones al infonavit');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.28',' Aportaciones al SAR');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.29',' Impuesto estatal sobre nóminas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.30',' Otras aportaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.31',' Asimilados a salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.32',' Servicios administrativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.33',' Servicios administrativos partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.34',' Honorarios a personas físicas residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.35',' Honorarios a personas físicas residentes nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.36',' Honorarios a personas físicas residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.37',' Honorarios a personas físicas residentes del extranjero partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.38',' Honorarios a personas morales residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.39',' Honorarios a personas morales residentes nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.40',' Honorarios a personas morales residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.41',' Honorarios a personas morales residentes del extranjero partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.42',' Honorarios aduanales personas físicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.43',' Honorarios aduanales personas morales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.44',' Honorarios al consejo de administración');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.45','Arrendamiento a personas físicas residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.46','Arrendamiento a personas morales residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.47','Arrendamiento a residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.48','Combustibles y lubricantes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.49','Viáticos y gastos de viaje');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.50','Teléfono, internet');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.51','Agua');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.52','Energía eléctrica');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.53','Vigilancia y seguridad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.54','Limpieza');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.55','Papelería y artículos de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.56','Mantenimiento y conservación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.57','Seguros y fianzas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.58','Otros impuestos y derechos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.59','Recargos fiscales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.60','Cuotas y suscripciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.61','Propaganda y publicidad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.62','Capacitación al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.63','Donativos y ayudas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.64','Asistencia técnica');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.65','Regalías sujetas a otros porcentajes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.66','Regalías sujetas al 5%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.67','Regalías sujetas al 10%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.68','Regalías sujetas al 15%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.69','Regalías sujetas al 25%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.70','Regalías sujetas al 30%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.71','Regalías sin retención');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.72','Fletes y acarreos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.73','Gastos de importación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.74','Comisiones sobre ventas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.75','Comisiones por tarjetas de crédito');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.76','Patentes y marcas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.77','Uniformes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.78','Prediales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.79','Gastos generales de urbanización');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.80','Gastos generales de construcción');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.81','Fletes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.82','Recolección de bienes del sector agropecuario y/o ganadero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.83','Gastos no deducibles (sin requisitos fiscales)');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','601.84','Otros gastos generales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','602','Gastos de venta');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.01','Sueldos y salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.02','Compensaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.03','Tiempos extras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.04','Premios de asistencia');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.05','Premios de puntualidad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.06','Vacaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.07','Prima vacacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.08','Prima dominical');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.09','Días festivos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.10','Gratificaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.11','Primas de antigüedad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.12','Aguinaldo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.13','Indemnizaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.14','Destajo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.15','Despensa');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.16','Transporte');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.17','Servicio médico');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.18','Ayuda en gastos funerarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.19','Fondo de ahorro');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.20','Cuotas sindicales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.21','PTU');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.22','Estímulo al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.23','Previsión social');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.24','Aportaciones para el plan de jubilación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.25','Otras prestaciones al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.26','Cuotas al IMSS');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.27','Aportaciones al infonavit');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.28','Aportaciones al SAR');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.29','Impuesto estatal sobre nóminas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.30','Otras aportaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.31','Asimilados a salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.32','Servicios administrativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.33','Servicios administrativos partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.34','Honorarios a personas físicas residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.35','Honorarios a personas físicas residentes nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.36','Honorarios a personas físicas residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.37','Honorarios a personas físicas residentes del extranjero partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.38','Honorarios a personas morales residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.39','Honorarios a personas morales residentes nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.40','Honorarios a personas morales residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.41','Honorarios a personas morales residentes del extranjero partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.42','Honorarios aduanales personas físicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.43','Honorarios aduanales personas morales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.44','Honorarios al consejo de administración');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.45','Arrendamiento a personas físicas residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.46','Arrendamiento a personas morales residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.47','Arrendamiento a residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.48','Combustibles y lubricantes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.49','Viáticos y gastos de viaje');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.50','Teléfono, internet');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.51','Agua');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.52','Energía eléctrica');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.53','Vigilancia y seguridad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.54','Limpieza');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.55','Papelería y artículos de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.56','Mantenimiento y conservación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.57','Seguros y fianzas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.58','Otros impuestos y derechos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.59','Recargos fiscales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.60','Cuotas y suscripciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.61','Propaganda y publicidad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.62','Capacitación al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.63','Donativos y ayudas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.64','Asistencia técnica');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.65','Regalías sujetas a otros porcentajes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.66','Regalías sujetas al 5%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.67','Regalías sujetas al 10%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.68','Regalías sujetas al 15%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.69','Regalías sujetas al 25%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.70','Regalías sujetas al 30%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.71','Regalías sin retención');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.72','Fletes y acarreos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.73','Gastos de importación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.74','Comisiones sobre ventas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.75','Comisiones por tarjetas de crédito');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.76','Patentes y marcas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.77','Uniformes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.78','Prediales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.79','Gastos de venta de urbanización');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.80','Gastos de venta de construcción');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.81','Fletes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.82','Recolección de bienes del sector agropecuario y/o ganadero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.83','Gastos no deducibles (sin requisitos fiscales)');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','602.84','Otros gastos de venta');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','603','Gastos de administración');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.01','Sueldos y salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.02','Compensaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.03','Tiempos extras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.04','Premios de asistencia');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.05','Premios de puntualidad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.06','Vacaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.07','Prima vacacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.08','Prima dominical');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.09','Días festivos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.10','Gratificaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.11','Primas de antigüedad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.12','Aguinaldo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.13','Indemnizaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.14','Destajo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.15','Despensa');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.16','Transporte');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.17','Servicio médico');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.18','Ayuda en gastos funerarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.19','Fondo de ahorro');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.20','Cuotas sindicales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.21','PTU');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.22','Estímulo al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.23','Previsión social');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.24','Aportaciones para el plan de jubilación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.25','Otras prestaciones al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.26','Cuotas al IMSS');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.27','Aportaciones al infonavit');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.28','Aportaciones al SAR');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.29','Impuesto estatal sobre nóminas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.30','Otras aportaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.31','Asimilados a salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.32','Servicios administrativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.33','Servicios administrativos partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.34','Honorarios a personas físicas residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.35','Honorarios a personas físicas residentes nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.36','Honorarios a personas físicas residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.37','Honorarios a personas físicas residentes del extranjero partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.38','Honorarios a personas morales residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.39','Honorarios a personas morales residentes nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.40','Honorarios a personas morales residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.41','Honorarios a personas morales residentes del extranjero partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.42','Honorarios aduanales personas físicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.43','Honorarios aduanales personas morales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.44','Honorarios al consejo de administración');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.45','Arrendamiento a personas físicas residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.46','Arrendamiento a personas morales residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.47','Arrendamiento a residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.48','Combustibles y lubricantes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.49','Viáticos y gastos de viaje');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.50','Teléfono, internet');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.51','Agua');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.52','Energía eléctrica');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.53','Vigilancia y seguridad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.54','Limpieza');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.55','Papelería y artículos de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.56','Mantenimiento y conservación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.57','Seguros y fianzas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.58','Otros impuestos y derechos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.59','Recargos fiscales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.60','Cuotas y suscripciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.61','Propaganda y publicidad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.62','Capacitación al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.63','Donativos y ayudas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.64','Asistencia técnica');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.65','Regalías sujetas a otros porcentajes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.66','Regalías sujetas al 5%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.67','Regalías sujetas al 10%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.68','Regalías sujetas al 15%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.69','Regalías sujetas al 25%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.70','Regalías sujetas al 30%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.71','Regalías sin retención');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.72','Fletes y acarreos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.73','Gastos de importación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.74','Patentes y marcas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.75','Uniformes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.76','Prediales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.77','Gastos de administración de urbanización');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.78','Gastos de administración de construcción');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.79','Fletes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.80','Recolección de bienes del sector agropecuario y/o ganadero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.81','Gastos no deducibles (sin requisitos fiscales)');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','603.82','Otros gastos de administración');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','604','Gastos de fabricación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.01','Sueldos y salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.02','Compensaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.03','Tiempos extras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.04','Premios de asistencia');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.05','Premios de puntualidad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.06','Vacaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.07','Prima vacacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.08','Prima dominical');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.09','Días festivos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.10','Gratificaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.11','Primas de antigüedad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.12','Aguinaldo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.13','Indemnizaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.14','Destajo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.15','Despensa');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.16','Transporte');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.17','Servicio médico');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.18','Ayuda en gastos funerarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.19','Fondo de ahorro');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.20','Cuotas sindicales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.21','PTU');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.22','Estímulo al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.23','Previsión social');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.24','Aportaciones para el plan de jubilación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.25','Otras prestaciones al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.26','Cuotas al IMSS');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.27','Aportaciones al infonavit');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.28','Aportaciones al SAR');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.29','Impuesto estatal sobre nóminas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.30','Otras aportaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.31','Asimilados a salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.32','Servicios administrativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.33','Servicios administrativos partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.34','Honorarios a personas físicas residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.35','Honorarios a personas físicas residentes nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.36','Honorarios a personas físicas residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.37','Honorarios a personas físicas residentes del extranjero partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.38','Honorarios a personas morales residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.39','Honorarios a personas morales residentes nacionales partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.40','Honorarios a personas morales residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.41','Honorarios a personas morales residentes del extranjero partes relacionadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.42','Honorarios aduanales personas físicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.43','Honorarios aduanales personas morales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.44','Honorarios al consejo de administración');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.45','Arrendamiento a personas físicas residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.46','Arrendamiento a personas morales residentes nacionales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.47','Arrendamiento a residentes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.48','Combustibles y lubricantes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.49','Viáticos y gastos de viaje');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.50','Teléfono, internet');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.51','Agua');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.52','Energía eléctrica');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.53','Vigilancia y seguridad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.54','Limpieza');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.55','Papelería y artículos de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.56','Mantenimiento y conservación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.57','Seguros y fianzas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.58','Otros impuestos y derechos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.59','Recargos fiscales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.60','Cuotas y suscripciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.61','Propaganda y publicidad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.62','Capacitación al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.63','Donativos y ayudas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.64','Asistencia técnica');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.65','Regalías sujetas a otros porcentajes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.66','Regalías sujetas al 5%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.67','Regalías sujetas al 10%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.68','Regalías sujetas al 15%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.69','Regalías sujetas al 25%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.70','Regalías sujetas al 30%');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.71','Regalías sin retención');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.72','Fletes y acarreos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.73','Gastos de importación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.74','Patentes y marcas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.75','Uniformes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.76','Prediales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.77','Gastos de fabricación de urbanización');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.78','Gastos de fabricación de construcción');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.79','Fletes del extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.80','Recolección de bienes del sector agropecuario y/o ganadero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.81','Gastos no deducibles (sin requisitos fiscales)');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','604.82','Otros gastos de fabricación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','605','Mano de obra directa');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.01','Mano de obra');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.02','Sueldos y Salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.03','Compensaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.04','Tiempos extras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.05','Premios de asistencia');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.06','Premios de puntualidad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.07','Vacaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.08','Prima vacacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.09','Prima dominical');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.10','Días festivos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.11','Gratificaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.12','Primas de antigüedad');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.13','Aguinaldo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.14','Indemnizaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.15','Destajo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.16','Despensa');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.17','Transporte');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.18','Servicio médico');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.19','Ayuda en gastos funerarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.20','Fondo de ahorro');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.21','Cuotas sindicales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.22','PTU');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.23','Estímulo al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.24','Previsión social');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.25','Aportaciones para el plan de jubilación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.26','Otras prestaciones al personal');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.27','Asimilados a salarios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.28','Cuotas al IMSS');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.29','Aportaciones al infonavit');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.30','Aportaciones al SAR');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','605.31','Otros costos de mano de obra directa');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','606','Facilidades administrativas fiscales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','606.01','Facilidades administrativas fiscales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','607','Participación de los trabajadores en las utilidades');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','607.01','Participación de los trabajadores en las utilidades');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','608','Participación en resultados de subsidiarias');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','608.01','Participación en resultados de subsidiarias');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','609','Participación en resultados de asociadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','609.01','Participación en resultados de asociadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','610','Participación de los trabajadores en las utilidades diferida');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','610.01','Participación de los trabajadores en las utilidades diferida');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','611','Impuesto Sobre la renta');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','611.01','Impuesto Sobre la renta');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','611.02','Impuesto Sobre la renta por remanente distribuible');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','612','Gastos no deducibles para CUFIN');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','612.01','Gastos no deducibles para CUFIN');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','613','Depreciación contable');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.01','Depreciación de edificios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.02','Depreciación de maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.03','Depreciación de automóviles, autobuses, camiones de carga, tractocamiones, montacargas y remolques');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.04','Depreciación de mobiliario y equipo de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.05','Depreciación de equipo de cómputo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.06','Depreciación de equipo de comunicación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.07','Depreciación de activos biológicos, vegetales y semovientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.08','Depreciación de otros activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.09','Depreciación de ferrocarriles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.10','Depreciación de embarcaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.11','Depreciación de aviones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.12','Depreciación de troqueles, moldes, matrices y herramental');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.13','Depreciación de equipo de comunicaciones telefónicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.14','Depreciación de equipo de comunicación satelital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.15','Depreciación de equipo de adaptaciones para personas con capacidades diferentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.16','Depreciación de maquinaria y equipo de generación de energía de fuentes renovables o de sistemas de cogeneración de electricidad eficiente');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.17','Depreciación de adaptaciones y mejoras');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','613.18','Depreciación de otra maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','614','Amortización contable');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.01','Amortización de gastos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.02','Amortización de gastos pre operativos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.03','Amortización de regalías, asistencia técnica y otros gastos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.04','Amortización de activos intangibles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.05','Amortización de gastos de organización');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.06','Amortización de investigación y desarrollo de mercado');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.07','Amortización de marcas y patentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.08','Amortización de crédito mercantil');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.09','Amortización de gastos de instalación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','614.10','Amortización de otros activos diferidos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','701','Gastos financieros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.01','Pérdida cambiaria');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.02','Pérdida cambiaria nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.03','Pérdida cambiaria extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.04','Intereses a cargo bancario nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.05','Intereses a cargo bancario extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.06','Intereses a cargo de personas físicas nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.07','Intereses a cargo de personas físicas extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.08','Intereses a cargo de personas morales nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.09','Intereses a cargo de personas morales extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.10','Comisiones bancarias');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','701.11','Otros gastos financieros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','702','Productos financieros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.01','Utilidad cambiaria');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.02','Utilidad cambiaria nacional parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.03','Utilidad cambiaria extranjero parte relacionada');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.04','Intereses a favor bancarios nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.05','Intereses a favor bancarios extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.06','Intereses a favor de personas físicas nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.07','Intereses a favor de personas físicas extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.08','Intereses a favor de personas morales nacional');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.09','Intereses a favor de personas morales extranjero');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','702.10','Otros productos financieros');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','703','Otros gastos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.01','Pérdida en venta y/o baja de terrenos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.02','Pérdida en venta y/o baja de edificios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.03','Pérdida en venta y/o baja de maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.04','Pérdida en venta y/o baja de automóviles, autobuses, camiones de carga, tractocamiones, montacargas y remolques');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.05','Pérdida en venta y/o baja de mobiliario y equipo de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.06','Pérdida en venta y/o baja de equipo de cómputo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.07','Pérdida en venta y/o baja de equipo de comunicación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.08','Pérdida en venta y/o baja de activos biológicos, vegetales y semovientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.09','Pérdida en venta y/o baja de otros activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.10','Pérdida en venta y/o baja de ferrocarriles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.11','Pérdida en venta y/o baja de embarcaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.12','Pérdida en venta y/o baja de aviones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.13','Pérdida en venta y/o baja de troqueles, moldes, matrices y herramental');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.14','Pérdida en venta y/o baja de equipo de comunicaciones telefónicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.15','Pérdida en venta y/o baja de equipo de comunicación satelital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.16','Pérdida en venta y/o baja de equipo de adaptaciones para personas con capacidades diferentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.17','Pérdida en venta y/o baja de maquinaria y equipo de generación de energía de fuentes renovables o de sistemas de cogeneración de electricidad eficiente');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.18','Pérdida en venta y/o baja de otra maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.19','Pérdida por enajenación de acciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.20','Pérdida por enajenación de partes sociales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','703.21','Otros gastos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','704','Otros productos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.01','Ganancia en venta y/o baja de terrenos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.02','Ganancia en venta y/o baja de edificios');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.03','Ganancia en venta y/o baja de maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.04','Ganancia en venta y/o baja de automóviles, autobuses, camiones de carga, tractocamiones, montacargas y remolques');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.05','Ganancia en venta y/o baja de mobiliario y equipo de oficina');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.06','Ganancia en venta y/o baja de equipo de cómputo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.07','Ganancia en venta y/o baja de equipo de comunicación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.08','Ganancia en venta y/o baja de activos biológicos, vegetales y semovientes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.09','Ganancia en venta y/o baja de otros activos fijos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.10','Ganancia en venta y/o baja de ferrocarriles');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.11','Ganancia en venta y/o baja de embarcaciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.12','Ganancia en venta y/o baja de aviones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.13','Ganancia en venta y/o baja de troqueles, moldes, matrices y herramental');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.14','Ganancia en venta y/o baja de equipo de comunicaciones telefónicas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.15','Ganancia en venta y/o baja de equipo de comunicación satelital');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.16','Ganancia en venta y/o baja de equipo de adaptaciones para personas con capacidades diferentes');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.17','Ganancia en venta de maquinaria y equipo de generación de energía de fuentes renovables o de sistemas de cogeneración de electricidad eficiente');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.18','Ganancia en venta y/o baja de otra maquinaria y equipo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.19','Ganancia por enajenación de acciones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.20','Ganancia por enajenación de partes sociales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.21','Ingresos por estímulos fiscales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.22','Ingresos por condonación de adeudo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','704.23','Otros productos');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','801','UFIN del ejercicio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','801.01','UFIN');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','801.02','Contra cuenta UFIN');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','802','CUFIN del ejercicio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','802.01','CUFIN');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','802.02','Contra cuenta CUFIN');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','803','CUFIN de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','803.01','CUFIN de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','803.02','Contra cuenta CUFIN de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','804','CUFINRE del ejercicio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','804.01','CUFINRE');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','804.02','Contra cuenta CUFINRE');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','805','CUFINRE de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','805.01','CUFINRE de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','805.02','Contra cuenta CUFINRE de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','806','CUCA del ejercicio');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','806.01','CUCA');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','806.02','Contra cuenta CUCA');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','807','CUCA de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','807.01','CUCA de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','807.02','Contra cuenta CUCA de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','808','Ajuste anual por inflación acumulable');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','808.01','Ajuste anual por inflación acumulable');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','808.02','Acumulación del ajuste anual inflacionario');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','809','Ajuste anual por inflación deducible');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','809.01','Ajuste anual por inflación deducible');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','809.02','Deducción del ajuste anual inflacionario');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','810','Deducción de inversión');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','810.01','Deducción de inversión');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','810.02','Contra cuenta deducción de inversiones');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','811','Utilidad o pérdida fiscal en venta y/o baja de activo fijo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','811.01','Utilidad o pérdida fiscal en venta y/o baja de activo fijo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','811.02','Contra cuenta utilidad o pérdida fiscal en venta y/o baja de activo fijo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','812','Utilidad o pérdida fiscal en venta acciones o partes sociales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','812.01','Utilidad o pérdida fiscal en venta acciones o partes sociales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','812.02','Contra cuenta utilidad o pérdida fiscal en venta acciones o partes sociales');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','813','Pérdidas fiscales pendientes de amortizar actualizadas de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','813.01','Pérdidas fiscales pendientes de amortizar actualizadas de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','813.02','Actualización de pérdidas fiscales pendientes de amortizar de ejercicios anteriores');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','814','Mercancías recibidas en consignación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','814.01','Mercancías recibidas en consignación');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','814.02','Consignación de mercancías recibidas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','815','Crédito fiscal de IVA e IEPS por la importación de mercancías para empresas certificadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','815.01','Crédito fiscal de IVA e IEPS por la importación de mercancías');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','815.02','Importación de mercancías con aplicación de crédito fiscal de IVA e IEPS');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','816','Crédito fiscal de IVA e IEPS por la importación de activos fijos para empresas certificadas');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','816.01','Crédito fiscal de IVA e IEPS por la importación de activo fijo');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','816.02','Importación de activo fijo con aplicación de crédito fiscal de IVA e IEPS');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('1','899','Otras cuentas de orden');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','899.01','Otras cuentas de orden');
insert into tbl_sat_codagrup (nivel, codigo, descripcion)
values('2','899.02','Contra cuenta otras cuentas de orden');

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
-----------------1.- Soporte para existencias y movimientos al almacén de Utensilios en GASTOS de Uso Interno.
-----------------2.- Soporte para seleccionar un respaldo independiente de una base de datos inexistente
-----------------3.- En respaldos, agregar el archivo informativo de versión forseti.version para cada base de datos y cada respaldo completo.
-----------------4.- Agregar catalogo de codigos agrupadores del sat para soporte a la agregación y cambios de cuentas contables
-----------------5.- Agregar soporte a proveedores y clientes para no poder ingresar facturas a crédito en caso de no tiener crédito o si ya rebasaron su límite de credito. 
-----------------6.- Agregar soporte a la asociación de productos en compras o gastos con proveedor de mostrador para el proceso de enlazar compra o gasto... Que haya base de datos del proveedor cero
-----------------7.- Permitir que se aplique IVA mayor de 0% al agregar o cambiar entidades de venta o compras no contables para que se pueda especificar IVA en una compra o venta no contable.
-----------------8.- Agregar soporte para la eliminación de rangos en los módulos de las tablas de nómina.
-----------------9.- Eliminar todos los impuestos al momento de definir un nuevo movimiento de nómina, menos el ISPT, y agregar el catálogo del SAT a una lista desplegable.
-----------------10.- Impedir la cancelación de facturas y reversión de movimientos al almacén que provienen de facturas cuando ya exista un cierre de caja que la contemple.
-----------------11.- Agregar soporte de consulta y eliminación de crédito fonacot.
12.- Agregar soporte para incapacidades y horas extras del recibo de nómina para el CFDI generado directamente en forseti (INDISPENSABLE) y para vedia también. Aparte, esto mismo al momento de enlazar desde un archivo xml externo.

CHECAR CON VEDIA EL CALCULO DE Descuentos por inconsistencias en sp_calculo_nómina_faltas..... Aqui en forseti no aplicaba DIAS SIN PAGO DE TIEMPO. y se modificó.
ELIMINAR JMsj antes de preparar la actualización
COPIAR sp_cajas_cierrez de RYRSA antes de actualizar para que a partir de esta copia se pueda volver a reconstruir soporte para OPT sobre el procedimiento actualizado.
*/


