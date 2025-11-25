// SPDX-License-Identifier: MIT
pragma solidity ^0.7.6; // older compiler to illustrate overflow risk

contract TokenV1 {
    mapping(address => uint256) public balances;

    function mint(address to, uint256 amount) external {
        balances[to] += amount; // overflow possible in <0.8
    }
}
