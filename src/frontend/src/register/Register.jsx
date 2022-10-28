import React, {useCallback, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Checkbox, Flex, Heading, TextField, Link, Button} from "@adobe/react-spectrum";
import styled from "styled-components";
import {AppName} from "../app-name/AppName";
import axios from "axios";
import toast from "react-hot-toast";
import {Toast} from "../toast/Toast";

export const Register = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [alias, setAlias] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [agree, setAgree] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const register = useCallback(async () => {
        try {
            setError('');
            await axios.post('/accounts', {
                firstName,
                lastName,
                alias,
                email,
                password
            });
            navigate('/login');
        } catch (e) {
            setError('Invalid input!');
        }
    }, [firstName, lastName, alias, email, password]);

    useEffect(() => {
        if (error) {
            toast.custom((t) => {
                return (
                    <Toast
                        message={error}
                        type="negative"
                        dismissFn={() => toast.remove(t.id)}
                    />
                );
            });
        }
    }, [error]);

    const isValid = useCallback(() => {
        return email.endsWith('@rice.edu') && password.length > 1 && password === confirmPassword && agree && alias.length > 1;
    }, [email, password, confirmPassword, agree, alias]);

    return (<Flex direction="row" height="100vh" width="100vw" >
        <Flex direction="column" width="600px" gap="size-100" alignItems="center" justifyContent="center">
            <Heading level="1">
                <AppName />
            </Heading>
            <Heading level="3">
                Register
            </Heading>
            <TextField
                validationState={error ? 'invalid' : undefined}
                label="First Name"
                value={firstName}
                onChange={setFirstName}
                type="text"
                onBlur={() => setError('')}
                isRequired
            />
            <TextField
                validationState={error ? 'invalid' : undefined}
                label="Last Name"
                value={lastName}
                onChange={setLastName}
                type="text"
                onBlur={() => setError('')}
                isRequired
            />
            <TextField
                validationState={error ? 'invalid' : undefined}
                label="Alias"
                value={alias}
                onChange={setAlias}
                type="text"
                onBlur={() => setError('')}
                isRequired
            />
            <TextField
                validationState={error ? 'invalid' : undefined}
                label="Email"
                value={email}
                onChange={setEmail}
                type="email"
                onBlur={() => setError('')}
                isRequired
            />
            <TextField
                validationState={error ? 'invalid' : undefined}
                label="Password"
                value={password}
                onChange={setPassword}
                type="password"
                onBlur={() => setError('')}
                isRequired
            />
            <TextField
                validationState={error ? 'invalid' : undefined}
                label="Confirm Password"
                value={confirmPassword}
                onChange={setConfirmPassword}
                type="password"
                onBlur={() => setError('')}
                isRequired
            />
            <Link isQuiet>Terms, Conditions, and Privacy Policy</Link>
            <Checkbox isSelected={agree} onChange={setAgree}>
                I Agree to the Above
            </Checkbox>
            <Button
                variant="cta"
                onPress={register}
                isDisabled={!isValid()}
            >
                Register
            </Button>
        </Flex>
        <BackgroundDiv />
    </Flex>)
}

const BackgroundDiv = styled.div`
    background-image: url('/images/register.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    width: calc(100% - 600px);
    `