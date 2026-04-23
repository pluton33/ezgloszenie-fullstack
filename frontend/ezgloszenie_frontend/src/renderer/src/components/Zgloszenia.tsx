import React from "react"
import "../assets/zgloszenia.css"
import { Car, TriangleAlert, HandFist, HandCoins, Pill, CircleEllipsis } from "lucide-react"


function Zgloszenia(): React.JSX.Element {
    return (
        <div className="zgl-body" style={{ margin: '10px' }}>
            <div className="zgl-panel1">
                <div style={{ padding: '10px' }}>
                    <h2>Zgłoś zdarzenie online</h2>
                    <br></br>
                    <span>Wypełnij formularz w kilka minut. Nie musisz osobiście odwiedzać komisariatu - Twoje zgłoszenie trafi bezpośrednio do właściwej jednostki</span>
                </div>
                <div className="zgl-przyciski">
                    <span style={{ padding: '10px' }}>Wybierz kategorię zdarzenia</span>
                    <div className="zgl-przyciski2">
                        <button className="zgl-btn">
                            <Car />
                            <span className="btn-name">Wykroczenie drogowe</span>
                        </button>
                        <button className="zgl-btn">
                            <TriangleAlert />
                            <span className="btn-name">Zakłócanie porzadku</span>
                        </button>
                        <button className="zgl-btn">
                            <HandFist />
                            <span className="btn-name">Wandalizm</span>
                        </button>
                    </div>
                    <div className="zgl-przyciski2">
                        <button className="zgl-btn">
                            <HandCoins />
                            <span className="btn-name">Kradzież</span>
                        </button>
                        <button className="zgl-btn">
                            <Pill />
                            <span className="btn-name">Narkotyki</span>
                        </button>
                        <button className="zgl-btn">
                            <CircleEllipsis />
                            <span className="btn-name">Inne</span>
                        </button>
                    </div>
                </div>

            </div>
            <div className="zgl-panel2">

            </div>
        </div>
    )
}

export default Zgloszenia
