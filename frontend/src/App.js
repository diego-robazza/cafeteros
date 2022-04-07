import React from 'react'
import { BrowserRouter } from "react-router-dom";
import { RouterConfig } from "./navigation/RouterConfig"

const App = () => {	
	return (
		<div>
      <BrowserRouter>
        <RouterConfig />
      </BrowserRouter>
    </div>
	)
}

export default App;
