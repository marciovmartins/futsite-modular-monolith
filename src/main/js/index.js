import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {createRoot} from 'react-dom/client';
import App from "./page/App";
import {AmateurSoccerGroupPage} from "./page/amateurSoccerGroups/AmateurSoccerGroupPage";
import {GamedayPage} from "./page/gamedays/GamedayPage";

const container = document.getElementById('react');
const root = createRoot(container);

root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App/>}>
                    <Route path="/amateurSoccerGroups/*" element={<AmateurSoccerGroupPage/>}/>
                    <Route path="/gamedays/*" element={<GamedayPage/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>,
)