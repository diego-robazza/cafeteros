import React, {useState, Fragment, useEffect} from 'react'
import Grid from '@mui/material/Grid';
import Table from '../components/Table';
import Button from '@mui/material/Button';
import {useLocation, useNavigate} from "react-router-dom";
import {AUDITORIA_EDIT, ENTE, ROOT} from "../navigation/CONSTANTS";
import {getInventory} from "../services/services";
import moment from "moment";
import {Alert, Stack} from "@mui/material";

const Home = () => {
  const columns = [
    {field: 'id', headerName: 'ID', width: 90},
    {
      field: 'nameAudit',
      headerName: 'Nombre',
      width: 200,
    },
    {
      field: 'ente',
      headerName: 'Ente Control',
      width: 200,
      valueGetter: (params) => {
        return params.value.name
      }
    },
    {
      field: 'startDate',
      headerName: 'Fecha',
      width: 200,
    },
    {
      field: 'auditStatus',
      headerName: 'Estado',
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
  const [inventory, setInventory] = useState([])
  const handleClick = (event, cellValues) => {
    history(`${AUDITORIA_EDIT}/${cellValues.row.id}`);
  };

  useEffect(() => {
    fetchInventory();
  }, []);

  function fetchInventory(id) {
    getInventory(id)
      .then((data) => {
        let inventoryArr = data.map((item) => {
          return {...item, startDate: moment(item.startDate).format("YYYY-MM-DD")}
        });
        setInventory(inventoryArr)
      })
      .catch(error => {
        console.warn(error);
        history(ROOT);
      });
  }

  const handleNew = () => {
    history(AUDITORIA_EDIT);
  }

  const handleEntity = () => {
    history(ENTE);
  }

  return (
    <Grid container>
      <Grid item xs={6}>
        {error?.text && (
          <Alert severity="success">{error.text}</Alert>
        )}
      </Grid>
      <Grid item xs={10}><h1>AUDITORIAS</h1></Grid>
      <Grid item xs={2} style={{marginTop: 20}}>
        <Stack direction="row" spacing={2}>
          <Button
            variant="contained"
            color="primary"
            onClick={handleNew}
          >
            Nuevo
          </Button>
          <Button
            variant="contained"
            color="warning"
            onClick={handleEntity}
          >
            Entes
          </Button>
        </Stack>
      </Grid>
      <Table rows={inventory} columns={columns}/>
    </Grid>
  )
}

export default Home;
