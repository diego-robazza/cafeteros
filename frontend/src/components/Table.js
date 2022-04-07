import * as React from 'react';
import { DataGrid } from '@mui/x-data-grid';

  export default function Table(props) {
    const {
        rows,
        columns
      } = props;
    return (
      <div style={{ height: 500, width: '100%' }}>
        <DataGrid
          rows={rows}
          columns={columns}
          pageSize={20}
          rowsPerPageOptions={[20]}
          disableSelectionOnClick
        />
      </div>
    );
  }
