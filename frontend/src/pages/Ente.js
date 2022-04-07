import React, {useState, Fragment, useEffect} from 'react'
import Grid from '@mui/material/Grid';
import Table from '../components/Table';
import Button from '@mui/material/Button';
import {useLocation, useNavigate} from "react-router-dom";
import {ENTE, ENTE_EDIT, ROOT} from "../navigation/CONSTANTS";
import {getEntities} from "../services/services";
import {Alert, Stack} from "@mui/material";
import ArrowBackIos from '@mui/icons-material/ArrowBackIos';

const Ente = () => {
  const columns = [
    {
      field: 'id',
      headerName: 'ID',
      width: 200
    },
    {
      field: 'name',
      headerName: 'Nombre',
      width: 200,
    },
    {
      field: 'activated',
      headerName: 'Activo',
      width: 200,
    },
    {
      field: "edit",
      headerName: 'Acciones',
      width: 200,
      renderCell: (cellValues) => {
        return (
          <>
            <Button
              variant="contained"
              color="primary"
              onClick={(event) => {
                handleClick(event, cellValues);
              }}
            >
              Editar
            </Button>
          </>
        );
      }
    }
  ];

  const history = useNavigate();
  const location = useLocation();
  const [error, setError] = useState({text: location?.state?.msg || ""});
  const [entities, setEntities] = useState([])
  const handleClick = (event, cellValues) => {
    history(`${ENTE_EDIT}/${cellValues.row.id}`);
  };

  useEffect(() => {
    fetchEntities();
  }, []);

  function fetchEntities(id) {
    getEntities(id)
      .then((data) => {
        let entityArr = data.map((entity) => {
          return {...entity, activated: entity.activated === true ? "SI" : "NO"}
        });
        setEntities(entityArr)
      })
      .catch(error => {
        console.warn(error);
        history(ENTE);
      });
  }

  const handleNew = () => {
    history(ENTE_EDIT);
  }

  const handleBack = () => {
    history(ROOT);
  }

  return (
    <Grid container>
      <Grid item xs={6}>
        {error?.text && (
          <Alert severity="success">{error.text}</Alert>
        )}
      </Grid>
      <Grid item xs={10}><h1>ENTES
        DE CONTROL</h1></Grid>
      <Grid item xs={2} style={{marginTop: 20}}>
        <Stack direction="row" spacing={2}>
          <Button
            variant="outlined"
            onClick={handleBack}
            color="secondary"
            startIcon={<ArrowBackIos/>}/>
          <Button
            variant="contained"
            color="primary"
            onClick={handleNew}
          >
            Nuevo
          </Button>
        </Stack>
      </Grid>
      <Table rows={entities} columns={columns}/>
    </Grid>
  )
}

export default Ente;
