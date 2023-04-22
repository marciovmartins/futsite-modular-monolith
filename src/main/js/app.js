import {BrowserRouter, Link, Outlet, Route, Routes} from "react-router-dom";
import Home from './components/Home'
import {AmateurSoccerGroupPage} from "./components/amateurSoccerGroups/amateurSoccerGroupPage";
import {AmateurSoccerGroupList} from "./components/amateurSoccerGroups/amateurSoccerGroupList";

const React = require('react');
const ReactDOM = require('react-dom');

export default function App() {
    return (
        <div>
            <nav>
                <Link to="/">Home</Link>&nbsp;|&nbsp;
                <Link to="/amateurSoccerGroups">Amateur Soccer Groups</Link>&nbsp;|&nbsp;
            </nav>
            <Outlet/>
        </div>
    )
}

ReactDOM.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App/>}>
                    <Route index element={<Home/>}/>
                    <Route path="amateurSoccerGroups" element={<AmateurSoccerGroupPage/>}>
                        <Route index element={<AmateurSoccerGroupList/>}/>
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>,
    document.getElementById('root')
)