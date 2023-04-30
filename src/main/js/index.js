import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import HomePage from './page/HomePage'
import {AmateurSoccerGroupPage} from "./page/amateurSoccerGroups/AmateurSoccerGroupPage";
import {createRoot} from 'react-dom/client';
import App from "./page/App";

const container = document.getElementById('react');
const root = createRoot(container);

root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App/>}>
                    <Route index element={<HomePage/>}/>
                    <Route path="/amateurSoccerGroups/*" element={<AmateurSoccerGroupPage/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>,
)