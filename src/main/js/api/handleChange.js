export function handleChange(setFormData) {
    return (event) => {
        const {name, value} = event.target
        setFormData((prevFormData) => ({...prevFormData, [name]: value}))
    }
}
